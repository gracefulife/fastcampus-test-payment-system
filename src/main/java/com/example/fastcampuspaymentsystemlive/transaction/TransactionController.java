package com.example.fastcampuspaymentsystemlive.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/api/balance/charge")
    public ChargeTransactionResponse charge(
            @RequestBody ChargeTransactionRequest request
    ) {
        return transactionService.charge(request);
    }

    @PostMapping("/api/balance/payment")
    public PaymentTransactionResponse payment(
            @RequestBody PaymentTransactionRequest request
    ) {
        return transactionService.payment(request);
    }
}
