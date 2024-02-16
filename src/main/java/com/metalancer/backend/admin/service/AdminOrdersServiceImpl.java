package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.dto.AdminOrderDTO.AllRefund;
import com.metalancer.backend.admin.dto.AdminOrderDTO.PartialRefund;
import com.metalancer.backend.category.repository.TagsRepository;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.orders.repository.OrderPaymentRepository;
import com.metalancer.backend.orders.repository.OrderProductsRepository;
import com.metalancer.backend.orders.repository.OrdersRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.io.IOException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class AdminOrdersServiceImpl implements AdminOrdersService {

    @Value("${iamport.api.key}")
    private String apiKey;
    @Value("${iamport.api.secret}")
    private String apiSecret;
    @Value("${iamport.imp_uid}")
    private String impUid;
    private final TagsRepository tagsRepository;
    private final OrdersRepository ordersRepository;
    private final OrderProductsRepository orderProductsRepository;
    private final OrderPaymentRepository orderPaymentRepository;

    @Override
    public Boolean refundAll(AllRefund dto, PrincipalDetails user)
        throws IamportResponseException, IOException {
        String merchantUid = dto.getOrderNo();
        String reason = dto.getReason();
        BigDecimal checksum = dto.getChecksum();
        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        CancelData cancelData = new CancelData(merchantUid, false);
        cancelData.setReason(reason);
        cancelData.setChecksum(checksum);
        // 캔슬 진행
        IamportResponse<Payment> canceledPayment = client.cancelPaymentByImpUid(cancelData);
        log.info("결제취소 응답: {}", canceledPayment);
        return true;
    }

    @Override
    public Boolean refundPartially(PartialRefund dto, PrincipalDetails user)
        throws IamportResponseException, IOException {
        String merchantUid = dto.getOrderNo();
        String reason = dto.getReason();
        BigDecimal amount = dto.getAmount();
        BigDecimal checksum = dto.getChecksum();
        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        CancelData cancelData = new CancelData(merchantUid, false, amount);
        cancelData.setReason(reason);
        cancelData.setChecksum(checksum);
        if (dto.getVatAmount() != null && dto.getVatAmount().compareTo(BigDecimal.ZERO) > 0) {
            // 부가세 같은거?
        }
        // 캔슬 진행
        IamportResponse<Payment> canceledPayment = client.cancelPaymentByImpUid(cancelData);
        log.info("결제취소 응답: {}", canceledPayment);
        return true;
    }
}