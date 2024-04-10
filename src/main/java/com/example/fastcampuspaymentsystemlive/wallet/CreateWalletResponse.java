package com.example.fastcampuspaymentsystemlive.wallet;

import java.math.BigDecimal;

public record CreateWalletResponse(
        Long id, Long userId, BigDecimal balance
) {
}
