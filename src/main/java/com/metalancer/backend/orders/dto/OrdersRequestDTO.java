package com.metalancer.backend.orders.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OrdersRequestDTO {

    @Data
    @NoArgsConstructor
    public static class CreateOrder {

        private int totalPrice;
        private List<Long> productsIdList;
    }

    @Data
    @NoArgsConstructor
    public static class PostPreparePayments {

        private String merchantUid;
        private BigDecimal amount;
    }

    @Data
    @NoArgsConstructor
    public static class CompleteOrder {

        private int totalPrice;
        private String merchantUid;

    }

    @Data
    @NoArgsConstructor
    public static class CompleteOrderWebhook {

        private String imp_uid;
        private String merchant_uid;
        private String status;
    }
}
