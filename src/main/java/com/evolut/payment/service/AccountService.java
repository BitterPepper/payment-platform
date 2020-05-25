package com.evolut.payment.service;

import com.evolut.payment.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    Account createAccount(String serial, String owner, BigDecimal balance);

    Account getAccountBySerial(String accountSerial);

    List<Account> getAllAccounts();
}
