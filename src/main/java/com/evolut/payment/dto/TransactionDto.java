package com.evolut.payment.dto;

import com.evolut.payment.model.Status;
import com.evolut.payment.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class TransactionDto {

    private String id;

    private String accFromId;

    private String accToId;

    private BigDecimal amount;

    public LocalDateTime timestamp;

    private Status status;

    public TransactionDto() {
    }

    public TransactionDto(String id, String accFromId, String accToId, BigDecimal amount, LocalDateTime timestamp,
                          Status status) {
        this.id = id;
        this.accFromId = accFromId;
        this.accToId = accToId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
    }

    public static TransactionDto from(Transaction trn){
        return new TransactionDto(trn.getId(), trn.getAccFromSerial(), trn.getAccToSerial(), trn.getAmount(),
                trn.getTimestamp(), trn.getStatus());
    }

    public String getId() {
        return id;
    }

    public String getAccFromId() {
        return accFromId;
    }

    public String getAccToId() {
        return accToId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDto that = (TransactionDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(accFromId, that.accFromId) &&
                Objects.equals(accToId, that.accToId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(timestamp, that.timestamp) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accFromId, accToId, amount, timestamp, status);
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "id='" + id + '\'' +
                ", accFromId='" + accFromId + '\'' +
                ", accToId='" + accToId + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", status=" + status +
                '}';
    }
}
