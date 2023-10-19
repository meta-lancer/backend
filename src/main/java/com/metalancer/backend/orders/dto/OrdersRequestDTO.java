package com.metalancer.backend.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrdersRequestDTO {

    @Data
    @NoArgsConstructor
    public static class CreateOrder {
        @Schema(description = "총 금액", example = "5000")
        private int totalPrice;
        @Schema(description = "총 결제금액", example = "5000")
        private int totalPaymentPrice;
        //        @Schema(description = "총 포인트", example = "0")
//        private Integer totalPoint;
        @Schema(description = "결제할 에셋 고유번호 목록", example = "[1, 3, 4]")
        private List<Long> productsIdList;
        @Schema(description = "이름", example = "김철수")
        private String name;
    }

    @Data
    @NoArgsConstructor
    public static class PostPreparePayments {
        @Schema(description = "주문서 만들기로부터 받은 orderNo", example = "20235325346344")
        private String merchantUid;
        @Schema(description = "결제금액", example = "5000")
        private BigDecimal amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompleteOrder {
        @Schema(description = "포트원으로부터 받은 결제 고유번호", example = "imp_0221141")
        private String impUid;
        @Schema(description = "주문서 만들기로부터 받은 orderNo", example = "20235325346344")
        private String merchantUid;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    public static class CheckPayment extends CommonElements {

    }

    @Data
    @NoArgsConstructor
    public static class CompleteOrderWebhook {

        private String imp_uid;
        private String merchant_uid;
        private String status;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    public static class CancelAllPayment extends CommonElements {
        @Schema(description = "취소사유", example = "개인 변심")
        private String reason;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    public static class CancelPartialPayment extends CommonElements {
        private String reason;
        private Integer amount;
    }

    @Data
    @NoArgsConstructor
    public static class CommonElements {
        @Schema(description = "포트원으로부터 받은 결제 고유번호", example = "imp_0221141")
        private String impUid;
        @Schema(description = "주문서 만들기로부터 받은 orderNo", example = "20235325346344")
        private String merchantUid;
    }
}
