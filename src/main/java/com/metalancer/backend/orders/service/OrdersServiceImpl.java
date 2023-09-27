package com.metalancer.backend.orders.service;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.common.exception.StatusException;
import com.metalancer.backend.orders.domain.CreatedOrder;
import com.metalancer.backend.orders.domain.PaymentResponse;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CompleteOrder;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CompleteOrderWebhook;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CreateOrder;
import com.metalancer.backend.orders.entity.OrderProductsEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import com.metalancer.backend.orders.repository.OrderProductsRepository;
import com.metalancer.backend.orders.repository.OrdersRepository;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.UserRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    @Value("${iamport.api.key}")
    private String apiKey;
    @Value("${iamport.api.secret}")
    private String apiSecret;
    @Value("${iamport.imp_uid}")
    private String impUid;
    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;
    private final OrderProductsRepository orderProductsRepository;
    private final ProductsRepository productsRepository;

    @Override
    public CreatedOrder createOrder(User user, CreateOrder dto) {
        if (user == null) {
            user = userRepository.findById(1L).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND)
            );
        }
        String orderNo = createOrderNo();
        OrdersEntity createdOrdersEntity = OrdersEntity.builder().orderer(user)
            .orderNo(orderNo)
            .totalPrice(dto.getTotalPrice()).build();
        ordersRepository.save(createdOrdersEntity);
        int index = 1;
        for (Long productsId : dto.getProductsIdList()) {
            ProductsEntity foundProductEntity = productsRepository.findProductById(productsId);
            OrderProductsEntity createdOrderProductsEntity = OrderProductsEntity.builder()
                .orderer(user)
                .ordersEntity(createdOrdersEntity).productsEntity(foundProductEntity)
                .orderNo(orderNo).orderProductNo(orderNo + String.format("%04d", index++))
                .build();
            orderProductsRepository.save(createdOrderProductsEntity);
        }
        CreatedOrder response = createdOrdersEntity.toCreatedOrderDomain();
        List<OrderProductsEntity> orderProductsEntityList = orderProductsRepository.findAllByOrder(
            createdOrdersEntity);
        response.setOrderProductList(orderProductsEntityList);
        return response;
    }

    @Override
    public PaymentResponse completePayment(User user, CompleteOrder dto)
        throws IamportResponseException, IOException {
        // id를 통해 해당 주문서의 merchant_uid 대조해본다.
        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        String orderNo = dto.getMerchantUid();

        // 주문서의 상태 확인
        OrdersEntity foundOrdersEntity = ordersRepository.findEntityByOrderNo(orderNo);
        if (foundOrdersEntity.getStatus().equals(DataStatus.DELETED)) {

        }
        // 주문 진행 상태 확인
        OrderStatus orderStatus = foundOrdersEntity.getOrderStatus();
        if (!orderStatus.equals(OrderStatus.PAY_ING)) {
            if (orderStatus.equals(OrderStatus.PAY_DONE)) {
                // 이미 처리했으면 탈출 return response;
            } else {
                // 예외처리
            }
        }

        // 결제내역 단건 조회
        // getPrepare는 클라에서만 사용 가능
        IamportResponse<Payment> payment_response = client.paymentByImpUid(orderNo);
        // 여기서 만약 조회가 안되고 있다면 주문 취소하도록??
        log.info("결제내역 단건 조회 응답: {}", payment_response);

        // 금액 비교
        BigDecimal createdOrderTotalPrice = new BigDecimal(foundOrdersEntity.getTotalPrice());
        BigDecimal paymentResponseTotalPrice = payment_response.getResponse().getAmount();

        if (!createdOrderTotalPrice.equals(paymentResponseTotalPrice)) {
            // 결제 취소
            IamportResponse<Payment> canceledPayment = cancelPayments(orderNo,
                paymentResponseTotalPrice, client);
            log.info("결제취소 응답: {}", canceledPayment);
            // 주문서 취소
            cancelOrder(foundOrdersEntity);
        }

        // 결제 처리 완료
        completeOrder(foundOrdersEntity);

        // 장바구니에서 삭제

        // 다운로드 허용

        PaymentResponse response = PaymentResponse.builder().build();
        log.info("결제처리 응답: {}", response);
        return response;
    }

    private void completeOrder(OrdersEntity ordersEntity) {
        ordersEntity.completeOrder();
        if (!ordersEntity.getOrderStatus().equals(OrderStatus.PAY_DONE)) {
            throw new StatusException("fail to complete order", ErrorCode.ILLEGAL_STATUS);
        }
    }

    @Override
    public PaymentResponse completePaymentByWebhook(CompleteOrderWebhook dto)
        throws IamportResponseException, IOException {
        if (!dto.getImp_uid().equals(impUid)) {
            // 예외 처리
        }
        if (dto.getStatus().equals("paid")) {
            OrdersEntity foundOrdersEntity = ordersRepository.findEntityByOrderNo(
                dto.getMerchant_uid());
            CompleteOrder completeOrder = new CompleteOrder(foundOrdersEntity.getTotalPrice(),
                dto.getMerchant_uid());
            completePayment(null, completeOrder);
        }
        return null;
    }

    private void cancelOrder(OrdersEntity ordersEntity) {
        ordersEntity.deleteOrder();
        if (!ordersEntity.getStatus().equals(DataStatus.DELETED)) {
            throw new StatusException("fail to delete order", ErrorCode.ILLEGAL_STATUS);
        }
    }

    private IamportResponse<Payment> cancelPayments(String merchantUid, BigDecimal amount,
        IamportClient client)
        throws IamportResponseException, IOException {
        CancelData cancelData = new CancelData(merchantUid, true, amount);
        return client.cancelPaymentByImpUid(cancelData);
    }

    private String createOrderNo() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String suffix = String.format("%02d", (int) ((Math.random() * (99))));
        return now.format(dtf) + suffix;
    }
}