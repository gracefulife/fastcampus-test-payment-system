package com.example.fastcampuspaymentsystemlive;

import com.example.fastcampuspaymentsystemlive.transaction.ChargeTransactionRequest;
import com.example.fastcampuspaymentsystemlive.transaction.ChargeTransactionResponse;
import com.example.fastcampuspaymentsystemlive.transaction.LockedTransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class LockedTransactionServiceIntgTest {
    @Autowired
    LockedTransactionService lockedTransactionService;

    @Test
    public void 충전을_동시에_실행한다() throws InterruptedException {
        Long userId = 2L;


        int numOfThread = 20;
        ExecutorService service = Executors.newFixedThreadPool(numOfThread);
        AtomicInteger completedTasks = new AtomicInteger(0);

        for (int i = 0; i < numOfThread; i++) {
            String orderId = UUID.randomUUID().toString();
            ChargeTransactionRequest chargeTransactionRequest = new ChargeTransactionRequest(
                    userId, orderId, BigDecimal.TEN
            );

            service.submit(() -> {
                try {
                    ChargeTransactionResponse response =
                            lockedTransactionService.charge(chargeTransactionRequest);
                    System.out.println(response);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    completedTasks.incrementAndGet();
                }
            });
        }

        service.shutdown();
        boolean finished = service.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println(finished);

    }
}
