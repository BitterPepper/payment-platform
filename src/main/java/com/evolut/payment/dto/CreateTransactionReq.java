package com.evolut.payment.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class CreateTransactionReq {

    private String accFromSerial;

    private String accToSerial;

    private BigDecimal amount;

    public String getAccFromSerial() {
        return accFromSerial;
    }

    public void setAccFromSerial(String accFromSerial) {
        this.accFromSerial = accFromSerial;
    }

    public String getAccToSerial() {
        return accToSerial;
    }

    public void setAccToSerial(String accToSerial) {
        this.accToSerial = accToSerial;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateTransactionReq that = (CreateTransactionReq) o;
        return Objects.equals(accFromSerial, that.accFromSerial) &&
                Objects.equals(accToSerial, that.accToSerial) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accFromSerial, accToSerial, amount);
    }

    @Override
    public String toString() {
        return "CreateTransactionReq{" +
                "accFromSerial='" + accFromSerial + '\'' +
                ", accToSerial='" + accToSerial + '\'' +
                ", amount=" + amount +
                '}';
    }
}
