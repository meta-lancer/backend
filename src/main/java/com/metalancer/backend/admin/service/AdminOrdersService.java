package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.UserCompletedOrder;
import com.metalancer.backend.admin.dto.AdminOrderDTO.AllRefund;
import com.metalancer.backend.admin.dto.AdminOrderDTO.PartialRefund;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.siot.IamportRestClient.exception.IamportResponseException;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminOrdersService {

    Boolean refundAll(AllRefund dto, PrincipalDetails user)
        throws IamportResponseException, IOException;

    Boolean refundPartially(PartialRefund dto, PrincipalDetails user)
        throws IamportResponseException, IOException;

    Page<UserCompletedOrder> getOrderList(Pageable pageable);
}