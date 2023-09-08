package com.metalancer.backend.orders.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.orders.domain.CreatedOrder;
import com.metalancer.backend.orders.dto.OrdersRequestDTO;
import com.metalancer.backend.orders.service.OrdersService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Prepare;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "주문", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrdersController {

    @Value("${iamport.api.key}")
    private String apiKey;
    @Value("${iamport.api.secret}")
    private String apiSecret;
    private final OrdersService ordersService;

    // 토큰 발행
    // 주문서 생성 => 도중에 사전 등록
    // 주문 결제 완료 처리 => Request로 프론트로부터 수신, 결제내역 단건 조회, 가격 비교 + 웹훅

//    프론트를 통해 받는 결제완료 처리는 고객에게 보여지는 주문페이지로 이동 등으로 활용하실 수 있겠고,
//    내부 DB저장 및 주문처리는 웹훅을 통해서 진행하시는 방법도 존재합니다:)
//    호출은 콜백과 웹훅이 동시에 병렬적으로 진행되게 됩니다.
//    만약 순서를 보장하고자 하실 경우 아래와 같이 웹훅을 우선적으로 호출할 수 있도록 설정해드릴 수 있습니다.
//        (https://guide.portone.io/74c9f8b4-e181-47bb-bfdb-7ca20a6e2466)

    @PostMapping("/order")
    public BaseResponse<CreatedOrder> createOrder(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody OrdersRequestDTO.CreateOrder dto) {
        log.info("주문서 만들기: {}", dto);
        return new BaseResponse<>(ordersService.createOrder(user.getUser(), dto));
    }


    @GetMapping("/token")
    public BaseResponse<String> getPortOneAccessToken() throws Exception {
        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        IamportResponse<AccessToken> auth_response = client.getAuth();
        return new BaseResponse<>(auth_response.getResponse().getToken());
    }

    //    @GetMapping("/payments/{orders-id}")
//    public BaseResponse<Boolean> getPayment(@PathVariable("orders-id") Long ordersId)
//        throws Exception {
//
//        // id를 통해 해당 주문서의 merchant_uid 불러온다.
//        IamportClient client = new IamportClient(apiKey, apiSecret, true);
//        String merchantUid = "";
//        IamportResponse<Payment> payment_response = client.paymentByImpUid(merchantUid);
//        log.info("결제내역 단건 조회 응답: {}", payment_response);
//        return new BaseResponse<>(true);
//    }

    @PostMapping("/prepare")
    public BaseResponse<Boolean> postPreparePayments(
        @RequestBody OrdersRequestDTO.PostPreparePayments dto)
        throws Exception {
        log.info("결제정보 사전 등록 요청: {}", dto);
        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        PrepareData prepareData = new PrepareData(dto.getMerchantUid(), dto.getAmount());
        IamportResponse<Prepare> prepare_response = client.postPrepare(prepareData);
        log.info("결제정보 사전 등록 응답: {}", prepare_response);

        return new BaseResponse<>(true);
    }

    // 프론트에서 결제완료되면 포트원 결제번호 + 우리의 orderNo 을 수신하는 api 이 안에 결제내역 단건 조회 + 금액 비교까지 하면 된다!

    // 결제 사후 검증 => 웹훅과 동시에 이루어지도록...?
//    @GetMapping("/payments/{orders-id}")
//    public BaseResponse<Boolean> getPayment(@PathVariable("orders-id") Long ordersId)
//        throws Exception {
//
//        // 포트원 결제고유번호(imp_uid), 가맹점 주문번호(merchant_uid) 수신
//
//        // id를 통해 해당 주문서의 merchant_uid 대조해본다.
//
//
//        IamportClient client = new IamportClient(apiKey, apiSecret, true);
//        String merchantUid = "";
//
//        // 결제내역 단건 조회
//        IamportResponse<Payment> payment_response = client.paymentByImpUid(merchantUid);
//        log.info("결제내역 단건 조회 응답: {}", payment_response);
//
//        // 금액 비교
//        client.paymentBalanceByImpUid()
//
//        return new BaseResponse<>(true);
//    }

//    @PostMapping("/cancel")
//    public BaseResponse<Boolean> cancelPayments(
//        @RequestBody OrdersRequestDTO.PostPreparePayments dto)
//        throws Exception {
//        log.info("결제정보 사전 등록 요청: {}", dto);
//        IamportClient client = new IamportClient(apiKey, apiSecret, true);
//        CancelData cancelData = new CancelData();
//        IamportResponse<Payment> payment_response = client.cancelPaymentByImpUid(cancelData);
//        log.info("결제취소 응답: {}", payment_response);
//        return new BaseResponse<>(true);
//    }


    private String getRandomMerchantUid() {
        DateFormat df = new SimpleDateFormat("$$hhmmssSS");
        int n = (int) (Math.random() * 100) + 1;

        return df.format(new Date()) + "_" + n;
    }

}
