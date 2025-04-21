package com.adk.oauth.util;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OAuthStateManager {
    private static final long STATE_TTL_MILLIS = 5 * 60 * 1000;
    private final Map<String, Instant> stateStore = new ConcurrentHashMap<>();

    public void storeState(String state) {
        stateStore.put(state, Instant.now());
    }

    public boolean isValidState(String state) {
        Instant created = stateStore.get(state);
        if (created == null) return false;

        boolean valid = Instant.now().isBefore(created.plusMillis(STATE_TTL_MILLIS));
        if (!valid) {
            stateStore.remove(state);
        }

        return valid;
    }

    public void removeState(String state) {
        stateStore.remove(state);
    }
}
