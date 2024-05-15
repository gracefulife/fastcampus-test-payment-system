package com.example.fastcampuspaymentsystemlive.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByUserId(Long userId);

    Order findByRequestId(String requestId);
}
