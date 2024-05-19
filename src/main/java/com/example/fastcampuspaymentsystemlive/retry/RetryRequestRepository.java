package com.example.fastcampuspaymentsystemlive.retry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetryRequestRepository extends JpaRepository<RetryRequest, Long> {
    // TODO status, type 별로 조회
    // 실패한 데이터 주기적으로 조회 >> IN_PROGRESS

}
