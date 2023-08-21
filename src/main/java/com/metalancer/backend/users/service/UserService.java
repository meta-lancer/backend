package com.metalancer.backend.users.service;

import com.metalancer.backend.users.dto.AuthResponseDTO;
import com.metalancer.backend.users.dto.UserRequestDTO;
import jakarta.mail.MessagingException;

public interface UserService {

    Long createUser(UserRequestDTO.CreateRequest dto) throws MessagingException;

    AuthResponseDTO.userInfo approveUserByLink(String link);
}