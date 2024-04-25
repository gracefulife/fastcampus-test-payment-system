package com.example.fastcampuspaymentsystemlive.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findTopByUserId(Long userId);

    List<Wallet> findAllByUserId(Long userId);
}
