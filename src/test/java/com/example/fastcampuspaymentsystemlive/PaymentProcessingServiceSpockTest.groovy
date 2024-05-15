package com.example.fastcampuspaymentsystemlive

import com.example.fastcampuspaymentsystemlive.checkout.ConfirmRequest
import com.example.fastcampuspaymentsystemlive.external.PaymentGatewayService
import com.example.fastcampuspaymentsystemlive.order.Order
import com.example.fastcampuspaymentsystemlive.order.OrderRepository
import com.example.fastcampuspaymentsystemlive.processing.PaymentProcessingService
import com.example.fastcampuspaymentsystemlive.transaction.TransactionService
import spock.lang.Specification

class PaymentProcessingServiceSpockTest extends Specification {
    PaymentProcessingService paymentProcessingService
    PaymentGatewayService paymentGatewayService = Mock()
    TransactionService transactionService = Mock()
    OrderRepository orderRepository = Mock()

    def setup() {
        paymentProcessingService = new PaymentProcessingService(
                paymentGatewayService, transactionService, orderRepository)
    }

    def "PG 결제 성공 시 결제기록이 생성된다."() {
        given:
        ConfirmRequest confirmRequest = new ConfirmRequest(
                "paymentKey",
                "orderId",
                "1000"
        )

        // mock
        Order order = new Order()
        orderRepository.findByRequestId(confirmRequest.orderId()) >> order

        when:
        paymentProcessingService.createPayment(confirmRequest)

        then:
        1 * paymentGatewayService.confirm(confirmRequest)
        1 * transactionService.pgPayment()
        1 * orderRepository.save(order)
    }

    def "PG 를 통한 결제 성공 시 충전된다."() {
        given:
        ConfirmRequest confirmRequest = new ConfirmRequest(
                "paymentKey",
                "orderId",
                "1000"
        )

        // mock
        Order order = new Order()
        orderRepository.findByRequestId(confirmRequest.orderId()) >> order

        when:
        paymentProcessingService.createCharge(confirmRequest)

        then:
        1 * paymentGatewayService.confirm(confirmRequest)
        1 * transactionService.charge(_)
        1 * orderRepository.save(order)
    }
}
