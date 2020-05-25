package com.evolut.payment.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.evolut.payment.manager.AccountManager;
import com.evolut.payment.utils.JDBCManager;
import com.evolut.payment.model.Account;

import java.math.BigDecimal;
import java.util.List;

@Singleton
public class AccountServiceImpl implements AccountService {

    @Inject
    private AccountManager accountManager;

    @Inject
    private JDBCManager jdbcManager;

    public Account createAccount(String serial, String owner, BigDecimal balance) {
        Account account = Account.create(serial, owner, balance);
        return jdbcManager.executeTransaction(() -> accountManager.create(account));
    }

    public Account getAccountBySerial(String accountSerial) {
        return jdbcManager.executeTransaction(() -> accountManager.findBySerial(accountSerial));
    }

    public List<Account> getAllAccounts() {
        return jdbcManager.executeTransaction(() -> accountManager.findAll());
    }

}
