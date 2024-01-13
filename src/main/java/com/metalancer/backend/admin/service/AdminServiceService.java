package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.Charge;
import com.metalancer.backend.admin.dto.AdminServiceDTO.UpdatePortoneCharge;
import com.metalancer.backend.admin.dto.AdminServiceDTO.UpdateServiceCharge;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import java.util.List;

public interface AdminServiceService {

    List<Charge> getAdminServiceChargeList();

    List<Charge> getAdminPortoneChargeList();

    Boolean updateAdminServiceCharge(PrincipalDetails user, UpdateServiceCharge dto);

    Boolean updateAdminPortoneCharge(PrincipalDetails user, UpdatePortoneCharge dto);
}