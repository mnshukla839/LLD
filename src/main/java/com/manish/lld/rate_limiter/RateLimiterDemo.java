package com.manish.lld.rate_limiter;

public class RateLimiterDemo {
    public static void main(String[] args) throws InterruptedException {
//        RateLimiterStrategy fixedWindowRateLimiter =
//                new FixedWindowRateLimiter(5, 1000);
        RateLimiterStrategy tokenBucket = new TokenBucketRateLimiter(5 , 2);
        RateLimiter rateLimiter = new RateLimiter(tokenBucket);

        Runnable task1 = () -> {
            for (int i = 1; i <= 10; i++) {
                boolean allowed =
                        rateLimiter.allowRequest("user-123");
                System.out.println(
                        "Request from thread 1 " + i + " -> " + allowed
                );
            }
        };

        Runnable task2 = () -> {
            for (int i = 1; i <= 10; i++) {
                boolean allowed =
                        rateLimiter.allowRequest("user-123");
                System.out.println(
                        "Request from thread 2 " + i + " -> " + allowed
                );
            }
        };

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
