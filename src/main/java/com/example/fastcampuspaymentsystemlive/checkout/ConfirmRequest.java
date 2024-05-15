package com.example.fastcampuspaymentsystemlive.checkout;

public record ConfirmRequest(String paymentKey, String orderId, String amount) {
}
