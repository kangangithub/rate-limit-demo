package com.example.ratelimitdemo.single;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

/**
 * 限流算法: Guava的RateLimiter
 * Guava RateLimiter基于令牌桶算法，只需要告诉RateLimiter系统限制的QPS(每秒生产令牌的数量)是多少，
 * RateLimiter将以这个速度往桶里面放入令牌，然后请求的时候，通过tryAcquire()方法向RateLimiter获取许可(令牌)。
 */
public class GuavaRateLimiterDemo {
    /**
     * 创建速率是每秒100个许可的RateLimiter
     */
    private static RateLimiter rateLimiter = RateLimiter.create(100.0);

    private boolean grant() {
        //获取令牌桶中1个令牌
        return rateLimiter.tryAcquire();
    }

    @Test
    public void testGrant() {
        for (int i = 0; i < 500; i++) {
            new Thread(() -> {
                if (grant()) {
                    System.out.println(Thread.currentThread().getName() + ": 执行业务逻辑开始");
                    // 模拟业务耗时
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + ": 执行业务逻辑结束");
                } else {
                    System.out.println(Thread.currentThread().getName() + ": 限流");
                }
            }).start();
        }
    }
}
