package com.example.fastcampuspaymentsystemlive;

import com.example.fastcampuspaymentsystemlive.transaction.PaymentTransactionRequest;
import com.example.fastcampuspaymentsystemlive.transaction.PaymentTransactionResponse;
import com.example.fastcampuspaymentsystemlive.transaction.TransactionService;
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
}
