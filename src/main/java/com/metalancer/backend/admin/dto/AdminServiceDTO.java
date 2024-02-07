package com.metalancer.backend.admin.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AdminServiceDTO {

    @Data
    @NoArgsConstructor
    public static class UpdateServiceCharge {

        private Long chargeId;
        private String chargeName;
        private BigDecimal chargeRate;
    }

    @Data
    @NoArgsConstructor
    public static class UpdatePortoneCharge {

        private Long chargeId;
        private String chargeName;
        private BigDecimal chargeRate;
    }
}
