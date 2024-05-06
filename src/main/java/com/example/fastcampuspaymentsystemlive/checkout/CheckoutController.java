package com.example.fastcampuspaymentsystemlive.checkout;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Controller
public class CheckoutController {
    // TODO
    /*
    1. checkout 페이지 렌더링 시 orderId 를 만들어주어야 한다.
        1.1 주문서비스로부터 오는걸 가정 ..
    2. PG 와 주고받은 데이터를 저장할필요가 있음.
        2.1 요청 > 승인
        2.2 요청 데이터도 저장.
        2.3 승인 데이터도 저장.
    3. 상품구매 API 와 연결
     */

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

    @RequestMapping(value = "/confirm")
    public ResponseEntity<Object> confirmPayment(@RequestBody String jsonBody) throws Exception {
        final JsonNode jsonNode = new ObjectMapper().readTree(jsonBody);
        final ConfirmRequest request = new ConfirmRequest(
                jsonNode.get("paymentKey").asText(),
                jsonNode.get("orderId").asText(),
                jsonNode.get("amount").asText()
        );

        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        RestClient defaultClient = RestClient.create();
        final Object object = defaultClient.post()
                .uri("https://api.tosspayments.com/v1/payments/confirm")
                .headers(httpHeaders -> {
                    httpHeaders.add("Authorization", authorizations);
                    httpHeaders.add("Content-Type", "application/json");
                })
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(Object.class);

        return ResponseEntity.ok(object);
    }

    public record ConfirmRequest(String paymentKey, String orderId, String amount) {
    }
}
