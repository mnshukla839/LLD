package com.manish.lld.rate_limiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FixedWindowRateLimiter implements RateLimiterStrategy{
    private final int maxRequest;
    private final int windowsMillis;

    private ConcurrentMap<String , ClientWindow> clients = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(int maxRequest, int windowsMillis) {
        if (maxRequest <= 0) {
            throw new IllegalArgumentException("maxRequests must be > 0");
        }

        if (windowsMillis <= 0) {
            throw new IllegalArgumentException("windowMillis must be > 0");
        }
        this.maxRequest = maxRequest;
        this.windowsMillis = windowsMillis;
    }

    @Override
    public boolean allowRequest(String clientId) {

        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("Invalid clientId");
        }

        long now = System.currentTimeMillis();

        ClientWindow window = clients.computeIfAbsent(
                clientId,
                id -> new ClientWindow(now)
        );

        synchronized (window) {

            if (now - window.windowStart >= windowsMillis) {

                window.windowStart = now;
                window.count = 1;

                return true;
            }

            if (window.count < maxRequest) {

                window.count++;

                return true;
            }

            return false;
        }
    }

}
