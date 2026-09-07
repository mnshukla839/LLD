package com.manish.lld.rate_limiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TokenBucketRateLimiter implements RateLimiterStrategy{
    private final int capacity;
    private final int refillRate;
    private ConcurrentMap<String,TokenBucket> clients = new ConcurrentHashMap<>();

    public TokenBucketRateLimiter(int capacity, int refillRate) {
        if(capacity <=0){
            throw  new IllegalArgumentException("Invalid capacity");
        }
        if(refillRate <=0){
            throw new IllegalArgumentException("Invalid refill rate");
        }
        this.capacity = capacity;
        this.refillRate = refillRate;
    }


    @Override
    public boolean allowRequest(String clientId) {
        if(clientId == null || clientId.isBlank()){
            throw new IllegalArgumentException("Inavlid client Id request");
        }

        long now = System.nanoTime();
        TokenBucket bucket = clients.computeIfAbsent(clientId , id -> new TokenBucket(capacity,refillRate,now));
        return bucket.tryConsume(now);
    }
}
