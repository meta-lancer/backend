package com.metalancer.backend.service.repository;

import com.metalancer.backend.common.constants.PaymentType;
import java.math.BigDecimal;

public interface PortOneChargeRepository {

    BigDecimal getChargeRate(PaymentType paymentType);
}
