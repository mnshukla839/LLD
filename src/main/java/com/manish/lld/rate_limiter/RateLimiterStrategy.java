package com.manish.lld.rate_limiter;

public interface RateLimiterStrategy {
    boolean allowRequest(String clinetId);
}
