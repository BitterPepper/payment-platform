package com.evolut.payment.manager;

import com.evolut.payment.model.Account;

import java.util.List;

public interface AccountManager {

    Account create(Account account);

    int update(String accountSerial, Integer latest);

    Account findBySerial(String accountSerial);

    List<Account> findAll();
}
