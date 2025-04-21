package com.adk.oauth.service;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import com.adk.oauth.config.HubspotConfig;
import com.adk.oauth.dto.TokenDTO;

@Service
public class TokenService {
  private TokenDTO token;

  @Autowired
  private HubspotConfig config;

  public void saveToken(TokenDTO token) {
    token.setCreatedAt(Instant.now());
    //If you want to test the refresh token method quickly, uncomment the line below. It will force to change tokens every 2 minutes
    //token.setExpires_in(120);  
    this.token = token;
  }

  public TokenDTO getToken() {
    return this.token;
  }

  public String getAccessToken() {
    return token != null ? token.getAccess_token() : null;
  }

  public String getRefreshToken() {
    return token != null ? token.getRefresh_token() : null;
  }

  public boolean isAccessTokenExpired() {
    if (token == null || token.getCreatedAt() == null || token.getExpires_in() <= 0)
      return true;
    return Instant.now().isAfter(token.getCreatedAt().plusSeconds(token.getExpires_in()));
  }

  public String getAndVerifyAccessToken() {
    if (isAccessTokenExpired()) {
      refreshAccessToken();
    }
    String token = getAccessToken();
    if (token == null) {
      throw new IllegalStateException("Access token is unavailable.");
    }
    return token;
  }

  public TokenDTO exchangeCodeForToken(String code) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", config.getClientId());
    body.add("client_secret", config.getClientSecret());
    body.add("redirect_uri", config.getRedirectUri());
    body.add("code", code);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    TokenDTO token = new RestTemplate().postForObject(config.getTokenUrl(), request, TokenDTO.class);

    if (token != null) {
      System.out.println("Access Token: " + token.getAccess_token());
      System.out.println("Refresh Token: " + token.getRefresh_token());
      saveToken(token);
    }

    return token;
  }

  public void refreshAccessToken() {
    System.out.println("#################### Refreshing access token #########################");
    String refreshToken = getRefreshToken();
    if (refreshToken == null) {
      throw new IllegalStateException("Token not found, you need to log in.");
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "refresh_token");
    body.add("client_id", config.getClientId());
    body.add("client_secret", config.getClientSecret());
    body.add("refresh_token", refreshToken);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    TokenDTO newToken = new RestTemplate().postForObject(config.getTokenUrl(), request, TokenDTO.class);

    if (newToken != null) {
      System.out.println("New Access Token: " + newToken.getAccess_token());
      System.out.println("Refresh Token: " + newToken.getRefresh_token());
      saveToken(newToken);
    } else {
      throw new RuntimeException("Failed to renew access token");
    }
  }
}