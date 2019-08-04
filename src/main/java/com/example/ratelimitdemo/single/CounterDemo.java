package com.example.ratelimitdemo.single;

import org.junit.Test;

/**
 * 限流算法: 计数器
 *
 * 注意:
 * 存在一个时间临界点的问题。举个栗子，在12:01:00到12:01:58这段时间内没有用户请求，然后在12:01:59
 * 这一瞬时发出100个请求，OK，然后在12:02:00这一瞬时又发出了100个请求。在这个临界点可能会承受恶意
 * 用户的大量请求，甚至超出系统预期的承受。
 */
public class CounterDemo {
    /**
     * 时间戳
     */
    private static long timeStamp = System.currentTimeMillis();
    /**
     * 限流计数器: 单位时间最多100个请求
     */
    private static long limitCounter = 100L;
    /**
     * 单位时间1秒(1000毫秒)
     */
    private static long interval = 1000L;
    /**
     * 请求计数器数
     */
    private static long requestCounter = 0L;

    private boolean grant() {
        // 当前时间
        long now = System.currentTimeMillis();

        // 当期时间在1秒内
        if (now < timeStamp + interval) {
            // 请求计数器<限流计数器
            if (requestCounter < limitCounter) {
                // 限流计数器+1
                ++requestCounter;
                return true;
            }
            return false;
        } else {
            // 当期时间超出 时间戳+1秒
            // 重置时间戳
            timeStamp = System.currentTimeMillis();
            // 重置请求计数器
            requestCounter = 0;
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
