package com.example.fastcampuspaymentsystemlive.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
