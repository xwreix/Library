package com.lab.library.dao;

import java.sql.*;

public class ProfitDao {
    private final String SELECT_BOOKS_AMOUNT = "SELECT COUNT(*) from issue\n" +
            "where returnDate >= ? AND returnDate <= ?";
    private final String SELECT_PAYMENTS = "select amount from payment\n" +
            "where paymDate >= ? AND paymDate <= ?";
    private final String SELECT_READERS_AMOUNT = "select count(distinct readerId) from issue\n" +
            "where returnDate >= ? AND returnDate <= ?";

    public ResultSet selectBooksAmount(Date start, Date finish, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOKS_AMOUNT);
        preparedStatement.setDate(1, start);
        preparedStatement.setDate(2, finish);
        return preparedStatement.executeQuery();
    }

    public ResultSet selectPayments(Date start, Date finish, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PAYMENTS);
        preparedStatement.setDate(1, start);
        preparedStatement.setDate(2, finish);
        return preparedStatement.executeQuery();
    }

    public ResultSet selectReadersAmount(Date start, Date finish, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_READERS_AMOUNT);
        preparedStatement.setDate(1, start);
        preparedStatement.setDate(2, finish);
        return preparedStatement.executeQuery();
    }
}
