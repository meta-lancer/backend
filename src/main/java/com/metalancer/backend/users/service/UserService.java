package com.metalancer.backend.users.service;

import com.metalancer.backend.users.dto.UserRequestDTO;
import com.metalancer.backend.users.entity.User;
import jakarta.mail.MessagingException;

public interface UserService {

    Long createUser(UserRequestDTO.CreateRequest dto) throws MessagingException;

    User approveUserByLink(String link);
}