package com.metalancer.backend.common.config.security;

import static java.util.Objects.isNull;

import com.metalancer.backend.common.constants.ObjectText;
import com.metalancer.backend.users.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class UserAuthenticationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute(ObjectText.LOGIN_USER);
        if (!isNull(user)) {
//            SecurityContextHolder.getContext().setAuthentication(user.makeAuthentication());
        }

        filterChain.doFilter(request, response);
    }
}