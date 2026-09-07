package com.manish.lld.rate_limiter;

import lombok.Data;

import java.util.concurrent.locks.ReentrantLock;

@Data
public class TokenBucket {
    private final int capacity;
    private final int refillRate;
    private long lastRefillTime;
    private double tokens;

    private  final ReentrantLock lock = new ReentrantLock();

    public TokenBucket(int capacity, int refillRate , long lastRefillTime) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.lastRefillTime = lastRefillTime;
        this.tokens = capacity;
    }

    public boolean tryConsume(long currentTime){
        lock.lock();
        try{
            refill(currentTime);
            if(tokens >= 1){
                tokens -= 1;
                return true;
            }
            return false;
        }finally {
            lock.unlock();
        }
    }

    private void refill(long currentTime){
        long elapsedNano = currentTime - lastRefillTime;
        if(elapsedNano <=0){
            return;
        }

        double elapsedInSecond = elapsedNano / 1_000_000_000.0;

        double newTokens = elapsedInSecond * refillRate;

        tokens = Math.min(capacity , tokens + newTokens);

        lastRefillTime = currentTime;
    }
}
