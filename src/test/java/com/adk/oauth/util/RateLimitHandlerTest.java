package com.adk.oauth.util;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RateLimitHandlerTest {

  private final RateLimitHandler rateLimitHandler = new RateLimitHandler();
  @Test
  void testRateLimitExceeded() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-RateLimit-Remaining", "0");
    headers.set("X-RateLimit-Reset", String.valueOf(System.currentTimeMillis() / 1000 + 10));

    assertDoesNotThrow(() -> rateLimitHandler.handleRateLimit(headers));
  }

  @Test
  void testRateLimitNotExceeded() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-RateLimit-Remaining", "10");
    headers.set("X-RateLimit-Reset", String.valueOf(System.currentTimeMillis() / 1000 + 10));

    assertDoesNotThrow(() -> rateLimitHandler.handleRateLimit(headers));
  }
}