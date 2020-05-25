package com.evolut.payment.manager;

import com.evolut.payment.model.Status;
import com.evolut.payment.model.Transaction;

import java.util.List;

public interface TransactionManager {

    Transaction create(Transaction transaction);

    int update(String transactionId, Integer latest);

    Transaction findById(String transactionId);

    List<Transaction> findByAccSerial(String accountSerial, Status status);

    List<Transaction> findAll(Status status);
}
