package com.evolut.payment.service;

import com.evolut.payment.model.Status;
import com.evolut.payment.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    Transaction getTransactionById(String transactionId);

    Transaction createTransaction(String accFromId, String accToId, BigDecimal amount);

    Transaction completeTransaction(String transactionId);

    Transaction cancelTransaction(String transactionId);

    List<Transaction> getTransactionsByAccSerial(String accountSerial, Status status);

    List<Transaction> getAllTransactions(Status status);
}