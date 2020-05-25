package com.evolut.payment.service;

import com.evolut.payment.exception.AccountNotFoundException;
import com.evolut.payment.exception.InsufficientBalanceException;
import com.evolut.payment.exception.TransactionNotFoundException;
import com.evolut.payment.exception.TransactionStatusException;
import com.evolut.payment.manager.AccountManager;
import com.evolut.payment.manager.TransactionManager;
import com.evolut.payment.model.Account;
import com.evolut.payment.model.Status;
import com.evolut.payment.model.Transaction;
import com.evolut.payment.utils.JDBCManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.math.BigDecimal;
import java.util.List;

@Singleton
public class TransactionServiceImpl implements TransactionService {

    @Inject
    private TransactionManager transactionManager;

    @Inject
    private AccountManager accountManager;

    @Inject
    private JDBCManager jdbcManager;

    @Override
    public Transaction getTransactionById(String transactionId) {
        return jdbcManager.executeTransaction(() -> transactionManager.findById(transactionId));
    }

    @Override
    public Transaction createTransaction(String accFromSerial, String accToSerial, BigDecimal amount) {
        Transaction transaction = Transaction.create(accFromSerial, accToSerial, amount);
        return jdbcManager.executeTransaction(() -> {
            blockAccounts(transaction.getAccFromSerial());
            Account accountFrom = accountManager.findBySerial(transaction.getAccFromSerial());
            Account accountTo = accountManager.findBySerial(transaction.getAccToSerial());
            checkAccount(accountFrom, transaction.getAccFromSerial());
            checkAccount(accountTo, transaction.getAccToSerial());
            checkBalance(accountFrom, transaction);

            transactionManager.create(transaction);
            Account updateAccount = Account.copy(accountFrom, accountFrom.getBalance(),
                    accountFrom.getBlocked().add(transaction.getAmount()), transaction.getId());
            updateAccounts(updateAccount);

            return transaction;
        });
    }

    @Override
    public Transaction completeTransaction(String transactionId) {
        return jdbcManager.executeTransaction(() -> {

            blockTransactions(transactionId);
            Transaction transaction = transactionManager.findById(transactionId);
            checkTransaction(transaction, transactionId);
            checkTransactionStatus(transaction, Status.NEW);
            blockAccounts(transaction.getAccFromSerial(), transaction.getAccToSerial());

            Transaction updateTransaction = Transaction.copy(transaction, Status.COMPLETED);
            updateTransaction(updateTransaction);

            Account accountFrom = accountManager.findBySerial(transaction.getAccFromSerial());
            Account accountTo = accountManager.findBySerial(transaction.getAccToSerial());

            Account updateAccountFrom = Account.copy(accountFrom, accountFrom.getBalance().subtract(transaction.getAmount()),
                    accountFrom.getBlocked().subtract(transaction.getAmount()), transactionId);
            Account updateAccountTo = Account.copy(accountTo, accountTo.getBalance().add(transaction.getAmount()),
                    accountTo.getBlocked(), transactionId);

            updateAccounts(updateAccountFrom, updateAccountTo);

            return updateTransaction;
        });
    }

    @Override
    public Transaction cancelTransaction(String transactionId) {
        return jdbcManager.executeTransaction(() -> {
            blockTransactions(transactionId);
            Transaction transaction = transactionManager.findById(transactionId);
            checkTransaction(transaction, transactionId);
            checkTransactionStatus(transaction, Status.NEW);
            blockAccounts(transaction.getAccFromSerial());

            Transaction updateTransaction = Transaction.copy(transaction, Status.CANCELED);
            updateTransaction(updateTransaction);

            Account accountFrom = accountManager.findBySerial(transaction.getAccFromSerial());
            Account updateAccount = Account.copy(accountFrom, accountFrom.getBalance(),
                    accountFrom.getBlocked().subtract(transaction.getAmount()), transaction.getId());
            updateAccounts(updateAccount);

            return updateTransaction;
        });
    }

    @Override
    public List<Transaction> getTransactionsByAccSerial(String transactionId, Status status) {
        return jdbcManager.executeTransaction(() -> transactionManager.findByAccSerial(transactionId, status));
    }

    @Override
    public List<Transaction> getAllTransactions(Status status) {
        return jdbcManager.executeTransaction(() -> transactionManager.findAll(status));
    }

    private void checkBalance(Account account, Transaction transaction) {
        BigDecimal availBalance = account.getBalance().subtract(account.getBlocked());
        if (transaction.getAmount().compareTo(availBalance) > 0) {
            String errorMessage = String.format(
                    "Not enough money for transfer money from acc id: %s to acc id: %s. Necessary: %s,available %s",
                    transaction.getAccFromSerial(), transaction.getAccToSerial(), transaction.getAmount(), availBalance);
            throw new InsufficientBalanceException(errorMessage);
        }
    }

    private void blockAccounts(String... accountsSerial) {
        for (String accountSerial : accountsSerial) {
            accountManager.update(accountSerial, 1);
        }
    }

    private void updateAccounts(Account... updateAccounts) {
        for (Account account : updateAccounts){
            accountManager.update(account.getSerial(), -1);
            accountManager.create(account);
        }
    }

    private void blockTransactions(String transactionId) {
        transactionManager.update(transactionId, 1);
    }

    private void updateTransaction(Transaction transaction) {
        transactionManager.update(transaction.getId(), -1);
        transactionManager.create(transaction);
    }

    private void checkAccount(Account account, String accountSerial) {
        if (account == null) {
            String errorMessage = String.format("Account with serial: %s not found", accountSerial);
            throw new AccountNotFoundException(errorMessage);
        }
    }

    private void checkTransaction(Transaction transaction, String transactionId) {
        if (transaction == null) {
            String errorMessage = String.format("Transaction with id: %s not found", transactionId);
            throw new TransactionNotFoundException(errorMessage);
        }
    }

    private void checkTransactionStatus(Transaction transaction, Status status) {
        if (transaction.getStatus() != status) {
            String errorMessage = String.format("Transaction with id: %s can not be completed becouse has status: %s"
                    , transaction.getId(), transaction.getStatus());
            throw new TransactionStatusException(errorMessage);
        }
    }
}
