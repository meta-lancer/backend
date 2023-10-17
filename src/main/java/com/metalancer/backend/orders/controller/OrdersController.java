package com.metalancer.backend.orders.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.orders.domain.CreatedOrder;
import com.metalancer.backend.orders.domain.PaymentResponse;
import com.metalancer.backend.orders.dto.OrdersRequestDTO;
import com.metalancer.backend.orders.dto.OrdersRequestDTO.CancelAllPayment;
import com.metalancer.backend.orders.repository.OrdersRepository;
import com.metalancer.backend.orders.service.OrdersService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Prepare;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private final OrdersRepository ordersRepository;

    // 토큰 발행
    // 주문서 생성 => 도중에 사전 등록
    // 주문 결제 완료 처리 => Request로 프론트로부터 수신, 결제내역 단건 조회, 가격 비교 + 웹훅

//    프론트를 통해 받는 결제완료 처리는 고객에게 보여지는 주문페이지로 이동 등으로 활용하실 수 있겠고,
//    내부 DB저장 및 주문처리는 웹훅을 통해서 진행하시는 방법도 존재합니다:)
//    호출은 콜백과 웹훅이 동시에 병렬적으로 진행되게 됩니다.
//    만약 순서를 보장하고자 하실 경우 아래와 같이 웹훅을 우선적으로 호출할 수 있도록 설정해드릴 수 있습니다.
//        (https://guide.portone.io/74c9f8b4-e181-47bb-bfdb-7ca20a6e2466)

    /// 구매하기를 누르면 장바구니 담기와 같이! 대신 response가 결제하기 페이지를 위한 게 있어야겠지???


    @Operation(summary = "주문서 만들기", description = "결제하기를 누르면 결제완료 이전의 주문서가 만들어집니다. \n\n " +
        "여기서 결제모듈에 사용될 orderNo가 생성됩니다. \n\n " +
        "로그인 후, Session값이 제대로 설정될 때까지 유저1로 진행됩니다.")
    @PostMapping
    public BaseResponse<CreatedOrder> createOrder(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody OrdersRequestDTO.CreateOrder dto) {
        log.info("주문서 만들기: {}", dto);
        return new BaseResponse<>(
            ordersService.createOrder(user.getUser(), dto));
    }


    @Operation(summary = "포트원 결제에 필요한 토큰 발행", description = "프론트엔드에서 포트원 api 호출 시 필요할 수도 있다.")
    @GetMapping("/token")
    public BaseResponse<String> getPortOneAccessToken() throws Exception {
        IamportClient client = new IamportClient(apiKey, apiSecret, true);
        IamportResponse<AccessToken> auth_response = client.getAuth();
        return new BaseResponse<>(auth_response.getResponse().getToken());
    }

    @Operation(summary = "결제정보 사전 등록 요청", description = "가맹점 고유번호와 결제금액만으로 미리 등록")
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

    @Operation(summary = "결제내역 단건 조회", description = "")
    @PostMapping("/payments/validation")
    public BaseResponse<Boolean> checkPayment(@RequestBody OrdersRequestDTO.CheckPayment dto)
        throws IamportResponseException, IOException {
        return new BaseResponse<>(ordersService.checkPayment(dto));
    }


    @Operation(summary = "결제 완료 처리", description = "")
    @PostMapping("/payments")
    public BaseResponse<PaymentResponse> completePayment(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody OrdersRequestDTO.CompleteOrder dto
    ) throws Exception {
        log.info("결제 처리 완료 요청 객체: {}", dto);
        return new BaseResponse<PaymentResponse>(
            ordersService.completePayment(user.getUser(), dto));
    }

    @Operation(summary = "결제 완료 처리 웹훅(포트원으로부터)", description = "")
    @PostMapping("/payments/webhook")
    public BaseResponse<PaymentResponse> completePaymentByWebhook(
        @RequestBody OrdersRequestDTO.CompleteOrderWebhook dto
    ) throws Exception {
        log.info("결제 처리 완료 웹 훅 객체: {}", dto);
        return new BaseResponse<PaymentResponse>(
            ordersService.completePaymentByWebhook(dto));
    }

    @Operation(summary = "결제 전체 취소 요청", description = "")
    @PatchMapping("/payments/cancellation/all")
    public BaseResponse<PaymentResponse> cancelAllPayment(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestBody CancelAllPayment dto
    ) throws Exception {
        log.info("결제 전체 취소 요청 객체: {}", dto);
        return new BaseResponse<PaymentResponse>(
            ordersService.cancelAllPayment(user.getUser(), dto));
    }

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
