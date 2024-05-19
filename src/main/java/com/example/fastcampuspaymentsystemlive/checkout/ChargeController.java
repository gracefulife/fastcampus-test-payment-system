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
public class ChargeController {
    private final OrderRepository orderRepository;
    private final PaymentProcessingService paymentProcessingService;

    @GetMapping("/charge-order")
    public String order(
            @RequestParam("userId") Long userId,
            @RequestParam("amount") String amount,
            Model model
    ) {
        Order order = new Order();
        order.setAmount(new BigDecimal(amount));
        order.setUserId(userId);
        order.setRequestId(UUID.randomUUID().toString());
        order.setStatus(Order.Status.WAIT);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        model.addAttribute("requestId", order.getRequestId());
        model.addAttribute("amount", amount);
        model.addAttribute("customerKey", "customerKey-" + userId);
        return "/charge-order.html";
    }

    @GetMapping("/charge-order-requested")
    public String orderRequested() {
        return "/charge-order-requested.html";
    }

    @GetMapping("/charge-fail")
    public String fail() {
        return "/charge-fail.html";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/charge-confirm")
    public ResponseEntity<Object> confirm(
            @RequestBody ConfirmRequest confirmRequest) throws Exception {
        // 1번
        Order order = orderRepository.findByRequestId(confirmRequest.orderId());
        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus(Order.Status.REQUESTED);
        orderRepository.save(order);

        // 2번.
        paymentProcessingService.createCharge(confirmRequest, false);


        return ResponseEntity.ok(null);
    }
}
