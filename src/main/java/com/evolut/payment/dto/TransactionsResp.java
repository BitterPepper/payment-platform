package com.evolut.payment.dto;

import com.evolut.payment.model.Transaction;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransactionsResp {
    private List<TransactionDto> transactions;

    public TransactionsResp() {
    }

    public TransactionsResp(List<TransactionDto> transactions) {
        this.transactions = transactions;
    }

    public static TransactionsResp from(List<Transaction> transactions) {
        return new TransactionsResp(transactions.stream().map(TransactionDto::from).collect(Collectors.toList()));
    }

    public List<TransactionDto> getTransactions() {
        return transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionsResp that = (TransactionsResp) o;
        return Objects.equals(transactions, that.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactions);
    }

    @Override
    public String toString() {
        return "TransactionsResp{" +
                "transactions=" + transactions +
                '}';
    }
}
