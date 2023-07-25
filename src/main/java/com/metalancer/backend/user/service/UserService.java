package com.metalancer.backend.user.service;

import com.metalancer.backend.user.dto.UserRequestDTO;
import com.metalancer.backend.user.entity.User;
import jakarta.mail.MessagingException;

public interface UserService {

    Long createUser(UserRequestDTO.CreateRequest dto) throws MessagingException;

    User approveUserByLink(String link);
}