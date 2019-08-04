package com.example.ratelimitdemo.single;

import org.junit.Test;

import java.util.LinkedList;

/**
 * 限流算法: 滑动窗口
 * <p>
 * 滑动窗口是把固定时间片，进行划分，并且随着时间的流逝，进行移动，避开了计数器的临界点问题。
 * 也就是说这些固定数量的可以移动的格子，将会进行计数判断阀值，因此格子的数量影响着滑动窗口算法的精度。
 */
public class RollingWindowDemo {
    /**
     * 时间戳
     */
    private static long timeStamp = System.currentTimeMillis();
    /**
     * 单位时间1/10秒(100毫秒)
     * 1秒100个请求均分成10个*(100毫秒10个请求)
     */
    private static long interval = 100L;
    /**
     * 窗口容量: 最多同时10个请求
     * 1秒100个请求均分成10个*(100毫秒10个请求)
     */
    private static long capacity = 10L;
    /**
     * 窗口对象: 存储请求进入时间
     */
    private static LinkedList<Long> windows = new LinkedList<>();

    private boolean grant() {
        // 当前时间
        long now = System.currentTimeMillis();
        // 窗口中第一个请求的进入时间
        Long firstTime = windows.peekFirst();
        if (firstTime == null) {
            firstTime = timeStamp;
        }
        // 当前时间和第一请求时间差<100毫秒
        if (now - firstTime < interval) {
            // 当前窗口容量<窗口最大容量
            if (windows.size() < capacity) {
                // 请求进入窗口,存储进入时间
                windows.addLast(System.currentTimeMillis());
                return true;
            } else {
                return false;
            }
        } else {
            // 当前时间和第一请求时间差>=100毫秒,重置窗口
            windows = new LinkedList<>();
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
