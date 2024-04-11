package com.example.fastcampuspaymentsystemlive.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class WalletController {
    private final WalletService walletService;

    // TODO Exception 처리가 필요
    @PostMapping("/api/wallets")
    public CreateWalletResponse createWallet(@RequestBody CreateWalletRequest request) {
        return walletService.createWallet(request);
    }

    @GetMapping("/api/users/{userId}/wallets")
    public FindWalletResponse findWalletByUserId(
            @PathVariable("userId") Long userId) {
        return walletService.findWalletByUserId(userId);
    }
}
