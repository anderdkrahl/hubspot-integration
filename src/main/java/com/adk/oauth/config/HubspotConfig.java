package com.adk.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "hubspot")
public class HubspotConfig {
  private String clientId;
  private String clientSecret;
  private String scope;
  private String redirectUri;
  private String tokenUrl;
  private String contactUrl;
  private String authUrl;

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getTokenUrl() {
    return tokenUrl;
  }

  public void setTokenUrl(String tokenUrl) {
    this.tokenUrl = tokenUrl;
  }

  public String getContactUrl() {
    return contactUrl;
  }

  public void setContactUrl(String contactUrl) {
    this.contactUrl = contactUrl;
  }

  public String getAuthUrl() {
    return authUrl;
  }

  public void setAuthUrl(String authUrl) {
    this.authUrl = authUrl;
  }
}