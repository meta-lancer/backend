package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.OrderedProduct;
import com.metalancer.backend.admin.domain.UserCompletedOrder;
import com.metalancer.backend.admin.dto.AdminOrderDTO.AllRefund;
import com.metalancer.backend.admin.dto.AdminOrderDTO.PartialRefund;
import com.metalancer.backend.admin.dto.AdminOrderDTO.ProductsRefund;
import com.metalancer.backend.admin.entity.AdminRefundEntity;
import com.metalancer.backend.admin.entity.AdminRefundProductsEntity;
import com.metalancer.backend.admin.entity.AdminRefundProductsRepository;
import com.metalancer.backend.admin.entity.AdminRefundRepository;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.InvalidParamException;
import com.metalancer.backend.orders.entity.OrderPaymentEntity;
import com.metalancer.backend.orders.entity.OrderProductsEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import com.metalancer.backend.orders.repository.OrderPaymentRepository;
import com.metalancer.backend.orders.repository.OrderProductsRepository;
import com.metalancer.backend.orders.repository.OrdersRepository;
import com.metalancer.backend.users.entity.User;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final OrdersRepository ordersRepository;
    private final OrderProductsRepository orderProductsRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final AdminRefundRepository adminRefundRepository;
    private final AdminRefundProductsRepository adminRefundProductsRepository;

    // 변수들 세팅
    // 유효성 검사: 상품 가격 == 환불 금액, checksum
    // 환불 내역 - 환불 상품 save
    // 주문, 주문상품들 orderStatus 변경 및 checkSum


    @Override
    public Boolean refundAll(AllRefund dto, PrincipalDetails user)
        throws IamportResponseException, IOException {
        User admin = user.getUser();
        String merchantUid = dto.getOrderNo();
        String reason = dto.getReason();
        BigDecimal checksum = dto.getChecksum();
        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        CancelData cancelData = new CancelData(merchantUid, false);
        cancelData.setReason(reason);
        cancelData.setChecksum(checksum);
        // 포트원 캔슬 진행
        IamportResponse<Payment> canceledPayment = client.cancelPaymentByImpUid(cancelData);
        boolean cancelSuccess = canceledPayment.getCode() == 0;
        if (!cancelSuccess) {
            throw new BaseException(ErrorCode.FAIL_TO_REFUND_ALL);
        }
        Payment paymentResponse = canceledPayment.getResponse();
        // 성공하면 db에서 주문상태 처리
        OrdersEntity ordersEntity = ordersRepository.findEntityByOrderNo(merchantUid);
        List<OrderProductsEntity> orderProductsEntityList = orderProductsRepository.findAllByOrder(
            ordersEntity);
        orderProductsEntityList.forEach(OrderProductsEntity::refundOrderProduct);
        LocalDateTime refundedAt = paymentResponse.getCancelledAt().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        BigDecimal refundedPrice = paymentResponse.getCancelAmount();
//         전부 성공하면 환불처리 데이터
        AdminRefundEntity createdAdminRefundEntity = AdminRefundEntity.builder()
            .adminName(admin.getName())
            .orderNo(merchantUid)
            .impUid(paymentResponse.getImpUid())
            .ordersEntity(ordersEntity)
            .refundTotalPrice(refundedPrice)
            .currency(paymentResponse.getCurrency())
            .title(paymentResponse.getName())
            .type(paymentResponse.getPgProvider())
            .method(paymentResponse.getPayMethod())
            .refundedAt(refundedAt)
            .reason(reason)
            .build();
        adminRefundRepository.save(createdAdminRefundEntity);
        for (OrderProductsEntity orderProductsEntity : orderProductsEntityList) {
            AdminRefundProductsEntity createdAdminRefundProductsEntity = AdminRefundProductsEntity.builder()
                .adminRefundEntity(createdAdminRefundEntity)
                .orderProductsEntity(orderProductsEntity)
                .productsTitle(orderProductsEntity.getProductsEntity().getTitle())
                .refundPrice(orderProductsEntity.getPrice())
                .reason(reason)
                .build();
            adminRefundProductsRepository.save(createdAdminRefundProductsEntity);
        }
        ordersEntity.refundOrder();
        return true;
    }

    @Override
    public Boolean refundPartially(PartialRefund dto, PrincipalDetails user)
        throws IamportResponseException, IOException {
        User admin = user.getUser();
        String merchantUid = dto.getOrderNo();
        OrdersEntity ordersEntity = ordersRepository.findEntityByOrderNo(merchantUid);
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
        boolean cancelSuccess = canceledPayment.getCode() == 0;
        if (!cancelSuccess) {
            throw new BaseException(ErrorCode.FAIL_TO_REFUND_PARTIALLY);
        }
        log.info("부분 결제취소 응답: {}", canceledPayment);
        Payment paymentResponse = canceledPayment.getResponse();
        LocalDateTime refundedAt = paymentResponse.getCancelledAt().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        BigDecimal refundedPrice = paymentResponse.getCancelAmount();
//         전부 성공하면 환불처리 데이터
        AdminRefundEntity createdAdminRefundEntity = AdminRefundEntity.builder()
            .adminName(admin.getName())
            .orderNo(merchantUid)
            .impUid(paymentResponse.getImpUid())
            .ordersEntity(ordersEntity)
            .refundTotalPrice(refundedPrice)
            .currency(paymentResponse.getCurrency())
            .title(paymentResponse.getName())
            .type(paymentResponse.getPgProvider())
            .method(paymentResponse.getPayMethod())
            .refundedAt(refundedAt)
            .reason(reason)
            .build();
        adminRefundRepository.save(createdAdminRefundEntity);

        ordersEntity.decreaseCheckSum(refundedPrice);
        return true;
    }

    @Override
    public Page<UserCompletedOrder> getOrderList(Pageable pageable) {
        Page<OrderPaymentEntity> orderPaymentEntities = orderPaymentRepository.findAllByStatus(
            DataStatus.ACTIVE, pageable);
        Page<UserCompletedOrder> response = orderPaymentEntities.map(
            OrderPaymentEntity::toUserCompletedOrder);
        response.forEach(userCompletedOrder -> {
            OrdersEntity ordersEntity = ordersRepository.findEntityByOrderNo(
                userCompletedOrder.getOrderNo());
            List<OrderProductsEntity> orderProductsEntityList = orderProductsRepository.findAllByOrder(
                ordersEntity);
            List<OrderedProduct> orderedProductList = orderProductsEntityList.stream()
                .map(OrderProductsEntity::toOrderedProduct).toList();
            userCompletedOrder.setOrderedProductList(orderedProductList);
            userCompletedOrder.setBoughtCnt(orderProductsEntityList.size());
        });
        return response;
    }

    @Override
    public Boolean refundProduct(ProductsRefund dto, PrincipalDetails user)
        throws IamportResponseException, IOException {
        User admin = user.getUser();
        OrderProductsEntity orderProductsEntity = orderProductsRepository.findByOrderProductNo(
            dto.getOrderProductNo());
        OrdersEntity ordersEntity = orderProductsEntity.getOrdersEntity();
        String merchantUid = orderProductsEntity.getOrderNo();
        String reason = dto.getReason();
        BigDecimal amount = dto.getAmount();
        if (!orderProductsEntity.getPrice().equals(amount)) {
            throw new InvalidParamException(ErrorCode.DIFFERENT_PRICE);
        }
        BigDecimal checksum = dto.getChecksum();
        if (!orderProductsEntity.getPrice().equals(checksum)) {
            throw new InvalidParamException(ErrorCode.DIFFERENT_CHECKSUM);
        }
        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        CancelData cancelData = new CancelData(merchantUid, false, amount);
        cancelData.setReason(reason);
        cancelData.setChecksum(checksum);
        if (dto.getVatAmount() != null && dto.getVatAmount().compareTo(BigDecimal.ZERO) > 0) {
            // 부가세 같은거?
        }
        // 캔슬 진행
        IamportResponse<Payment> canceledPayment = client.cancelPaymentByImpUid(cancelData);
        boolean cancelSuccess = canceledPayment.getCode() == 0;
        if (!cancelSuccess) {
            throw new BaseException(ErrorCode.FAIL_TO_REFUND_PARTIALLY);
        }
        log.info("상품 환불의 결제취소 응답: {}", canceledPayment);
        Payment paymentResponse = canceledPayment.getResponse();
        LocalDateTime refundedAt = paymentResponse.getCancelledAt().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        BigDecimal refundedPrice = paymentResponse.getCancelAmount();
//       성공하면 환불처리 데이터
        AdminRefundEntity createdAdminRefundEntity = AdminRefundEntity.builder()
            .adminName(admin.getName())
            .orderNo(merchantUid)
            .impUid(paymentResponse.getImpUid())
            .ordersEntity(ordersEntity)
            .refundTotalPrice(refundedPrice)
            .currency(paymentResponse.getCurrency())
            .title(paymentResponse.getName())
            .type(paymentResponse.getPgProvider())
            .method(paymentResponse.getPayMethod())
            .refundedAt(refundedAt)
            .reason(reason)
            .build();
        adminRefundRepository.save(createdAdminRefundEntity);
        if (!refundedPrice.equals(orderProductsEntity.getPrice())) {
            throw new InvalidParamException(ErrorCode.DIFFERENT_PRICE);
        }
        AdminRefundProductsEntity createdAdminRefundProductsEntity = AdminRefundProductsEntity.builder()
            .adminRefundEntity(createdAdminRefundEntity)
            .orderProductsEntity(orderProductsEntity)
            .productsTitle(orderProductsEntity.getProductsEntity().getTitle())
            .refundPrice(refundedPrice)
            .reason(reason)
            .build();
        adminRefundProductsRepository.save(createdAdminRefundProductsEntity);

        // 상품 환불 처리 및 주문 환불액 처리
        orderProductsEntity.refundOrderProduct();
        ordersEntity.decreaseCheckSum(refundedPrice);

        return true;
    }

    @Override
    public Boolean refundProductPartial(ProductsRefund dto, PrincipalDetails user)
        throws IamportResponseException, IOException {
        User admin = user.getUser();
        OrderProductsEntity orderProductsEntity = orderProductsRepository.findByOrderProductNo(
            dto.getOrderProductNo());
        OrdersEntity ordersEntity = orderProductsEntity.getOrdersEntity();
        String merchantUid = orderProductsEntity.getOrderNo();
        String reason = dto.getReason();
        BigDecimal amount = dto.getAmount();
        BigDecimal checksum = dto.getChecksum();
        if (amount.compareTo(checksum) > 0) {
            throw new InvalidParamException(ErrorCode.DIFFERENT_CHECKSUM);
        }
        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        CancelData cancelData = new CancelData(merchantUid, false, amount);
        cancelData.setReason(reason);
        cancelData.setChecksum(checksum);
        if (dto.getVatAmount() != null && dto.getVatAmount().compareTo(BigDecimal.ZERO) > 0) {
            // 부가세 같은거?
        }
        // 캔슬 진행
        IamportResponse<Payment> canceledPayment = client.cancelPaymentByImpUid(cancelData);
        boolean cancelSuccess = canceledPayment.getCode() == 0;
        if (!cancelSuccess) {
            throw new BaseException(ErrorCode.FAIL_TO_REFUND_PARTIALLY);
        }
        log.info("상품 환불의 부분 결제취소 응답: {}", canceledPayment);
        Payment paymentResponse = canceledPayment.getResponse();
        LocalDateTime refundedAt = paymentResponse.getCancelledAt().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        BigDecimal refundedPrice = paymentResponse.getCancelAmount();
//       성공하면 환불처리 데이터
        AdminRefundEntity createdAdminRefundEntity = AdminRefundEntity.builder()
            .adminName(admin.getName())
            .orderNo(merchantUid)
            .impUid(paymentResponse.getImpUid())
            .ordersEntity(ordersEntity)
            .refundTotalPrice(paymentResponse.getCancelAmount())
            .currency(paymentResponse.getCurrency())
            .title(paymentResponse.getName())
            .type(paymentResponse.getPgProvider())
            .method(paymentResponse.getPayMethod())
            .refundedAt(refundedAt)
            .reason(reason)
            .build();
        adminRefundRepository.save(createdAdminRefundEntity);
        if (!refundedPrice.equals(amount)) {
            throw new InvalidParamException(ErrorCode.DIFFERENT_PRICE);
        }
        AdminRefundProductsEntity createdAdminRefundProductsEntity = AdminRefundProductsEntity.builder()
            .adminRefundEntity(createdAdminRefundEntity)
            .orderProductsEntity(orderProductsEntity)
            .productsTitle(orderProductsEntity.getProductsEntity().getTitle())
            .refundPrice(refundedPrice)
            .reason(reason)
            .build();
        adminRefundProductsRepository.save(createdAdminRefundProductsEntity);

        // 상품 환불 처리 및 주문 환불액 처리
        orderProductsEntity.refundOrderProductPartially(refundedPrice);
        ordersEntity.decreaseCheckSum(refundedPrice);

        return true;
    }


}