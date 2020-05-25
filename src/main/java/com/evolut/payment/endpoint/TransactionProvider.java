package com.evolut.payment.endpoint;

import com.evolut.payment.dto.CreateTransactionReq;
import com.evolut.payment.dto.TransactionDto;
import com.evolut.payment.dto.TransactionsResp;
import com.evolut.payment.exception.TransactionNotFoundException;
import com.evolut.payment.model.Status;
import com.evolut.payment.model.Transaction;
import com.evolut.payment.service.TransactionService;
import com.evolut.payment.utils.GSONHelper;
import com.evolut.payment.utils.ValidationHelper;
import com.google.inject.Inject;
import spark.Route;

import java.util.List;

import static spark.Spark.*;

public class TransactionProvider {

    @Inject
    private TransactionService transactionService;

    @Inject
    private ValidationHelper validationHelper;

    public void init() {
        path("/transaction", () -> {
            post("", addTransaction(), GSONHelper.toJson());
            get("", getAllTransactions(), GSONHelper.toJson());
            patch("/confirm/:id", confirmTransaction(), GSONHelper.toJson());
            patch("/cancel/:id", cancelTransaction(), GSONHelper.toJson());
            get("/getById/:id", getTransactionById(), GSONHelper.toJson());
            get("/getByAccountId", getTransactionsByAccId(), GSONHelper.toJson());
        });
    }

    public Route addTransaction() {
        return (req, res) -> {
            CreateTransactionReq trnReq = GSONHelper.fromJson(req.body(), CreateTransactionReq.class);
            validationHelper.checkAccount(trnReq, "Transaction creation validation failed: ");
            Transaction transaction = transactionService
                    .createTransaction(trnReq.getAccFromSerial(), trnReq.getAccToSerial(), trnReq.getAmount());
            return TransactionDto.from(transaction);
        };
    }

    public Route confirmTransaction() {
        return (req, res) -> {
            Transaction transaction = transactionService.completeTransaction(req.params(":id"));
            return TransactionDto.from(transaction);
        };
    }

    public Route cancelTransaction() {
        return (req, res) -> {
            Transaction transaction = transactionService.cancelTransaction(req.params(":id"));
            return TransactionDto.from(transaction);
        };
    }

    public Route getTransactionById() {
        return (req, res) -> {
            String transactionId = req.params(":id");
            Transaction transaction = transactionService.getTransactionById(transactionId);
            checkTransaction(transaction, transactionId);
            return TransactionDto.from(transaction);
        };
    }

    public Route getTransactionsByAccId() {
        return (req, res) -> {
            String transactionId = req.queryParams("accId");
            Status status = Status.fromString(req.queryParams("status"));
            List<Transaction> transactions = transactionService.getTransactionsByAccSerial(transactionId, status);
            return TransactionsResp.from(transactions);
        };
    }

    public Route getAllTransactions() {
        return (req, res) -> {
            Status status = Status.fromString(req.queryParams("status"));
            List<Transaction> transactions = transactionService.getAllTransactions(status);
            return TransactionsResp.from(transactions);
        };
    }

    private void checkTransaction(Transaction transaction, String transactionId) {
        if (transaction == null) {
            String errorMessage = String.format("Transaction with id: %s not found", transactionId);
            throw new TransactionNotFoundException(errorMessage);
        }
    }
}
