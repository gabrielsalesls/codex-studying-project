package dev.gabrielsales.wallet_service.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, String> {

    boolean existsByUserId(Long userId);
}
