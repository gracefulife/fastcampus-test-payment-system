package com.example.fastcampuspaymentsystemlive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@AllArgsConstructor
public class WalletLockerService {

    private static final Long LOCK_TTL = 30000L;
    private static final String LOCK_PREFIX = "wallet-lock:";

    private final RedisTemplate<String, Lock> redisTemplate;

    public Lock acquireLock(Long walletId) {
        final Lock lock = new Lock(generateKey(walletId), "contents");

        Boolean result = redisTemplate.opsForValue().setIfAbsent(
                lock.getKey(),
                lock,
                Duration.ofMillis(LOCK_TTL)
        );
        if (result == Boolean.TRUE) {
            return lock;
        }

        return null;
    }

    public void releaseLock(Lock lock) {
        if (lock == null) {
            return;
        }

        redisTemplate.delete(lock.getKey());
    }

    private String generateKey(Long key) {
        return LOCK_PREFIX + key;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Lock {
        private String key;
        private String contents;
    }
}
