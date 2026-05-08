package dev.gabrielsales.wallet_service.wallet;

import dev.gabrielsales.wallet_service.common.exception.ResourceConflictException;
import dev.gabrielsales.wallet_service.common.exception.ResourceNotFoundException;
import dev.gabrielsales.wallet_service.wallet.api.CreateWalletRequest;
import dev.gabrielsales.wallet_service.wallet.api.WalletCreatedResponse;
import dev.gabrielsales.wallet_service.wallet.api.WalletResponse;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    private static final BigDecimal INITIAL_BALANCE = BigDecimal.ZERO;

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public WalletCreatedResponse create(CreateWalletRequest request) {
        if (walletRepository.existsByUserId(request.userId())) {
            throw new ResourceConflictException(
                    "Wallet already exists for user with id %s".formatted(request.userId())
            );
        }

        var wallet = new Wallet(
                "wallet-%s".formatted(UUID.randomUUID()),
                request.userId(),
                request.userType(),
                INITIAL_BALANCE
        );

        var savedWallet = walletRepository.save(wallet);
        return new WalletCreatedResponse(
                savedWallet.getWalletId(),
                savedWallet.getUserId(),
                savedWallet.getBalance()
        );
    }

    @Transactional(readOnly = true)
    public WalletResponse findByUserId(Long userId) {
        var wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Wallet not found for user with id %s".formatted(userId)
                ));

        return new WalletResponse(wallet.getUserId(), wallet.getBalance());
    }
}
