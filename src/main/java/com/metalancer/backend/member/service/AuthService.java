package com.metalancer.backend.member.service;

import com.metalancer.backend.member.dto.AuthRequestDTO;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    boolean emailLogin(HttpSession session, AuthRequestDTO.LoginRequest dto);
}