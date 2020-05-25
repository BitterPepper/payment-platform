package com.evolut.payment.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Account {

    private String id;

    private String serial;

    private String owner;

    private BigDecimal balance;

    private BigDecimal blocked;

    private String transactionId;

    private Integer latest;

    /* in real situation should / could be added
     * - currency
     * - pin
     * - active or not
     * - banned fro some period of time or not
     * */

    public static Account create(String serial, String owner, BigDecimal balance) {
        return create(UUID.randomUUID().toString(), serial, owner, balance, BigDecimal.valueOf(0),
                null, 1);
    }

    public static Account copy(Account account, BigDecimal balance, BigDecimal blocked, String transactionId) {
        return create(UUID.randomUUID().toString(), account.getSerial(), account.getOwner(), balance, blocked,
                transactionId, 1);
    }

    public static Account create(String id, String serial, String owner, BigDecimal balance, BigDecimal blocked,
                                 String transactionId, Integer isLatest) {
        Account account = new Account();
        account.id = id;
        account.serial = serial;
        account.owner = owner;
        account.balance = balance;
        account.blocked = blocked;
        account.transactionId = transactionId;
        account.latest = isLatest;
        return account;
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

    public String getTransactionId() {
        return transactionId;
    }

    public Integer getLatest() {
        return latest;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setBlocked(BigDecimal blocked) {
        this.blocked = blocked;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(serial, account.serial) &&
                Objects.equals(owner, account.owner) &&
                Objects.equals(balance, account.balance) &&
                Objects.equals(blocked, account.blocked) &&
                Objects.equals(transactionId, account.transactionId) &&
                Objects.equals(latest, account.latest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serial, owner, balance, blocked, transactionId, latest);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", serial='" + serial + '\'' +
                ", owner='" + owner + '\'' +
                ", balance=" + balance +
                ", blocked=" + blocked +
                ", transactionId='" + transactionId + '\'' +
                ", latest=" + latest +
                '}';
    }
}
