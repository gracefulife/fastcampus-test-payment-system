package com.example.fastcampuspaymentsystemlive.transaction;

import com.example.fastcampuspaymentsystemlive.WalletLockerService;
import com.example.fastcampuspaymentsystemlive.wallet.AddBalanceWalletRequest;
import com.example.fastcampuspaymentsystemlive.wallet.AddBalanceWalletResponse;
import com.example.fastcampuspaymentsystemlive.wallet.FindWalletResponse;
import com.example.fastcampuspaymentsystemlive.wallet.WalletService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class LockedTransactionService {
    private final WalletLockerService walletLockerService;
    private final WalletService walletService;
    private final TransactionService transactionService;

    @Transactional
    public ChargeTransactionResponse charge(ChargeTransactionRequest request) {
        final FindWalletResponse findWalletResponse = walletService
                .findWalletByUserId(request.userId());
        // 월렛에 락을 걸었음.
        WalletLockerService.Lock lock =
                walletLockerService.acquireLock(findWalletResponse.id());
        if (lock == null) {
            log.info("락 취득 실패");
            throw new IllegalStateException("cannot get lock");
        }

        try {
            log.info("락 취득 성공");
            return transactionService.charge(request);
        } finally {
            walletLockerService.releaseLock(lock);
        }
    }

}
