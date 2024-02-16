package com.metalancer.backend.orders.service;

import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.common.constants.PaymentType;
import com.metalancer.backend.common.constants.ProductsType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.DataStatusException;
import com.metalancer.backend.common.exception.InvalidParamException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.common.exception.OrderStatusException;
import com.metalancer.backend.creators.entity.ProductsSalesEntity;
import com.metalancer.backend.creators.repository.ProductsSalesRepository;
import com.metalancer.backend.external.exchange.ExchangeRatesEntity;
import com.metalancer.backend.external.exchange.ExchangeRatesJpaRepository;
import com.metalancer.backend.orders.domain.CreatedOrder;
import com.metalancer.backend.orders.domain.OrderProducts;
import com.metalancer.backend.orders.domain.PaymentResponse;
import com.metalancer.backend.orders.dto.OrdersRequestDTO;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CancelAllPayment;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CompleteOrder;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CompleteOrderWebhook;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CreateFreeOrder;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CreateOrder;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.RequestOption;
import com.metalancer.backend.orders.entity.OrderPaymentEntity;
import com.metalancer.backend.orders.entity.OrderProductsEntity;
import com.metalancer.backend.orders.entity.OrderRequestProductsEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import com.metalancer.backend.orders.repository.OrderPaymentRepository;
import com.metalancer.backend.orders.repository.OrderProductsRepository;
import com.metalancer.backend.orders.repository.OrderRequestProductsRepository;
import com.metalancer.backend.orders.repository.OrdersRepository;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsRequestOptionEntity;
import com.metalancer.backend.products.repository.ProductsAssetFileRepository;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.products.repository.ProductsRequestOptionRepository;
import com.metalancer.backend.service.repository.PortOneChargeRepository;
import com.metalancer.backend.users.entity.CreatorEntity;
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
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    private final ProductsRequestOptionRepository productsRequestOptionRepository;
    private final OrderRequestProductsRepository orderRequestProductsRepository;
    private final ExchangeRatesJpaRepository exchangeRatesJpaRepository;

    @Override
    public CreatedOrder createOrder(User user, CreateOrder dto) {
        user = userRepository.findById(user.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        // 포트원용 주문번호 만들기
        String orderNo = createOrderNo();
        OrdersEntity createdOrdersEntity = getCreatedOrdersEntity(user, dto.getTotalPrice(),
            dto.getTotalPaymentPrice(),
            orderNo);
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
            // # 제작요청
            // 만약 productsId가 같은 requsetOption이 있다면!
            boolean anyProductsIdMatch = dto.getOptionList() != null && dto.getOptionList().stream()
                .anyMatch(option -> productsId.equals(option.getProductsId()));
            if (anyProductsIdMatch) {
                List<RequestOption> requestOptions = dto.getOptionList().stream()
                    .filter(option -> productsId.equals(option.getProductsId())).toList();
                // 같은 상품이지만 옵션이 다른 게 있을 수도 있다!
                for (RequestOption requestOption : requestOptions) {
                    ProductsRequestOptionEntity productsRequestOptionEntity = productsRequestOptionRepository.findOptionByProductsAndId(
                        foundProductEntity, requestOption.getRequestOptionId()).orElseThrow(
                        () -> new NotFoundException("옵션", ErrorCode.NOT_FOUND)
                    );
                    price += productsRequestOptionEntity.getPrice();
                    createOrderProductsEntityWithRequestOption(user, orderNo, createdOrdersEntity,
                        index,
                        foundProductEntity, BigDecimal.valueOf(price),
                        productsRequestOptionEntity);
                    index++;
                }
            } else {
                createOrderProductsEntity(user, orderNo, createdOrdersEntity, index,
                    foundProductEntity, BigDecimal.valueOf(price));
                index++;
            }
        }

        // response 만들기
        CreatedOrder response = createdOrdersEntity.toCreatedOrderDomain();
        List<OrderProductsEntity> orderProductsEntityList = orderProductsRepository.findAllByOrder(
            createdOrdersEntity);
        response.setOrderProductList(orderProductsEntityList);
        return response;
    }

    private void createOrderProductsEntity(User user, String orderNo,
        OrdersEntity createdOrdersEntity, int index,
        ProductsEntity foundProductEntity, BigDecimal price) {
        OrderProductsEntity createdOrderProductsEntity = OrderProductsEntity.builder()
            .orderer(user)
            .ordersEntity(createdOrdersEntity).productsEntity(foundProductEntity)
            .orderNo(orderNo).orderProductNo(orderNo + String.format("%04d", index))
            .price(price)
            .build();
        orderProductsRepository.save(createdOrderProductsEntity);
    }

    private OrderProductsEntity getCreateOrderProductsEntity(User user, String orderNo,
        OrdersEntity createdOrdersEntity, int index,
        ProductsEntity foundProductEntity, BigDecimal price) {
        OrderProductsEntity createdOrderProductsEntity = OrderProductsEntity.builder()
            .orderer(user)
            .ordersEntity(createdOrdersEntity).productsEntity(foundProductEntity)
            .orderNo(orderNo).orderProductNo(orderNo + String.format("%04d", index))
            .price(price)
            .build();
        orderProductsRepository.save(createdOrderProductsEntity);
        return createdOrderProductsEntity;
    }

    private void createOrderProductsEntityWithRequestOption(User user, String orderNo,
        OrdersEntity createdOrdersEntity, int index,
        ProductsEntity foundProductEntity, BigDecimal price,
        ProductsRequestOptionEntity productsRequestOptionEntity) {
        OrderProductsEntity createdOrderProductsEntity = OrderProductsEntity.builder()
            .orderer(user)
            .ordersEntity(createdOrdersEntity).productsEntity(foundProductEntity)
            .productsRequestOptionEntity(productsRequestOptionEntity)
            .orderNo(orderNo).orderProductNo(orderNo + String.format("%04d", index))
            .price(price)
            .build();
        orderProductsRepository.save(createdOrderProductsEntity);
    }

    private OrdersEntity getCreatedOrdersEntity(User user, BigDecimal totalPrice,
        BigDecimal totalPaymentPrice, String orderNo) {
        OrdersEntity createdOrdersEntity = OrdersEntity.builder().orderer(user)
            .orderNo(orderNo)
            .totalPrice(totalPrice)
            .totalPaymentPrice(totalPaymentPrice)
//            .totalPoint(dto.getTotalPoint())
            .build();
        ordersRepository.save(createdOrdersEntity);
        return createdOrdersEntity;
    }

    @Override
    public CreatedOrder createOrderByEn(User user, CreateOrder dto) {
        user = userRepository.findById(user.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        // 포트원용 주문번호 만들기
        String orderNo = createOrderNo();
        OrdersEntity createdOrdersEntity = getCreatedOrdersEntity(user,
            dto.getTotalPrice(), dto.getTotalPaymentPrice(), orderNo);

        // # 상품 고유번호를 가지고 가격 조회 + 주문상품 생성
        // 주문상품에 붙일 주문상품번호
        int index = 1;
        for (Long productsId : dto.getProductsIdList()) {
            // 상품 조회
            ProductsEntity foundProductEntity = productsRepository.findProductById(productsId);
            // 만약 상품의 할인가가 없다면 원래 금액으로
            // # 미화 주문인 경우....!
            ExchangeRatesEntity exchangeRatesEntity = exchangeRatesJpaRepository.getFirstByOrderByCreatedAtDesc()
                .orElseThrow(
                    () -> new NotFoundException("환율 정보", ErrorCode.NOT_FOUND)
                );
            BigDecimal exchangeRateAmount = exchangeRatesEntity.getAmount();

            // # 제작요청
            // 만약 productsId가 같은 requsetOption이 있다면!
            boolean anyProductsIdMatch = dto.getOptionList() != null && dto.getOptionList().stream()
                .anyMatch(option -> productsId.equals(option.getProductsId()));
            if (anyProductsIdMatch) {
                List<RequestOption> requestOptions = dto.getOptionList().stream()
                    .filter(option -> productsId.equals(option.getProductsId())).toList();
                // 같은 상품이지만 옵션이 다른 게 있을 수도 있다!
                for (RequestOption requestOption : requestOptions) {
                    ProductsRequestOptionEntity productsRequestOptionEntity = productsRequestOptionRepository.findOptionByProductsAndId(
                        foundProductEntity, requestOption.getRequestOptionId()).orElseThrow(
                        () -> new NotFoundException("옵션", ErrorCode.NOT_FOUND)
                    );
                    BigDecimal price =
                        foundProductEntity.getSalePrice() == null ?
                            convertToEnPriceWithExchangeRate(
                                BigDecimal.valueOf(foundProductEntity.getPrice()
                                    + productsRequestOptionEntity.getPrice()),
                                exchangeRateAmount)
                            : convertToEnPriceWithExchangeRate(
                                BigDecimal.valueOf(foundProductEntity.getSalePrice()
                                    + productsRequestOptionEntity.getPrice()),
                                exchangeRateAmount);
                    createOrderProductsEntityWithRequestOption(user, orderNo, createdOrdersEntity,
                        index,
                        foundProductEntity,
                        price, productsRequestOptionEntity);
                    index++;
                }
            } else {
                BigDecimal price =
                    foundProductEntity.getSalePrice() == null ?
                        convertToEnPriceWithExchangeRate(
                            BigDecimal.valueOf(foundProductEntity.getPrice()),
                            exchangeRateAmount)
                        : convertToEnPriceWithExchangeRate(
                            BigDecimal.valueOf(foundProductEntity.getSalePrice()),
                            exchangeRateAmount);
                createOrderProductsEntity(user, orderNo, createdOrdersEntity, index,
                    foundProductEntity, price);
                index++;
            }
        }

        // response 만들기
        CreatedOrder response = createdOrdersEntity.toCreatedOrderDomain();
        List<OrderProductsEntity> orderProductsEntityList = orderProductsRepository.findAllByOrder(
            createdOrdersEntity);
        response.setOrderProductList(orderProductsEntityList);
        return response;
    }

    @Override
    public PaymentResponse createFreeOrder(User user, CreateFreeOrder dto) {
        user = userRepository.findById(user.getId()).orElseThrow(
            () -> new NotFoundException("유저: ", ErrorCode.NOT_FOUND)
        );
        // 상품 조회
        ProductsEntity foundProductEntity = productsRepository.findProductById(dto.getProductsId());
        if (foundProductEntity.getPrice() != 0) {
            throw new InvalidParamException("product is not free", ErrorCode.INVALID_PARAMETER);
        }

        Optional<PayedAssetsEntity> freePayedAssetEntityOptional = payedAssetsRepository.findByUserAndProductsAndStatus(
            user, foundProductEntity, DataStatus.ACTIVE);

        if (freePayedAssetEntityOptional.isPresent()) {
            PayedAssetsEntity payedAssetsEntity = freePayedAssetEntityOptional.get();
            OrdersEntity ordersEntity = payedAssetsEntity.getOrderProductsEntity()
                .getOrdersEntity();
            OrderPaymentEntity orderPaymentEntity = payedAssetsEntity.getOrderPaymentEntity();
            Date purchasedAtDate = convertLocalDateTimeToDate(orderPaymentEntity.getPurchasedAt());
            PaymentResponse response = getPaymentResponse(user,
                ordersEntity, ordersEntity.getOrderStatus(),
                purchasedAtDate);
            log.info("무료 결제처리 응답: {}", response);
            return response;
        }

        // 포트원용 주문번호 만들기
        String orderNo = createOrderNo();
        OrdersEntity createdOrdersEntity = getCreatedOrdersEntity(user, BigDecimal.ZERO,
            BigDecimal.ZERO,
            orderNo);
        // # 상품 고유번호를 가지고 가격 조회 + 주문상품 생성
        // 주문상품에 붙일 주문상품번호
        int index = 1;
        OrderProductsEntity createdOrderProductsEntity = getCreateOrderProductsEntity(user, orderNo,
            createdOrdersEntity, index,
            foundProductEntity, BigDecimal.ZERO);
        // 결제 처리 완료
        // # 일반과 제작요청 구분
        createdOrdersEntity.completeOrder();
        createdOrderProductsEntity.completeOrder();
        Date purchasedAt = new Date();
        // 결제 완료 저장 => 일부 데이터는 결제 완료 받은 값이 필요
        OrderPaymentEntity savedOrderPaymentEntity = createFreeOrderPaymentEntity(orderNo,
            createdOrdersEntity,
            "(Free) " + foundProductEntity.getTitle(), purchasedAt);
        String paymentMethod = savedOrderPaymentEntity.getMethod();
        String paymentPgType = savedOrderPaymentEntity.getType();
        PaymentType paymentType = PaymentType.getType(paymentMethod, paymentPgType);

        String assetUrl = productsAssetFileRepository.findUrlByProduct(foundProductEntity);
        PayedAssetsEntity createdPayedAssetsEntity = PayedAssetsEntity.builder().user(user)
            .orderProductsEntity(createdOrderProductsEntity)
            .products(foundProductEntity)
            .orderPaymentEntity(savedOrderPaymentEntity)
            .downloadLink(assetUrl).build();
        payedAssetsRepository.save(createdPayedAssetsEntity);

        // 판매자 판매내역
        CurrencyType currencyType = CurrencyType.valueOf(
            savedOrderPaymentEntity.getCurrency());
        ProductsSalesEntity createdProductsSalesEntity = createdOrderProductsEntity.toProductsSalesEntity();
        createdProductsSalesEntity.setCurrency(currencyType);
        createdProductsSalesEntity.setPaymentType(paymentType);
        createdProductsSalesEntity.setChargeRate(BigDecimal.ZERO);
        productsSalesRepository.save(createdProductsSalesEntity);

        // response 만들기

        PaymentResponse response = getPaymentResponse(user,
            createdOrdersEntity, createdOrdersEntity.getOrderStatus(), purchasedAt);
        log.info("무료 결제처리 응답: {}", response);
        return response;
    }

    private Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }


    private BigDecimal convertToEnPriceWithExchangeRate(BigDecimal value, BigDecimal exchangeRate) {
        return value.divide(exchangeRate, 2, RoundingMode.HALF_UP);
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
        checkOrderStatusEqualsPAY_ING_OR_PAY_CONFIRM(orderStatus);
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

    private void checkOrderStatusEqualsPAY_ING_OR_PAY_CONFIRM(OrderStatus orderStatus) {
        if (!orderStatus.equals(OrderStatus.PAY_ING) && !orderStatus.equals(
            OrderStatus.PAY_CONFIRM)) {
            throw new OrderStatusException("orderStatus should be 'PAY_ING or PAY_CONFIRM'",
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
        // # 일반과 제작요청 혼용
        // # 일반, 제작요청 모두 PAY_ING 혹은 PAY_CONFIRM
        checkOrderStatusEqualsPAY_ING_OR_PAY_CONFIRM(orderStatus);

        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        IamportResponse<Payment> payment_response = client.paymentByImpUid(dto.getImpUid());
        Payment paymentResponse = payment_response.getResponse();
        List<OrderProductsEntity> orderProductsEntityList = orderProductsRepository.findAllByOrder(
            foundOrdersEntity);

        if (orderStatus.equals(OrderStatus.PAY_CONFIRM)) {
            return getPaymentResponse(user,
                foundOrdersEntity, orderStatus, paymentResponse.getPaidAt());
        }

        // 결제 처리 완료
        // # 일반과 제작요청 구분
        completeOrder(foundOrdersEntity, orderProductsEntityList);
        // 결제 완료 저장 => 일부 데이터는 결제 완료 받은 값이 필요
        OrderPaymentEntity savedOrderPaymentEntity = createOrderPaymentEntity(orderNo,
            foundOrdersEntity, paymentResponse);
        String paymentMethod = savedOrderPaymentEntity.getMethod();
        String paymentPgType = savedOrderPaymentEntity.getType();
        PaymentType paymentType = PaymentType.getType(paymentMethod, paymentPgType);
        // 장바구니에서 삭제
        for (OrderProductsEntity orderProductsEntity : orderProductsEntityList) {
            // # 일반과 제작요청 혼용
            // 옵션...!
            cartRepository.deleteCart(user, orderProductsEntity.getProductsEntity(),
                orderProductsEntity.getProductsRequestOptionEntity());
            // # 일반과 제작요청 구분
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

                // 판매자 판매내역
                CurrencyType currencyType = CurrencyType.valueOf(
                    savedOrderPaymentEntity.getCurrency());
                ProductsSalesEntity createdProductsSalesEntity = orderProductsEntity.toProductsSalesEntity();
                createdProductsSalesEntity.setCurrency(currencyType);
                createdProductsSalesEntity.setPaymentType(paymentType);
                BigDecimal chargeRate = portOneChargeRepository.getChargeRate(paymentType);
                createdProductsSalesEntity.setChargeRate(chargeRate);
                productsSalesRepository.save(createdProductsSalesEntity);
            } else {
                ProductsEntity productsEntity = orderProductsEntity.getProductsEntity();
                CreatorEntity seller = productsEntity.getCreatorEntity();
                CurrencyType currencyType = CurrencyType.valueOf(
                    savedOrderPaymentEntity.getCurrency());
                LocalDateTime purchasedAt = savedOrderPaymentEntity.getPurchasedAt();
                OrderRequestProductsEntity orderRequestProductsEntity = OrderRequestProductsEntity
                    .builder()
                    .seller(seller)
                    .buyer(user)
                    .ordersEntity(orderProductsEntity.getOrdersEntity())
                    .orderProductsEntity(orderProductsEntity)
                    .productsEntity(orderProductsEntity.getProductsEntity())
                    .productsRequestOptionEntity(
                        orderProductsEntity.getProductsRequestOptionEntity())
                    .currency(currencyType)
                    .purchasedAt(purchasedAt)
                    .price(orderProductsEntity.getPrice())
                    .build();
                orderRequestProductsRepository.save(orderRequestProductsEntity);
            }
        }

        PaymentResponse response = getPaymentResponse(user,
            foundOrdersEntity, orderStatus, paymentResponse.getPaidAt());
        log.info("결제처리 응답: {}", response);
        return response;
    }

    private OrderPaymentEntity createOrderPaymentEntity(String orderNo,
        OrdersEntity foundOrdersEntity,
        Payment paymentResponse) {
        OrderPaymentEntity createdOrderPaymentEntity = OrderPaymentEntity.builder()
            .ordersEntity(foundOrdersEntity)
            .impUid(paymentResponse.getImpUid())
            .orderNo(orderNo).paymentPrice(foundOrdersEntity.getTotalPaymentPrice())
            .title(paymentResponse.getName())
            .receiptUrl(paymentResponse.getReceiptUrl())
            .type(paymentResponse.getPgProvider())
            .method(paymentResponse.getPayMethod()).currency(paymentResponse.getCurrency())
            .purchasedAt(paymentResponse.getPaidAt())
            .paidStatus(paymentResponse.getStatus())
            .pgTid(paymentResponse.getPgTid())
            .build();
        orderPaymentRepository.save(createdOrderPaymentEntity);
        return orderPaymentRepository.findByOrderNo(orderNo);
    }

    private OrderPaymentEntity createFreeOrderPaymentEntity(String orderNo,
        OrdersEntity foundOrdersEntity, String title, Date purchasedAt) {
        OrderPaymentEntity createdOrderPaymentEntity = OrderPaymentEntity.builder()
            .ordersEntity(foundOrdersEntity)
            .impUid("")
            .orderNo(orderNo)
            .paymentPrice(foundOrdersEntity.getTotalPaymentPrice())
            .title(title)
            .receiptUrl("")
            .type("free")
            .method("free")
            .currency("KRW")
            .purchasedAt(purchasedAt)
            .paidStatus("paid")
            .pgTid("")
            .build();
        orderPaymentRepository.save(createdOrderPaymentEntity);
        return orderPaymentRepository.findByOrderNo(orderNo);
    }

    private PaymentResponse getPaymentResponse(User user, OrdersEntity foundOrdersEntity,
        OrderStatus orderStatus, Date paidAt) {
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
            .purchasedAt(paidAt)
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
            if (ProductsType.NORMAL.equals(
                orderProductsEntity.getProductsEntity().getProductsType())) {
                orderProductsEntity.completeOrder();
            } else {
                orderProductsEntity.completeRequestOrder();
            }
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
    public boolean cancelAllPayment(CancelAllPayment dto)
        throws IamportResponseException, IOException {
        String orderNo = dto.getMerchantUid();
        String impUid = dto.getImpUid();
        OrdersEntity foundOrdersEntity = ordersRepository.findEntityByOrderNo(orderNo);
        portoneCancelPayments(impUid, dto.getMerchantUid(),
            foundOrdersEntity.getTotalPaymentPrice(),
            dto.getReason());
        cancelOrder(foundOrdersEntity);
        return true;
    }

    private IamportResponse<Payment> portoneCancelPayments(String impUid, String merchantUid,
        BigDecimal amount,
        String reason)
        throws IamportResponseException, IOException {
        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        // 굳이...?
//        IamportResponse<Payment> payment_response = client.paymentByImpUid(impUid);
//        Payment paymentResponse = payment_response.getResponse();
        CancelData cancelData = new CancelData(merchantUid, false, amount);
        cancelData.setReason(reason);
        // 캔슬 진행
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