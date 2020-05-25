package com.evolut.payment.dto;

import com.evolut.payment.model.Account;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AccountsResp {
    private List<AccountDto> accounts;

    private AccountsResp() {
    }

    private AccountsResp(List<AccountDto> accounts) {
        this.accounts = accounts;
    }

    public static AccountsResp from(List<Account> accounts) {
        return new AccountsResp(accounts.stream().map(AccountDto::from).collect(Collectors.toList()));
    }

    public List<AccountDto> getAccounts() {
        return accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountsResp that = (AccountsResp) o;
        return Objects.equals(accounts, that.accounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accounts);
    }

    @Override
    public String toString() {
        return "AccountsResp{" +
                "accounts=" + accounts +
                '}';
    }
}
