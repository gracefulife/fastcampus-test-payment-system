package com.example.fastcampuspaymentsystemlive.retry;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class RetryRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestJson;

    private String requestId;

    private Integer retryCount;

    private String errorResponse;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Type type;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RetryRequest(String requestJson, String requestId,
                        String errorResponse, Type type) {
        this.requestJson = requestJson;
        this.requestId = requestId;
        this.retryCount = 0;
        this.errorResponse = errorResponse;
        this.status = Status.IN_PROGRESS;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public enum Status {
        IN_PROGRESS, SUCCESS, FAILURE
    }

    public enum Type {
        CONFIRM
    }
}
