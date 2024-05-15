package com.example.fastcampuspaymentsystemlive.checkout;

import com.example.fastcampuspaymentsystemlive.order.Order;
import com.example.fastcampuspaymentsystemlive.order.OrderRepository;
import com.example.fastcampuspaymentsystemlive.processing.PaymentProcessingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Controller
@AllArgsConstructor
public class CheckoutController {
    private final OrderRepository orderRepository;
    private final PaymentProcessingService paymentProcessingService;

    @GetMapping("/order")
    public String order(
            @RequestParam("userId") Long userId,
            @RequestParam("courseId") Long courseId,
            @RequestParam("courseName") String courseName,
            @RequestParam("amount") String amount,
            Model model
    ) {
        Order order = new Order();
        order.setAmount(new BigDecimal(amount));
        order.setCourseId(courseId);
        order.setCourseName(courseName);
        order.setUserId(userId);
        order.setRequestId(UUID.randomUUID().toString());
        order.setStatus(Order.Status.WAIT);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        model.addAttribute("courseName", courseName);
        model.addAttribute("requestId", order.getRequestId());
        model.addAttribute("amount", amount);
        model.addAttribute("customerKey", "customerKey-" + userId);
        return "/order.html";
    }

    @GetMapping("/order-requested")
    public String orderRequested() {
        return "/order-requested.html";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "/checkout.html";
    }

    @GetMapping("/success")
    public String success() {
        return "/success.html";
    }

    @GetMapping("/fail")
    public String fail() {
        return "/fail.html";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/confirm")
    public ResponseEntity<Object> confirmPayment(
            @RequestBody ConfirmRequest confirmRequest) throws Exception {
        /*
        1. 주문 서비스 - 주문 상태가 변경 됨 > REQUESTED
        2. 주문서비스 > 결제서비스 승인 요청 (POST API /confirm)
        3. 결제서비스 > PG 승인 요청
        4. 결제서비스 > 결제기록 저장
            > 결제수단으로 바로 결제하는 메서드 구현
        ...
        6. 주문서비스에 응답
        7. 주문을 APPROVED 상태로 변경
         */

        // 1번
        Order order = orderRepository.findByRequestId(confirmRequest.orderId());
        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus(Order.Status.REQUESTED);
        orderRepository.save(order);

        // 2번.
        paymentProcessingService.createPayment(confirmRequest);


        return ResponseEntity.ok(null);
    }
}
