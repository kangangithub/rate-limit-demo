package com.example.ratelimitdemo.single;

import org.junit.Test;

/**
 * 限流算法: 令牌桶算法
 * 漏桶算法存在的问题: 漏桶的出水速度是恒定的，如果瞬时大流量的话，将有大部分请求被丢弃掉（也就是所谓的溢出）。
 * 令牌桶改进: 生成令牌的速度是恒定的，而请求去拿令牌是没有速度限制的
 */
public class TokenBucketDemo {
    /**
     * 时间戳
     */
    private static long timeStamp = System.currentTimeMillis();
    /**
     * 桶容量: 最多同时100个令牌(请求)
     */
    private static long capacity = 100L;
    /**
     * 令牌生成速度
     */
    private static long rate = 5L;
    /**
     * 桶内剩余令牌数
     */
    private static long remain = 0L;

    private boolean grant() {
        // 当前时间
        long now = System.currentTimeMillis();
        // 当前剩余令牌数
        remain = Math.min(capacity, remain + (now - timeStamp) * rate);
        // 重置时间戳为当前时间,为下一次请求做准备
        timeStamp = now;
        // 剩余令牌数>0
        if (remain > 0) {
            // 剩余令牌数-1
            --remain;
            return true;
        } else {
            return false;
        }
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
