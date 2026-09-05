package com.manish.lld.rate_limiter;

public class RateLimiter {
    private final RateLimiterStrategy strategy;

    public RateLimiter(RateLimiterStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean allowRequest(String clientId){
        return strategy.allowRequest(clientId);
    }
}
