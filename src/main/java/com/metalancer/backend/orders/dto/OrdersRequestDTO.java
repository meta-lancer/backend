package com.metalancer.backend.orders.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OrdersRequestDTO {

    @Data
    @NoArgsConstructor
    public static class CreateOrder {

        private int totalPrice;
        private int totalPaymentPrice;
        private Integer totalPoint;
        private List<Long> productsIdList;
    }

    @Data
    @NoArgsConstructor
    public static class PostPreparePayments {

        private String merchantUid;
        private BigDecimal amount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompleteOrder {

        private String impUid;
        private String merchantUid;
    }

    @Data
    @NoArgsConstructor
    public static class CheckPayment {

        private String impUid;
        private String merchantUid;
    }

    @Data
    @NoArgsConstructor
    public static class CompleteOrderWebhook {

        private String imp_uid;
        private String merchant_uid;
        private String status;
    }

    @Data
    @NoArgsConstructor
    public static class CancelAllPayment {

        private String impUid;
        private String merchantUid;
        private String reason;
    }

    @Data
    @NoArgsConstructor
    public static class CancelPartialPayment {

        private String impUid;
        private String merchantUid;
        private String reason;
        private Integer amount;
    }
}
