package com.metalancer.backend.admin.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AdminOrderDTO {

    @Data
    @NoArgsConstructor
    public static class AllRefund {

        private String orderNo;
        private String reason;
        private BigDecimal checksum;
    }

    @Data
    @NoArgsConstructor
    public static class PartialRefund {

        private String orderNo;
        private String reason;
        private BigDecimal checksum;
        private BigDecimal amount;
        private BigDecimal vatAmount;
    }

    @Data
    @NoArgsConstructor
    public static class ProductsRefund {

        private String orderProductNo;
        private String reason;
        private BigDecimal checksum;
        private BigDecimal amount;
        private BigDecimal vatAmount;
    }

}
