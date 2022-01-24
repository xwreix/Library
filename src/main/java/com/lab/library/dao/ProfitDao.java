package com.lab.library.dao;

import java.sql.*;

public class ProfitDao {
    private final String SELECT_BOOKS_AMOUNT = "SELECT COUNT(*) from issue\n" +
            "where returnDate >= ? AND returnDate <= ?";
    private final String SELECT_PAYMENTS = "select sum(amount) from payment\n" +
            "where paymDate >= ? AND paymDate <= ?";
    private final String SELECT_READERS_AMOUNT = "select count(distinct readerId) from issue\n" +
            "where returnDate >= ? AND returnDate <= ?";

    public int selectBooksAmount(Date start, Date finish, Connection connection) throws SQLException {
        int amount = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOKS_AMOUNT);
        preparedStatement.setDate(1, start);
        preparedStatement.setDate(2, finish);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            amount = resultSet.getInt(1);
        }

        return amount;
    }

    public double selectPayments(Date start, Date finish, Connection connection) throws SQLException {
        double sum = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PAYMENTS);
        preparedStatement.setDate(1, start);
        preparedStatement.setDate(2, finish);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            sum = resultSet.getDouble(1);
        }

        return sum;
    }

    public int selectReadersAmount(Date start, Date finish, Connection connection) throws SQLException {
        int amount = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_READERS_AMOUNT);
        preparedStatement.setDate(1, start);
        preparedStatement.setDate(2, finish);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            amount = resultSet.getInt(1);
        }

        return amount;
    }
}
