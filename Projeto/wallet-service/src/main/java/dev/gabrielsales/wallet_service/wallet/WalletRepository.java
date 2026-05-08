package dev.gabrielsales.wallet_service.wallet;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, String> {

    boolean existsByUserId(Long userId);

    Optional<Wallet> findByUserId(Long userId);
}
