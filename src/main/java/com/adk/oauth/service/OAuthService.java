package com.adk.oauth.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adk.oauth.config.HubspotConfig;
import com.adk.oauth.util.OAuthStateManager;

@Service
public class OAuthService {

  @Autowired
  private OAuthStateManager stateManager;

  @Autowired
  private HubspotConfig config;

  public OAuthService(HubspotConfig config) {
    this.config = config;
  }

  public String buildAuthorizationUrl() {
    String state = UUID.randomUUID().toString();
    stateManager.storeState(state);

    return config.getAuthUrl() +
        "?client_id=" + config.getClientId() +
        "&scope=" + config.getScope() +
        "&redirect_uri=" + config.getRedirectUri() +
        "&state=" + state;
  }
}