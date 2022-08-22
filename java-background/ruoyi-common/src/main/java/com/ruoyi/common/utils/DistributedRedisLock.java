package com.ruoyi.common.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式Redis锁
 */
@Component
public class DistributedRedisLock {
    @Autowired
    private RedissonClient redissonClient;
    // 加锁
    public Boolean lock(String lockName) {
        if (redissonClient == null) {
            return false;
        }

        try {
            RLock lock = redissonClient.getLock(lockName);
            // 锁10秒后自动释放，防止死锁
            lock.lock(10, TimeUnit.SECONDS);
            // 加锁成功
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 释放锁
    public Boolean unlock(String lockName) {
        if (redissonClient == null) {
            return false;
        }

        try {
            RLock lock = redissonClient.getLock(lockName);
            lock.unlock();
            // 释放锁成功
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
