# 高并发限流方案
## 单机限流:
### 1. 计数器: CounterDemo
### 2. 滑动窗口: RollingWindowDemo
### 3. 漏桶算法: LeakyBucketDemo
### 4. 令牌桶算法: TokenBucketDemo
### 5. Guava的RateLimiter: GuavaRateLimiterDemo
## 分布式限流:
### 1. Nginx + Lua
### 2. Redis + Lua