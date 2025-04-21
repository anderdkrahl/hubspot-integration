package com.adk.oauth.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.adk.oauth.config.HubspotConfig;

import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class WebhookSecurityUtil {

    @Autowired
    private HubspotConfig config;

    private static final long MAX_TIMESTAMP_DIFFERENCE = 300000;

    public boolean validate(HttpServletRequest request, String body, String signature, String timestamp) {
        try {
            long now = System.currentTimeMillis();
            long ts = Long.parseLong(timestamp);

            if (Math.abs(now - ts) > MAX_TIMESTAMP_DIFFERENCE) 
                return false;

            String method = request.getMethod();
            String uri = request.getRequestURI();
            String host = request.getHeader("Host");

            String baseString = method + "https://" + host + uri + body + timestamp;

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(config.getClientSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(baseString.getBytes(StandardCharsets.UTF_8));
            String expectedSignature = Base64.getEncoder().encodeToString(hash);

            return expectedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
}
