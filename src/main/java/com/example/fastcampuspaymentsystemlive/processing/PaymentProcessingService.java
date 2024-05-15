package com.example.fastcampuspaymentsystemlive.processing;

import com.example.fastcampuspaymentsystemlive.checkout.ConfirmRequest;
import com.example.fastcampuspaymentsystemlive.external.PaymentGatewayService;
import com.example.fastcampuspaymentsystemlive.order.Order;
import com.example.fastcampuspaymentsystemlive.order.OrderRepository;
import com.example.fastcampuspaymentsystemlive.transaction.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PaymentProcessingService {
    private final PaymentGatewayService paymentGatewayService;
    private final TransactionService transactionService;

    private final OrderRepository orderRepository;

    public void createPayment(ConfirmRequest confirmRequest) {
        /*
        3. 결제서비스 > PG 승인 요청
        4. 결제서비스 > 결제기록 저장
            > 결제수단으로 바로 결제하는 메서드 구현
        ...
        6. 주문서비스에 응답
        7. 주문을 APPROVED 상태로 변경
         */
        paymentGatewayService.confirm(confirmRequest);
        transactionService.pgPayment(); // FIXME not yet

        final Order order = orderRepository.findByRequestId(confirmRequest.orderId());
        order.setStatus(Order.Status.APPROVED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

}
