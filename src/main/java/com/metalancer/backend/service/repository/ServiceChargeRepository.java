package com.metalancer.backend.service.repository;

import com.metalancer.backend.common.constants.ServiceChargesType;
import java.math.BigDecimal;

public interface ServiceChargeRepository {

    BigDecimal getChargeRate(ServiceChargesType serviceChargesType);
}
