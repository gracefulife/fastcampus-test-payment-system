package com.example.fastcampuspaymentsystemlive;

import com.example.fastcampuspaymentsystemlive.wallet.CreateWalletRequest;
import com.example.fastcampuspaymentsystemlive.wallet.CreateWalletResponse;
import com.example.fastcampuspaymentsystemlive.wallet.WalletRepository;
import com.example.fastcampuspaymentsystemlive.wallet.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class WalletServiceIntgTest {
    @Autowired
    WalletService walletService;
    @Autowired
    WalletRepository walletRepository;

    @Test
    @Transactional
    public void 지갑을생성한다() {
        // given
        CreateWalletRequest request = new CreateWalletRequest(1001L);

        // when
        CreateWalletResponse response = walletService.createWallet(request);

        // then
        Assertions.assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void 동시에_여러건의_계좌가_생성된다면_잘_될까() throws InterruptedException {
        Long userId = 10L;
        CreateWalletRequest request = new CreateWalletRequest(userId);

        int numOfThread = 20;
        ExecutorService service = Executors.newFixedThreadPool(numOfThread);
        AtomicInteger completedTasks = new AtomicInteger(0);

        for (int i = 0; i < numOfThread; i++) {
            service.submit(() -> {
                try {
                    walletService.createWallet(request);
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
        System.out.println(walletRepository.findAllByUserId(userId));
    }
}
