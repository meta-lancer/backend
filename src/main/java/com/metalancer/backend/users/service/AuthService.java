package com.metalancer.backend.users.service;

import com.metalancer.backend.users.dto.AuthRequestDTO;
import jakarta.servlet.http.HttpSession;

public interface AuthService {

    boolean emailLogin(HttpSession session, AuthRequestDTO.LoginRequest dto);

    boolean emailLogout(HttpSession session);
}