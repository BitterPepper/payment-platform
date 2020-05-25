package com.evolut.payment.utils;

import java.sql.SQLException;

@FunctionalInterface
public interface JDBCConsumer<T, R> {

    R accept(T t) throws SQLException;

}