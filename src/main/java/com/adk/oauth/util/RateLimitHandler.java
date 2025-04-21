package com.adk.oauth.util;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class RateLimitHandler {
    public void handleRateLimit(HttpHeaders headers) {
        if (headers.containsKey("X-RateLimit-Remaining") && headers.containsKey("X-RateLimit-Reset")) {
            int remaining = Integer.parseInt(headers.getFirst("X-RateLimit-Remaining"));
            long reset = Long.parseLong(headers.getFirst("X-RateLimit-Reset"));
            long now = System.currentTimeMillis() / 1000;

            if (remaining <= 0 && reset > now) {
                long wait = reset - now;
                System.out.println("Rate limit reached. Waiting " + wait + "seconds...");
                try {
                    Thread.sleep(wait * 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
