package com.metalancer.backend.users.service;

import com.metalancer.backend.users.dto.AuthRequestDTO;
import com.metalancer.backend.users.dto.AuthResponseDTO;
import com.metalancer.backend.users.dto.UserRequestDTO;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

public interface AuthService {

    Long createUser(UserRequestDTO.CreateRequest dto) throws MessagingException;

    AuthResponseDTO.userInfo approveUserByLink(String link);

    boolean emailLogin(HttpSession session, AuthRequestDTO.LoginRequest dto);

    boolean emailLogout(HttpSession session);
}