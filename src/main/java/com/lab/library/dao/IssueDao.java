package com.lab.library.dao;

import com.lab.library.beans.BookCopy;
import com.lab.library.beans.Issue;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IssueDao {
    public static final String INSERT_ISSUE = "INSERT INTO issue(readerID, bookCopyId, preliminaryDate, discount)" +
            " VALUES (?, ?, ?, ?) ";
    public static final String SELECT_BOOK_COPY = "SELECT bookcopy.id, damage  FROM bookCopy " +
            "JOIN book on book.id = bookCopy.bookId" +
            " LEFT JOIN issue on bookCopy.id = issue.bookcopyid" +
            " WHERE (nameInRus = ?) AND (issue.id IS NULL OR returnDate IS NOT NULl)" +
            "LIMIT 1";
    public static final String SELECT_TAKEN_COPY = "SELECT bookcopyID FROM issue\n" +
            "JOIN bookCopy on bookCopy.id = issue.bookCopyId\n" +
            "JOIN book on book.id = bookCopy.bookId\n" +
            "WHERE readerId = ? AND book.nameinrus = ?;\n";
    public static final String SELECT_DAMAGE = "SELECT damage FROM bookCopy WHERE id = ?";
    public static final String INSERT_DAMAGE =  "UPDATE  bookCopy SET damage = ? WHERE id = ?;";
    public static final String INSERT_DAMAGE_PHOTO =  "INSERT INTO damagePhoto(img, copyid) VALUES (?, ?)";
    public static final String UPDATE_ISSUE = "UPDATE issue SET returnDate = ?, rating = ?\n" +
            "WHERE readerId=? AND bookcopyid=? AND returnDate IS NULL;";
    public static final String INSERT_PAYMENT = "insert into payment(readerId, paymDate, amount) values (?, ?, ?)";

    public static void insertIssue(Connection connection, Issue issue, int copyId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ISSUE);
        preparedStatement.setInt(1, issue.getReaderId());
        preparedStatement.setInt(2, copyId);
        preparedStatement.setDate(3, issue.getDate());
        preparedStatement.setInt(4, issue.getDiscount());
        preparedStatement.executeUpdate();
    }

    public static ResultSet selectBookCopy(Connection connection, String bookName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_COPY);
        preparedStatement.setString(1, bookName);
        return preparedStatement.executeQuery();
    }

    public static ResultSet selectTakenCopy(Connection connection, String bookName, int readerId) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TAKEN_COPY);
        preparedStatement.setInt(1, readerId);
        preparedStatement.setString(2, bookName);
        return preparedStatement.executeQuery();
    }

    public static ResultSet selectDamage(Connection connection, int copyId) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DAMAGE);
        preparedStatement.setInt(1, copyId);
        return  preparedStatement.executeQuery();
    }

    public static void insertDamage(Connection connection, BookCopy bookCopy) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DAMAGE);
        preparedStatement.setString(1, bookCopy.getDamage());
        preparedStatement.setInt(2, bookCopy.getId());
        preparedStatement.executeUpdate();
    }

    public static void insertDamagePhoto(Connection connection, InputStream photo, int id) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DAMAGE_PHOTO);
        preparedStatement.setBinaryStream(1, photo);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
    }

    public static void updateIssue(Connection connection, Issue issue, BookCopy bookCopy) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ISSUE);
        preparedStatement.setDate(1, issue.getDate());
        preparedStatement.setInt(2, bookCopy.getRating());
        preparedStatement.setInt(3, issue.getReaderId());
        preparedStatement.setInt(4, bookCopy.getId());
        preparedStatement.executeUpdate();
    }

    public static void insertPayment(Connection connection, Issue issue) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PAYMENT);
        preparedStatement.setInt(1, issue.getReaderId());
        preparedStatement.setDate(2, issue.getDate());
        preparedStatement.setDouble(3, issue.getCost());
        preparedStatement.executeUpdate();
    }


}
