package com.example.fastcampuspaymentsystemlive.transaction;

import com.example.fastcampuspaymentsystemlive.wallet.AddBalanceWalletRequest;
import com.example.fastcampuspaymentsystemlive.wallet.AddBalanceWalletResponse;
import com.example.fastcampuspaymentsystemlive.wallet.FindWalletResponse;
import com.example.fastcampuspaymentsystemlive.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final WalletService walletService;
    private final TransactionRepository transactionRepository;

    @Transactional
    public ChargeTransactionResponse charge(ChargeTransactionRequest request) {
        final FindWalletResponse findWalletResponse = walletService
                .findWalletByUserId(request.userId());
        if (findWalletResponse == null) {
            throw new RuntimeException("사용자 지갑이 존재하지 않습니다.");
        }

        if (transactionRepository.findTransactionByOrderId(request.orderId()).isPresent()) {
            throw new RuntimeException("이미 충전된 거래입니다.");
        }

        final AddBalanceWalletResponse wallet = walletService.addBalance(
                new AddBalanceWalletRequest(findWalletResponse.id(), request.amount()));
        final Transaction transaction = Transaction.createChargeTransaction(
                request.userId(), wallet.id(),
                request.orderId(), request.amount()
        );
        transactionRepository.save(transaction);
        return new ChargeTransactionResponse(wallet.id(), wallet.balance());
    }

    @Transactional
    public PaymentTransactionResponse payment(PaymentTransactionRequest request) {
        // FIXME
        if (transactionRepository.findTransactionByOrderId(request.courseId()).isPresent()) {
            throw new RuntimeException("이미 결제 된 강좌입니다.");
        }

        final FindWalletResponse findWalletResponse = walletService
                .findWalletByWalletId(request.walletId());
        final AddBalanceWalletResponse wallet = walletService.addBalance(
                new AddBalanceWalletRequest(
                        findWalletResponse.id(),
                        request.amount().negate()
                )
        );
        final Transaction transaction = Transaction.createPaymentTransaction(
                wallet.userId(), wallet.id(),
                request.courseId(), request.amount()
        );
        transactionRepository.save(transaction);

        return new PaymentTransactionResponse(wallet.id(), wallet.balance());
    }
}
