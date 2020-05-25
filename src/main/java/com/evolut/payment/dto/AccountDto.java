package com.evolut.payment.dto;

import com.evolut.payment.model.Account;

import java.math.BigDecimal;
import java.util.Objects;

public class AccountDto {

    private String id;

    private String serial;

    private String owner;

    private BigDecimal balance;

    private BigDecimal blocked;

    private String latestTransaction;

    private AccountDto() {
    }

    public AccountDto(String id, String serial, String owner, BigDecimal balance, BigDecimal blocked, String transactionId) {
        this.id = id;
        this.serial = serial;
        this.owner = owner;
        this.balance = balance;
        this.blocked = blocked;
        this.latestTransaction = transactionId;
    }

    public static AccountDto from(Account account) {
        return new AccountDto(account.getId(), account.getSerial(),
                account.getOwner(), account.getBalance(), account.getBlocked(), account.getTransactionId());
    }

    public String getId() {
        return id;
    }

    public String getSerial() {
        return serial;
    }

    public String getOwner() {
        return owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getBlocked() {
        return blocked;
    }

    public String getLatestTransaction() {
        return latestTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDto that = (AccountDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(serial, that.serial) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(balance, that.balance) &&
                Objects.equals(blocked, that.blocked) &&
                Objects.equals(latestTransaction, that.latestTransaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serial, owner, balance, blocked, latestTransaction);
    }

    @Override
    public String toString() {
        return "AccountDto{" +
                "id='" + id + '\'' +
                ", serial='" + serial + '\'' +
                ", owner='" + owner + '\'' +
                ", balance=" + balance +
                ", blocked=" + blocked +
                ", latestTransaction='" + latestTransaction + '\'' +
                '}';
    }
}
