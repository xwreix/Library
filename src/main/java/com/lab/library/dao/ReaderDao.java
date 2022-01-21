package com.lab.library.dao;

import com.lab.library.beans.Reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReaderDao {
    public static final String INSERT_READER = "INSERT INTO reader" +
            " (surname, name, patronymic, passportNumber, dateOfBirth, address, email)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String SELECT_READERS = "SELECT id, surname, name, dateOfBirth, " +
            "address, email FROM reader ORDER BY surname";
    public static final String SELECT_READER_ID = "SELECT id FROM reader WHERE email = ?";
    public static final String SELECT_NOT_RETURNED = "SELECT COUNT(*) FROM issue WHERE readerId = ? " +
            "and returnDate IS NULL";
    public static final String SELECT_ALL_BOOKS = "SELECT preliminarydate, discount, book.nameinrus, book.priceforday FROM issue\n" +
            "JOIN reader ON reader.id = issue.readerid\n" +
            "JOIN bookcopy ON bookcopy.id = issue.bookcopyid\n" +
            "JOIN book ON book.id = bookcopy.bookid\n" +
            "WHERE reader.email = ? AND returndate IS NULL";
    public static final String SELECT_OVERDUE = "select reader.email, (now()-preliminaryDate) from issue" +
            " join reader on reader.id = issue.readerId \n" +
            "where returnDate is NULL and preliminaryDate < now()";
    public static final String SELECT_OVERDUE_BOOKS = "select book.nameInRus, book.priceForDay from issue\n" +
            "join bookcopy on bookcopy.id = issue.bookcopyid\n" +
            "join book on book.id = bookcopy.bookid\n" +
            " join reader on reader.id = issue.readerid " +
            "where reader.email = ?";

    public static void insertReader(Connection connection, Reader reader) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_READER);

        preparedStatement.setString(1, reader.getSurname());
        preparedStatement.setString(2, reader.getName());
        preparedStatement.setString(3, reader.getPatronymic());
        preparedStatement.setString(4, reader.getPassportNumber());
        preparedStatement.setDate(5, reader.getDateOfBirth());
        preparedStatement.setString(6, reader.getAddress());
        preparedStatement.setString(7, reader.getEmail());

        preparedStatement.executeUpdate();
    }

    public static ResultSet selectReaders(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_READERS);

        return preparedStatement.executeQuery();
    }

    public static ResultSet selectReaderId(Connection connection, String email) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_READER_ID);
        preparedStatement.setString(1, email);

        return preparedStatement.executeQuery();
    }

    public static ResultSet selectNotReturned(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NOT_RETURNED);
        preparedStatement.setInt(1, id);

        return preparedStatement.executeQuery();
    }

    public static ResultSet selectAllBooks(Connection connection, String email) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BOOKS);
        preparedStatement.setString(1, email);
        return preparedStatement.executeQuery();
    }

    public static ResultSet selectOverdue(Connection connection) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_OVERDUE);
        return  preparedStatement.executeQuery();
    }

    public static ResultSet selectOverdueBooks(Connection connection, String email) throws  SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_OVERDUE_BOOKS);
        preparedStatement.setString(1, email);
        return  preparedStatement.executeQuery();
    }
}
