package com.metalancer.backend.user.service;

import com.metalancer.backend.user.dto.AuthRequestDTO;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    boolean emailLogin(HttpSession session, AuthRequestDTO.LoginRequest dto);

    boolean emailLogout(HttpSession session);
}