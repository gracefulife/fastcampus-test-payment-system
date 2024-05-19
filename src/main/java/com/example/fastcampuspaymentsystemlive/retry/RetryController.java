package com.example.fastcampuspaymentsystemlive.retry;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class RetryController {
    private final RetryRequestService retryRequestService;

    @PostMapping("/api/retry-request/{retryId}")
    public void retry(@PathVariable("retryId") Long retryId) {
        retryRequestService.retry(retryId);
    }
}
