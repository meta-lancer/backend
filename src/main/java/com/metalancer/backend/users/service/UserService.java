package com.metalancer.backend.users.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;

public interface UserService {

    boolean updateToCreator(PrincipalDetails user);
}