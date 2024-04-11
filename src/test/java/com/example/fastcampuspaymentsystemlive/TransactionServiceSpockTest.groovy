package com.example.fastcampuspaymentsystemlive

import com.example.fastcampuspaymentsystemlive.transaction.ChargeTransactionRequest
import com.example.fastcampuspaymentsystemlive.transaction.PaymentTransactionRequest
import com.example.fastcampuspaymentsystemlive.transaction.Transaction
import com.example.fastcampuspaymentsystemlive.transaction.TransactionRepository
import com.example.fastcampuspaymentsystemlive.transaction.TransactionService
import com.example.fastcampuspaymentsystemlive.wallet.*
import spock.lang.Specification

import java.time.LocalDateTime

class TransactionServiceSpockTest extends Specification {
    TransactionService transactionService
    WalletService walletService = Mock()
    TransactionRepository transactionRepository = Mock()

    def setup() {
        transactionService = new TransactionService(walletService, transactionRepository)
    }

    def "충전 트랜잭션이 성공한다."() {
        given:
        ChargeTransactionRequest request = new ChargeTransactionRequest(1L, "orderId", BigDecimal.TEN)
        transactionRepository.findTransactionByOrderId(request.orderId())
                >> Optional.empty()

        def findWalletResponse = new FindWalletResponse(1L, 1L,
                BigDecimal.ZERO, LocalDateTime.now(), LocalDateTime.now())
        walletService.findWalletByUserId(1L) >> findWalletResponse
        def addBalanceWalletResponse = new AddBalanceWalletResponse(
                1L, 1L,
                findWalletResponse.balance().add(request.amount()),
                LocalDateTime.now(), LocalDateTime.now()
        )
        walletService.addBalance(_) >> addBalanceWalletResponse

        when:
        def createdWallet = transactionService.charge(request)

        then:
        1 * transactionRepository.save(_)
        createdWallet != null
        println createdWallet
    }

    def "지갑이 없다면 충전이 실패한다."() {
        given:
        ChargeTransactionRequest request = new ChargeTransactionRequest(1L, "orderId", BigDecimal.TEN)
        transactionRepository.findTransactionByOrderId(request.orderId())
                >> Optional.empty()

        walletService.findWalletByUserId(1L) >> null

        when:
        def createdWallet = transactionService.charge(request)

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }

    def "이미 충전됐다면 충전 트랜잭션이 실패한다."() {
        given:
        ChargeTransactionRequest request = new ChargeTransactionRequest(1L, "orderId", BigDecimal.TEN)
        transactionRepository.findTransactionByOrderId(request.orderId())
                >> Optional.of(new Transaction())

        when:
        def createdWallet = transactionService.charge(request)

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }

    def "결제 트랜잭션이 성공한다."() {
        given:
        PaymentTransactionRequest request = new PaymentTransactionRequest(1L, "100", BigDecimal.TEN)
        transactionRepository.findTransactionByOrderId(request.courseId())
                >> Optional.empty()

        def findWalletResponse = new FindWalletResponse(1L, 1L,
                BigDecimal.TEN, LocalDateTime.now(), LocalDateTime.now())
        walletService.findWalletByWalletId(1L) >> findWalletResponse
        def addBalanceWalletResponse = new AddBalanceWalletResponse(
                1L, 1L,
                findWalletResponse.balance().add(request.amount().negate()),
                LocalDateTime.now(), LocalDateTime.now()
        )
        walletService.addBalance(_) >> addBalanceWalletResponse

        when:
        def createdWallet = transactionService.payment(request)

        then:
        1 * transactionRepository.save(_)
        createdWallet != null
        println createdWallet
    }

    // TODO
}
