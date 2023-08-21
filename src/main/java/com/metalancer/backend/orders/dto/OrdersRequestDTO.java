package com.metalancer.backend.orders.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OrdersRequestDTO {

    @Data
    @NoArgsConstructor
    public static class PostPreparePayments {

        private String merchantUid;
        private BigDecimal amount;
    }

}
