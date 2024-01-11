package com.metalancer.backend.admin.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;

public interface AdminService {

    Boolean checkIfAdmin(PrincipalDetails user);
}