package com.example.fastcampuspaymentsystemlive.processing;

import com.example.fastcampuspaymentsystemlive.checkout.ConfirmRequest;
import com.example.fastcampuspaymentsystemlive.external.PaymentGatewayService;
import com.example.fastcampuspaymentsystemlive.order.Order;
import com.example.fastcampuspaymentsystemlive.order.OrderRepository;
import com.example.fastcampuspaymentsystemlive.retry.RetryRequestRepository;
import com.example.fastcampuspaymentsystemlive.retry.RetryRequest;
import com.example.fastcampuspaymentsystemlive.transaction.ChargeTransactionRequest;
import com.example.fastcampuspaymentsystemlive.transaction.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentProcessingService {
    private final PaymentGatewayService paymentGatewayService;
    private final TransactionService transactionService;

    private final OrderRepository orderRepository;

    private final RetryRequestRepository retryRequestRepository;
    private final ObjectMapper objectMapper;

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

        approveOrder(confirmRequest.orderId());
    }

    public void createCharge(ConfirmRequest confirmRequest, boolean isRetry) {
        /*
        .. 주문서비스 프로세스
        3. 결제서비스 > PG 승인 요청
        4. 결제서비스 > 충전기록 저장
            > 결제수단 > 잔액 충전 구현
        ...
        6. 주문서비스에 응답
        7. 주문을 APPROVED 상태로 변경
         */
        try {
            paymentGatewayService.confirm(confirmRequest);
        } catch (Exception e) {
            log.error("caught exception on createCharge", e);
            if (!isRetry && e instanceof RestClientException &&
                    e.getCause() instanceof SocketTimeoutException) {
                createRetryRequest(confirmRequest, e);
            }
            throw e;
        }

        final Order order = orderRepository.findByRequestId(confirmRequest.orderId());
        transactionService.charge(
                new ChargeTransactionRequest(
                        order.getUserId(),
                        confirmRequest.orderId(),
                        new BigDecimal(confirmRequest.amount())
                )
        );

        approveOrder(confirmRequest.orderId());
    }

    @SneakyThrows
    private void createRetryRequest(ConfirmRequest confirmRequest, Exception e) {
        RetryRequest retryRequest = new RetryRequest(
                objectMapper.writeValueAsString(confirmRequest),
                confirmRequest.orderId(),
                e.getMessage(),
                RetryRequest.Type.CONFIRM
        );
        retryRequestRepository.save(retryRequest);
    }

    private void approveOrder(String orderId) {
        final Order order = orderRepository.findByRequestId(orderId);
        order.setStatus(Order.Status.APPROVED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }
}
