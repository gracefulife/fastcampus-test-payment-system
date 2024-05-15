package com.example.fastcampuspaymentsystemlive;

import com.example.fastcampuspaymentsystemlive.checkout.ConfirmRequest;
import com.example.fastcampuspaymentsystemlive.external.PaymentGatewayService;
import com.example.fastcampuspaymentsystemlive.transaction.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PaymentGatewayServiceIntgTest {
    @Autowired
    PaymentGatewayService paymentGatewayService;

    @Test
    public void test() {
        paymentGatewayService.confirm(
                new ConfirmRequest(
                        "tgen_202405152249399sgB1",
                        "34fed86b-94e7-4ff2-96b7-946eeee6fe23",
                        "1000"
                )
        );
    }
}
