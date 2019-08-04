package com.example.ratelimitdemo.single;

import org.junit.Test;

/**
 * 限流算法: 漏桶算法
 * 一个固定大小的桶，进水的速率不确定，出水的速率是恒定，水满会溢出。
 */
public class LeakyBucketDemo {
    /**
     * 时间戳
     */
    private static long timeStamp = System.currentTimeMillis();
    /**
     * 桶容量: 最多同时100个请求
     */
    private static long capacity = 100L;
    /**
     * 出水速度
     */
    private static long rate = 5L;
    /**
     * 桶内剩余水量
     */
    private static long remain = 0L;

    private boolean grant() {
        // 当前时间
        long now = System.currentTimeMillis();
        // 当前剩余水量
        remain = Math.max(0, capacity - (now - timeStamp) * rate);
        // 重置时间戳为当前时间,为下一次请求做准备
        timeStamp = now;
        // 剩余水量<桶容量
        if (remain < capacity) {
            // 剩余水量+1
            ++remain;
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
