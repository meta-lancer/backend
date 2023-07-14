package com.metalancer.backend.member.service;

import com.metalancer.backend.member.dto.MemberRequestDTO;
import com.metalancer.backend.member.entity.User;

public interface UserService {

    Long createUser(MemberRequestDTO.CreateRequest dto);

    User approveUserByLink(String link);
}