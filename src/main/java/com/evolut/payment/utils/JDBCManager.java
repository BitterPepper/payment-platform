package com.evolut.payment.utils;

import com.google.inject.Inject;
import org.hsqldb.jdbc.JDBCPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Supplier;

public class JDBCManager {
    private Logger LOG = LoggerFactory.getLogger(JDBCManager.class);

    private JDBCPool jdbcPool;
    private ThreadLocal<Connection> localConnection = new ThreadLocal<>();

    @Inject
    public JDBCManager(JDBCPool connectionPool) {
        jdbcPool = connectionPool;
    }

    public <T> T executeTransaction(Supplier<T> transactionChain) {
        T result;
        try (Connection connection = jdbcPool.getConnection()) {
            connection.setAutoCommit(false);
            localConnection.set(connection);
            result = transactionChain.get();
            connection.commit();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public <T> T executeStatement(String sql, JDBCConsumer<PreparedStatement, T> executeStatement) {
        T result;
        try (PreparedStatement statement = localConnection.get().prepareStatement(sql)) {
            result = executeStatement.accept(statement);
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public void cleanAndCloseDB() {
        executeTransaction(() -> {
                    String sql = "TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK";
                    return executeStatement(sql, PreparedStatement::execute);
                }
        );
    }
}
