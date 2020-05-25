package com.evolut.payment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

    private String id;

    private String accFromSerial;

    private String accToSerial;

    private BigDecimal amount;

    public LocalDateTime timestamp;

    private Status status;

    private Integer latest;

    public static Transaction create(String accFromSerial, String accToSerial, BigDecimal amount) {
        return create(UUID.randomUUID().toString(), accFromSerial, accToSerial, amount, LocalDateTime.now(),
                Status.NEW, 1);
    }

    public static Transaction copy(Transaction transaction, Status status) {
        return create(UUID.randomUUID().toString(), transaction.accFromSerial, transaction.getAccToSerial(),
                transaction.amount, transaction.getTimestamp(), status, 1);
    }

    public static Transaction create(String id, String accFromSerial, String accToSerial, BigDecimal amount,
                                     LocalDateTime timestamp, Status status, Integer latest) {
        Transaction transaction = new Transaction();
        transaction.id = id;
        transaction.accFromSerial = accFromSerial;
        transaction.accToSerial = accToSerial;
        transaction.amount = amount;
        transaction.timestamp = timestamp;
        transaction.status = status;
        transaction.latest = latest;
        return transaction;
    }

    public String getId() {
        return id;
    }

    public String getAccFromSerial() {
        return accFromSerial;
    }

    public String getAccToSerial() {
        return accToSerial;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Status getStatus() {
        return status;
    }

    public Integer getLatest() {
        return latest;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(accFromSerial, that.accFromSerial) &&
                Objects.equals(accToSerial, that.accToSerial) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(timestamp, that.timestamp) &&
                status == that.status &&
                Objects.equals(latest, that.latest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accFromSerial, accToSerial, amount, timestamp, status, latest);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", accFromSerial='" + accFromSerial + '\'' +
                ", accToSerial='" + accToSerial + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", latest=" + latest +
                '}';
    }
}
