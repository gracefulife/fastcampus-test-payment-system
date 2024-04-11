package com.example.fastcampuspaymentsystemlive.transaction;

import java.math.BigDecimal;

public record PaymentTransactionRequest(
        Long walletId, String courseId, BigDecimal amount) {
}
