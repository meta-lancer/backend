package com.metalancer.backend.orders.service;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.common.constants.PaymentType;
import com.metalancer.backend.common.constants.ProductsType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.DataStatusException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.common.exception.OrderStatusException;
import com.metalancer.backend.creators.entity.ProductsSalesEntity;
import com.metalancer.backend.creators.repository.ProductsSalesRepository;
import com.metalancer.backend.orders.domain.CreatedOrder;
import com.metalancer.backend.orders.domain.OrderProducts;
import com.metalancer.backend.orders.domain.PaymentResponse;
import com.metalancer.backend.orders.dto.OrdersRequestDTO;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CancelAllPayment;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CompleteOrder;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CompleteOrderWebhook;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CreateOrder;
import com.metalancer.backend.orders.entity.OrderPaymentEntity;
import com.metalancer.backend.orders.entity.OrderProductsEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import com.metalancer.backend.orders.repository.OrderPaymentRepository;
import com.metalancer.backend.orders.repository.OrderProductsRepository;
import com.metalancer.backend.orders.repository.OrdersRepository;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.repository.ProductsAssetFileRepository;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.service.repository.PortOneChargeRepository;
import com.metalancer.backend.users.entity.PayedAssetsEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.CartRepository;
import com.metalancer.backend.users.repository.PayedAssetsRepository;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
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
    private final CartRepository cartRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final PayedAssetsRepository payedAssetsRepository;
    private final ProductsAssetFileRepository productsAssetFileRepository;
    private final ProductsSalesRepository productsSalesRepository;
    private final PortOneChargeRepository portOneChargeRepository;

    @Override
    public CreatedOrder createOrder(User user, CreateOrder dto) {
        user = userRepository.findById(user.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        // 포트원용 주문번호 만들기
        String orderNo = createOrderNo();
        // # 주문서 entity만들기
        OrdersEntity createdOrdersEntity = OrdersEntity.builder().orderer(user)
            .orderNo(orderNo)
            .totalPrice(dto.getTotalPrice())
            .totalPaymentPrice(dto.getTotalPaymentPrice())
//            .totalPoint(dto.getTotalPoint())
            .build();
        ordersRepository.save(createdOrdersEntity);

        // # 상품 고유번호를 가지고 가격 조회 + 주문상품 생성
        // 주문상품에 붙일 주문상품번호
        int index = 1;
        for (Long productsId : dto.getProductsIdList()) {
            // 상품 조회
            ProductsEntity foundProductEntity = productsRepository.findProductById(productsId);
            // 만약 상품의 할인가가 없다면 원래 금액으로
            Integer price =
                foundProductEntity.getSalePrice() == null ? foundProductEntity.getPrice()
                    : foundProductEntity.getSalePrice();
            OrderProductsEntity createdOrderProductsEntity = OrderProductsEntity.builder()
                .orderer(user)
                .ordersEntity(createdOrdersEntity).productsEntity(foundProductEntity)
                .orderNo(orderNo).orderProductNo(orderNo + String.format("%04d", index++))
                .price(BigDecimal.valueOf(price))
                .build();
            orderProductsRepository.save(createdOrderProductsEntity);
        }

        // response 만들기
        CreatedOrder response = createdOrdersEntity.toCreatedOrderDomain();
        List<OrderProductsEntity> orderProductsEntityList = orderProductsRepository.findAllByOrder(
            createdOrdersEntity);
        response.setOrderProductList(orderProductsEntityList);
        return response;
    }

    @Override
    public boolean checkPayment(OrdersRequestDTO.CheckPayment dto)
        throws IamportResponseException, IOException {
        // 결제내역 단건 조회
        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        // getPrepare는 클라에서만 사용 가능
        // 여기서 만약 조회가 안되고 있다면 주문 취소하도록??
        IamportResponse<Payment> payment_response = client.paymentByImpUid(dto.getImpUid());
        log.info("결제내역 단건 조회 응답 주문번호: {}", payment_response.getResponse().getMerchantUid());
        log.info("결제내역 단건 조회 응답 가격: {}", payment_response.getResponse().getAmount());
        log.info("결제내역 단건 조회 응답 상태: {}", payment_response.getResponse().getStatus());
        checkOrderStatusIsPaidFromAPI(payment_response.getResponse().getStatus());
        String orderNo = dto.getMerchantUid();
        // 주문서의 상태 확인
        OrdersEntity foundOrdersEntity = ordersRepository.findEntityByOrderNo(orderNo);
        DataStatus dataStatus = foundOrdersEntity.getStatus();
        checkOrdersEntityStatusIsNotDeleted(dataStatus);
        // 주문서의 주문 진행 상태 확인
        OrderStatus orderStatus = foundOrdersEntity.getOrderStatus();
        checkOrderStatusEqualsPAY_ING_OR_PAY_DONE(orderStatus);
        // 금액 비교
        BigDecimal createdOrderTotalPrice = foundOrdersEntity.getTotalPrice();
        BigDecimal paymentResponseTotalPrice = payment_response.getResponse().getAmount();
        return createdOrderTotalPrice.equals(paymentResponseTotalPrice);
    }

    private void checkOrdersEntityStatusIsNotDeleted(DataStatus dataStatus) {
        if (dataStatus.equals(DataStatus.DELETED)) {
            throw new OrderStatusException("order already deleted", ErrorCode.ILLEGAL_ORDER_STATUS);
        }
    }

    private void checkOrderStatusIsPaidFromAPI(String status) {
        if (!status.equals("paid")) {
            throw new OrderStatusException("orderStatus should be 'PAY_ING'",
                ErrorCode.ILLEGAL_ORDER_STATUS);
        }
    }

    private void checkOrderStatusEqualsPAY_ING_OR_PAY_DONE(OrderStatus orderStatus) {
        if (!orderStatus.equals(OrderStatus.PAY_ING) && !orderStatus.equals(OrderStatus.PAY_DONE)) {
            throw new OrderStatusException("orderStatus should be 'PAY_ING'",
                ErrorCode.ILLEGAL_ORDER_STATUS);
        }
    }

    @Override
    public PaymentResponse completePayment(User user, CompleteOrder dto)
        throws IamportResponseException, IOException {
        if (user == null) {
            String orderNo = dto.getMerchantUid();
            OrdersEntity foundOrdersEntity = ordersRepository.findEntityByOrderNo(orderNo);
            user = foundOrdersEntity.getOrderer();
        }
        user = userRepository.findById(user.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        String orderNo = dto.getMerchantUid();
        OrdersEntity foundOrdersEntity = ordersRepository.findEntityByOrderNo(orderNo);
        DataStatus dataStatus = foundOrdersEntity.getStatus();
        OrderStatus orderStatus = foundOrdersEntity.getOrderStatus();
        checkOrdersEntityStatusIsNotDeleted(dataStatus);
        checkOrderStatusEqualsPAY_ING_OR_PAY_DONE(orderStatus);

        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        IamportResponse<Payment> payment_response = client.paymentByImpUid(dto.getImpUid());
        Payment paymentResponse = payment_response.getResponse();
        List<OrderProductsEntity> orderProductsEntityList = orderProductsRepository.findAllByOrder(
            foundOrdersEntity);

        if (orderStatus.equals(OrderStatus.PAY_DONE)) {
            return getPaymentResponse(user,
                foundOrdersEntity, orderStatus, paymentResponse);
        }
        // 결제 처리 완료
        completeOrder(foundOrdersEntity, orderProductsEntityList);
        // 결제 완료 저장 => 일부 데이터는 결제 완료 받은 값이 필요
        OrderPaymentEntity savedOrderPaymentEntity = createOrderPaymentEntity(orderNo,
            foundOrdersEntity, paymentResponse);
        String paymentMethod = savedOrderPaymentEntity.getMethod();
        String paymentPgType = savedOrderPaymentEntity.getType();
        PaymentType paymentType = PaymentType.getType(paymentMethod, paymentPgType);
        // 장바구니에서 삭제
        for (OrderProductsEntity orderProductsEntity : orderProductsEntityList) {
            // 옵션...!
            cartRepository.deleteCart(user, orderProductsEntity.getProductsEntity(),
                orderProductsEntity.getProductsRequestOptionEntity());
            // 다운로드 허용
            if (ProductsType.NORMAL.equals(
                orderProductsEntity.getProductsEntity().getProductsType())) {
                ProductsEntity foundProductEntity = orderProductsEntity.getProductsEntity();
                String assetUrl = productsAssetFileRepository.findUrlByProduct(foundProductEntity);
                PayedAssetsEntity createdPayedAssetsEntity = PayedAssetsEntity.builder().user(user)
                    .orderProductsEntity(orderProductsEntity)
                    .products(foundProductEntity)
                    .orderPaymentEntity(savedOrderPaymentEntity)
                    .downloadLink(assetUrl).build();
                payedAssetsRepository.save(createdPayedAssetsEntity);
            }
            // 판매자 판매내역
            CurrencyType currencyType = CurrencyType.valueOf(savedOrderPaymentEntity.getCurrency());
            ProductsSalesEntity createdProductsSalesEntity = orderProductsEntity.toProductsSalesEntity();
            createdProductsSalesEntity.setCurrency(currencyType);
            createdProductsSalesEntity.setPaymentType(paymentType);
            BigDecimal chargeRate = portOneChargeRepository.getChargeRate(paymentType);
            createdProductsSalesEntity.setChargeRate(chargeRate);
            productsSalesRepository.save(createdProductsSalesEntity);
        }

        PaymentResponse response = getPaymentResponse(user,
            foundOrdersEntity, orderStatus, paymentResponse);
        log.info("결제처리 응답: {}", response);
        return response;
    }

    private OrderPaymentEntity createOrderPaymentEntity(String orderNo,
        OrdersEntity foundOrdersEntity,
        Payment paymentResponse) {
        OrderPaymentEntity createdOrderPaymentEntity = OrderPaymentEntity.builder()
            .ordersEntity(foundOrdersEntity).impUid(paymentResponse.getImpUid())
            .orderNo(orderNo).paymentPrice(foundOrdersEntity.getTotalPaymentPrice())
            .title(paymentResponse.getName())
            .receiptUrl(paymentResponse.getReceiptUrl())
            .type(paymentResponse.getPgProvider())
            .method(paymentResponse.getPayMethod()).currency(paymentResponse.getCurrency())
            .purchasedAt(paymentResponse.getPaidAt())
            .paidStatus(paymentResponse.getStatus())
            .pgTid(paymentResponse.getPgTid())
            .impUid(paymentResponse.getImpUid())
            .build();
        orderPaymentRepository.save(createdOrderPaymentEntity);
        return orderPaymentRepository.findByOrderNo(orderNo);
    }

    private PaymentResponse getPaymentResponse(User user, OrdersEntity foundOrdersEntity,
        OrderStatus orderStatus, Payment paymentResponse) {
        user = userRepository.findById(user.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        PaymentResponse response = PaymentResponse.builder().ordererId(user.getId())
            .ordererNm(user.getName())
            .ordererPhone(user.getMobile()).ordererEmail(user.getEmail())
            .orderNo(foundOrdersEntity.getOrderNo()).orderStatus(orderStatus)
            .totalPrice(
                foundOrdersEntity.getTotalPrice())
            .totalPayment(foundOrdersEntity.getTotalPaymentPrice())
            .totalPoint(foundOrdersEntity.getTotalPoint())
            .purchasedAt(paymentResponse.getPaidAt())
            .build();
        List<OrderProducts> orderProductsList = orderProductsRepository.findAllProductsByOrder(
            foundOrdersEntity);
        response.setOrderProductList(orderProductsList);
        log.info("결제처리 응답: {}", response);
        return response;
    }

    private void completeOrder(OrdersEntity ordersEntity,
        List<OrderProductsEntity> orderProductsEntityList) {
        ordersEntity.completeOrder();
        for (OrderProductsEntity orderProductsEntity : orderProductsEntityList) {
            orderProductsEntity.completeOrder();
        }
    }

    @Override
    public PaymentResponse completePaymentByWebhook(CompleteOrderWebhook dto)
        throws IamportResponseException, IOException {
        if (!dto.getImp_uid().equals(impUid)) {
            // 예외 처리
        }
        if (dto.getStatus().equals("paid")) {
            CompleteOrder completeOrder = new CompleteOrder(dto.getImp_uid(),
                dto.getMerchant_uid());
            // 가격이 맞는지, 고유번호가 맞는 지 등
            completePayment(null, completeOrder);
        }
        return null;
    }

    @Override
    public PaymentResponse cancelAllPayment(User user, CancelAllPayment dto)
        throws IamportResponseException, IOException {
        user = userRepository.findById(user.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        String orderNo = dto.getMerchantUid();
        String impUid = dto.getImpUid();
        OrdersEntity foundOrdersEntity = ordersRepository.findEntityByOrderNo(orderNo);
        portoneCancelPayments(impUid, dto.getMerchantUid(),
            foundOrdersEntity.getTotalPaymentPrice(),
            dto.getReason());
        cancelOrder(foundOrdersEntity);
        return null;
    }

    private IamportResponse<Payment> portoneCancelPayments(String impUid, String merchantUid,
        BigDecimal amount,
        String reason)
        throws IamportResponseException, IOException {
        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        IamportResponse<Payment> payment_response = client.paymentByImpUid(impUid);
        Payment paymentResponse = payment_response.getResponse();
        CancelData cancelData = new CancelData(merchantUid, true, amount);
        cancelData.setReason(reason);
        IamportResponse<Payment> canceledPayment = client.cancelPaymentByImpUid(cancelData);
        log.info("결제취소 응답: {}", canceledPayment);
        if (canceledPayment.getResponse().getStatus().equals("")) {

        }
        return canceledPayment;
    }

    void cancelOrder(OrdersEntity foundOrdersEntity) {
        foundOrdersEntity.deleteOrder();
        if (!foundOrdersEntity.getStatus().equals(DataStatus.DELETED)) {
            throw new DataStatusException("fail to delete order", ErrorCode.ILLEGAL_DATA_STATUS);
        }
    }

    private String createOrderNo() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String suffix = String.format("%02d", (int) ((Math.random() * (99))));
        return now.format(dtf) + suffix;
    }
}