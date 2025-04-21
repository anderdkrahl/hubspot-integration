package com.adk.oauth.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.adk.oauth.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
  @Autowired
  private TokenService tokenService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    try {
      String token = tokenService.getAndVerifyAccessToken();
      if (token == null) {
        System.out.println("SEM TOKEN!");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Token inválido ou não autenticado.");
        return false;
      }

    } catch (Exception e) {
      response.sendRedirect("/unauthorized");
    }
    return true;
  }
}
