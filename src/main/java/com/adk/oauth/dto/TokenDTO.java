package com.adk.oauth.dto;

import java.time.Instant;

public class TokenDTO {
  private String access_token;
  private String refresh_token;
  private int expires_in;
  private Instant createdAt;

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  public String getRefresh_token() {
    return refresh_token;
  }

  public void setRefresh_token(String refresh_token) {
    this.refresh_token = refresh_token;
  }

  public int getExpires_in() {
    return expires_in;
  }

  public void setExpires_in(int expires_in) {
    this.expires_in = expires_in;
  }

  public boolean isAccessTokenExpired() {
    if (createdAt == null || expires_in <= 0) return true;
    return Instant.now().isAfter(createdAt.plusSeconds(expires_in));
  }
}
