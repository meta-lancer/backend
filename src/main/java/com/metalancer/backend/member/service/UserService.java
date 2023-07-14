package com.metalancer.backend.member.service;

import com.metalancer.backend.member.dto.MemberRequestDTO;
import com.metalancer.backend.member.entity.User;
import jakarta.mail.MessagingException;

public interface UserService {

    Long createUser(MemberRequestDTO.CreateRequest dto) throws MessagingException;

    User approveUserByLink(String link);
}