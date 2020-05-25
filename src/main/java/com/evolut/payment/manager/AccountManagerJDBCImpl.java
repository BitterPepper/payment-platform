package com.evolut.payment.manager;

import com.evolut.payment.model.Account;
import com.evolut.payment.utils.JDBCManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class AccountManagerJDBCImpl implements AccountManager {

    @Inject
    private JDBCManager jdbcManager;

    @Override
    public Account create(Account account) {
        String sql = "INSERT INTO ACCOUNT(ID, SERIAL, OWNER, BALANCE, BLOCKED, TRAN_ID, LATEST) VALUES(?,?,?,?,?,?,?)";
        return jdbcManager.executeStatement(sql, (statement) -> {
            System.out.println("******* " + account);
            statement.setString(1, account.getId());
            statement.setString(2, account.getSerial());
            statement.setString(3, account.getOwner());
            statement.setBigDecimal(4, account.getBalance());
            statement.setBigDecimal(5, account.getBlocked());
            statement.setString(6, account.getTransactionId());
            statement.setInt(7, account.getLatest());
            statement.executeUpdate();
            return account;
        });
    }

    @Override
    public int update(String accountSerial, Integer latest) {
        String sql = "UPDATE ACCOUNT SET LATEST = ? WHERE SERIAL = ? AND LATEST = 1";
        return jdbcManager.executeStatement(sql, (statement) -> {
            statement.setInt(1, latest);
            statement.setString(2, accountSerial);
            return statement.executeUpdate();
        });
    }

    @Override
    public Account findBySerial(String accountSerial) {
        String sql = "SELECT * FROM ACCOUNT WHERE SERIAL = ? AND LATEST = 1";
        return jdbcManager.executeStatement(sql, (statement) -> {
            Account result = null;
            statement.setString(1, accountSerial);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = mapAccount(resultSet);
            }
            return result;
        });
    }

    @Override
    public List<Account> findAll() {
        String sql = "SELECT * FROM ACCOUNT WHERE LATEST = 1";
        return jdbcManager.executeStatement(sql, (statement) -> {
            List<Account> result = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(mapAccount(resultSet));
            }
            return result;
        });
    }

    private Account mapAccount(ResultSet resultSet) throws SQLException {
        return Account.create(
                resultSet.getString("ID")
                , resultSet.getString("SERIAL")
                , resultSet.getString("OWNER")
                , resultSet.getBigDecimal("BALANCE")
                , resultSet.getBigDecimal("BLOCKED")
                , resultSet.getString("TRAN_ID")
                , resultSet.getInt("LATEST"));
    }

}
