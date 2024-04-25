package com.example.fastcampuspaymentsystemlive;

import com.example.fastcampuspaymentsystemlive.transaction.*;
import com.example.fastcampuspaymentsystemlive.wallet.CreateWalletRequest;
import com.example.fastcampuspaymentsystemlive.wallet.CreateWalletResponse;
import com.example.fastcampuspaymentsystemlive.wallet.WalletService;
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
public class TransactionServiceIntgTest {
    @Autowired
    TransactionService transactionService;

    @Test
    @Transactional
    public void 결제를생성한다() {
        // given
        PaymentTransactionRequest request = new PaymentTransactionRequest(
                1L, "course-1", new BigDecimal(10)
        );

        // when
        PaymentTransactionResponse response = transactionService.payment(request);

        // then
        Assertions.assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void 충전진행() throws InterruptedException {
        Long userId = 2L;
        String orderId = "orderId-1";

        // given
        ChargeTransactionRequest chargeTransactionRequest = new ChargeTransactionRequest(
                userId, orderId, BigDecimal.TEN
        );

        // when
        ChargeTransactionResponse response = transactionService.charge(chargeTransactionRequest);

        // then
        System.out.println(response);
    }

    @Test
    public void 충전을_동시에_실행한다() throws InterruptedException {
        Long userId = 2L;
        String orderId = "orderId-2";
        ChargeTransactionRequest chargeTransactionRequest = new ChargeTransactionRequest(
                userId, orderId, BigDecimal.TEN
        );

        int numOfThread = 20;
        ExecutorService service = Executors.newFixedThreadPool(numOfThread);
        AtomicInteger completedTasks = new AtomicInteger(0);

        for (int i = 0; i < numOfThread; i++) {
            service.submit(() -> {
                try {
                    ChargeTransactionResponse response = transactionService.charge(chargeTransactionRequest);
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
