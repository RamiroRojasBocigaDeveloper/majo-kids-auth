package com.chancla.chancla_lite_auth.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Control simple de intentos fallidos de login en memoria.
 * Bloquea una combinacion IP+email tras MAX_ATTEMPTS fallos durante BLOCK_DURATION.
 */
@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_DURATION_SECONDS = 900;

    private final Map<String, AttemptEntry> attempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String key) {
        if (key == null) {
            return false;
        }
        AttemptEntry entry = attempts.get(key);
        if (entry == null) {
            return false;
        }
        if (entry.failures >= MAX_ATTEMPTS) {
            if (Instant.now().isBefore(entry.blockedUntil)) {
                return true;
            }
            attempts.remove(key);
        }
        return false;
    }

    public void loginFailed(String key) {
        if (key == null) {
            return;
        }
        AttemptEntry entry = attempts.computeIfAbsent(key, k -> new AttemptEntry());
        entry.failures++;
        if (entry.failures >= MAX_ATTEMPTS) {
            entry.blockedUntil = Instant.now().plusSeconds(BLOCK_DURATION_SECONDS);
        }
    }

    public void loginSucceeded(String key) {
        if (key != null) {
            attempts.remove(key);
        }
    }

    private static class AttemptEntry {
        int failures;
        Instant blockedUntil;
    }
}
