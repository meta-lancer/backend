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
        private List<Long> productsId;
    }

    @Data
    @NoArgsConstructor
    public static class PostPreparePayments {

        private String merchantUid;
        private BigDecimal amount;
    }

}
