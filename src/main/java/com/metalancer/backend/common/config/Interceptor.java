package com.metalancer.backend.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

@Slf4j
@Component
public class Interceptor extends WebRequestHandlerInterceptorAdapter {

    public Interceptor(
        WebRequestInterceptor requestInterceptor) {
        super(requestInterceptor);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        manageClientIP(request);
        String url = request.getRequestURI();
        String httpMethod = request.getMethod();
        String[] arr = {"/", "/error", "/.env", "/Core/Skin/Login.aspx", "/favicon.ico", "/"};
        if (url.equals("/") || url.equals("/error") || url.equals("/.env")) {
            return false;
        }
        log.info("url : {}", httpMethod + "-" + url);
        return super.preHandle(request, response, handler);
    }

    private static void manageClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress.equals("127.0.0.1")) {
            return;
        }
        log.info("Client IP: " + ipAddress);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        log.info("response status: {}", response.getStatus());
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}