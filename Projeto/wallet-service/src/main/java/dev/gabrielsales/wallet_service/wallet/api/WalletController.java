package dev.gabrielsales.wallet_service.wallet.api;

import dev.gabrielsales.wallet_service.wallet.WalletService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<WalletCreatedResponse> create(@Valid @RequestBody CreateWalletRequest request) {
        var response = walletService.create(request);
        return ResponseEntity.created(URI.create("/wallet/%s".formatted(response.userId()))).body(response);
    }
}
