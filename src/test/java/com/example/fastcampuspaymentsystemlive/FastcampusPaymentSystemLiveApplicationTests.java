package com.example.fastcampuspaymentsystemlive;

import com.example.fastcampuspaymentsystemlive.wallet.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FastcampusPaymentSystemLiveApplicationTests {
    @Autowired
    WalletRepository walletRepository;

    @Test
    void contextLoads() {
        System.out.println(walletRepository.findAll());
    }

}
