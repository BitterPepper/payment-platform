package com.evolut.payment.manager;

import com.evolut.payment.model.Status;
import com.evolut.payment.model.Transaction;
import com.evolut.payment.utils.JDBCManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class TransactionManagerJDBCImpl implements TransactionManager {

    @Inject
    private JDBCManager jdbcManager;

    @Override
    public Transaction create(Transaction transaction) {
        String sql = "INSERT INTO TRANSACTION(ID, ACC_FROM, ACC_TO, AMOUNT, DATE, STATUS, LATEST)" +
                "VALUES(?,?,?,?,?,?,?)";
        return jdbcManager.executeStatement(sql, (statement) -> {
            statement.setString(1, transaction.getId());
            statement.setString(2, transaction.getAccFromSerial());
            statement.setString(3, transaction.getAccToSerial());
            statement.setBigDecimal(4, transaction.getAmount());
            statement.setTimestamp(5, Timestamp.valueOf(transaction.getTimestamp()));
            statement.setString(6, transaction.getStatus().toString());
            statement.setInt(7, transaction.getLatest());
            statement.executeUpdate();
            return transaction;
        });
    }

    @Override
    public int update(String transactionId, Integer latest) {
        String sql = "UPDATE TRANSACTION SET LATEST = ? WHERE ID = ? AND LATEST = 1";
        return jdbcManager.executeStatement(sql, (statement) -> {
            statement.setInt(1, latest);
            statement.setString(2, transactionId);
            return statement.executeUpdate();
        });
    }

    @Override
    public Transaction findById(String transactionId) {
        String sql = "SELECT * FROM TRANSACTION WHERE ID = ? AND LATEST = 1";
        return jdbcManager.executeStatement(sql, (statement) -> {
            Transaction result = null;
            statement.setString(1, transactionId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = mapTransaction(resultSet);
            }
            return result;
        });
    }

    @Override
    public List<Transaction> findByAccSerial(String accountSerial, Status status) {
        String sql = "SELECT * FROM TRANSACTION WHERE LATEST = 1 AND (ACC_FROM = ? OR ACC_TO = ?)"
                + addStatusSQL(status);
        return jdbcManager.executeStatement(sql, (statement) -> {
            List<Transaction> result = new ArrayList<>();
            statement.setString(1, accountSerial);
            statement.setString(2, accountSerial);
            addStatusCondition(statement, status, 3);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(mapTransaction(resultSet));
            }
            return result;
        });
    }

    @Override
    public List<Transaction> findAll(Status status) {
        String sql = "SELECT * FROM TRANSACTION WHERE LATEST = 1" + addStatusSQL(status);
        return jdbcManager.executeStatement(sql, (statement) -> {
            List<Transaction> result = new ArrayList<>();
            addStatusCondition(statement, status, 1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(mapTransaction(resultSet));
            }
            return result;
        });
    }

    private Transaction mapTransaction(ResultSet resultSet) throws SQLException {
        return Transaction.create(
                resultSet.getString("ID")
                , resultSet.getString("ACC_FROM")
                , resultSet.getString("ACC_TO")
                , resultSet.getBigDecimal("AMOUNT")
                , new Timestamp(resultSet.getDate("TIMESTAMP").getTime()).toLocalDateTime()
                , Status.valueOf(resultSet.getString("STATUS"))
                , resultSet.getInt("LATEST"));
    }

    private String addStatusSQL(Status status) {
        return status != null ? "AND status = ?" : "";
    }

    private void addStatusCondition(PreparedStatement statement, Status status, int condNum) throws SQLException {
        if (status != null) {
            statement.setString(condNum, status.name());
        }
    }
}
