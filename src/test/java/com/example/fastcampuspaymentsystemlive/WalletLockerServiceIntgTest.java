package com.example.fastcampuspaymentsystemlive;

import com.example.fastcampuspaymentsystemlive.transaction.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class WalletLockerServiceIntgTest {
    @Autowired
    WalletLockerService walletLockerService;

    @Test
    public void test_acquire_lock() {
        WalletLockerService.Lock lock = walletLockerService.acquireLock(1L);
        System.out.println(lock);
        System.out.println(walletLockerService.acquireLock(1L));

        walletLockerService.releaseLock(lock);
        System.out.println(walletLockerService.acquireLock(1L));
    }
}
