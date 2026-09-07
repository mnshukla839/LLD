package com.manish.lld.rate_limiter;

public class ClientWindow {
    long windowStart;
    int count;

    ClientWindow(long windowStart) {
        this.windowStart = windowStart;
        this.count = 0;
    }


}
