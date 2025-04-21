package com.adk.oauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.adk.oauth.service.OAuthService;
import com.adk.oauth.service.TokenService;
import com.adk.oauth.util.OAuthStateManager;

@RestController
public class OAuthController {
  @Autowired
  private OAuthService hubspotService;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private OAuthStateManager stateManager;

  @GetMapping("/")
  public String home() {
    return "Welcome! You are logged in.";
  }

  @GetMapping("/authorize")
  public RedirectView authorize() {
    return new RedirectView(hubspotService.buildAuthorizationUrl());
  }

  @GetMapping("/oauth-callback")
  public RedirectView handleCallback(
      @RequestParam("code") String code,
      @RequestParam("state") String state) {

    if (!stateManager.isValidState(state) || code == null) {
      return new RedirectView("/unauthorized");
    }

    tokenService.exchangeCodeForToken(code);

    return new RedirectView("/");
  }
}