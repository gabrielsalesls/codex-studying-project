package dev.gabrielsales.wallet_service.wallet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @Column(nullable = false, updatable = false)
    private String walletId;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    protected Wallet() {
    }

    public Wallet(String walletId, Long userId, UserType userType, BigDecimal balance) {
        this.walletId = walletId;
        this.userId = userId;
        this.userType = userType;
        this.balance = balance;
    }

    public String getWalletId() {
        return walletId;
    }

    public Long getUserId() {
        return userId;
    }

    public UserType getUserType() {
        return userType;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
