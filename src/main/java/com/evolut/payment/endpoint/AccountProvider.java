package com.evolut.payment.endpoint;

import com.evolut.payment.dto.AccountDto;
import com.evolut.payment.dto.AccountsResp;
import com.evolut.payment.dto.CreateAccountReq;
import com.evolut.payment.exception.AccountNotFoundException;
import com.evolut.payment.model.Account;
import com.evolut.payment.service.AccountService;
import com.evolut.payment.utils.GSONHelper;
import com.evolut.payment.utils.ValidationHelper;
import com.google.inject.Inject;
import spark.Route;

import java.util.List;

import static spark.Spark.*;

public class AccountProvider {

    @Inject
    private AccountService accountService;

    @Inject
    private ValidationHelper validationHelper;

    public void init() {
        path("/account", () -> {
            post("", addAccount(), GSONHelper.toJson());
            get("", getAllAccounts(), GSONHelper.toJson());
            get("/getBySerial/:serial", getAccountBySerial(), GSONHelper.toJson());
        });
    }

    private Route addAccount() {
        return (req, res) -> {
            CreateAccountReq accountReq = GSONHelper.fromJson(req.body(), CreateAccountReq.class);
            validationHelper.checkAccount(accountReq, "Account creation validation failed: ");
            Account account = accountService
                    .createAccount(accountReq.getSerial(), accountReq.getOwner(), accountReq.getBalance());
            return AccountDto.from(account);
        };
    }

    private Route getAccountBySerial() {
        return (req, res) -> {
            String accountSerial = req.params(":serial");
            Account account = accountService.getAccountBySerial(accountSerial);
            checkAccount(account, accountSerial);
            return AccountDto.from(account);
        };
    }

    private Route getAllAccounts() {
        return (req, res) -> {
            List<Account> allAccounts = accountService.getAllAccounts();
            return AccountsResp.from(allAccounts);
        };
    }

    private void checkAccount(Account account, String accountSerial) {
        if (account == null) {
            String errorMessage = String.format("Account with serial: %s not found", accountSerial);
            throw new AccountNotFoundException(errorMessage);
        }
    }
}
