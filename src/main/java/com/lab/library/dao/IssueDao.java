package com.lab.library.dao;

import com.lab.library.beans.BookCopy;
import com.lab.library.beans.Issue;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IssueDao {
    public final String INSERT_ISSUE = "INSERT INTO issue(readerID, bookCopyId, preliminaryDate, discount)" +
            " VALUES (?, ?, ?, ?) ";
    public final String SELECT_BOOK_COPY = "SELECT bookcopy.id, damage  FROM bookCopy " +
            "JOIN book on book.id = bookCopy.bookId" +
            " LEFT JOIN issue on bookCopy.id = issue.bookcopyid" +
            " WHERE (nameInRus = ?) AND (issue.id IS NULL OR returnDate IS NOT NULl)" +
            "LIMIT 1";
    public final String SELECT_TAKEN_COPY = "SELECT bookcopyID FROM issue\n" +
            "JOIN bookCopy on bookCopy.id = issue.bookCopyId\n" +
            "JOIN book on book.id = bookCopy.bookId\n" +
            "WHERE readerId = ? AND book.nameinrus = ?;\n";
    public final String SELECT_DAMAGE = "SELECT damage FROM bookCopy WHERE id = ?";
    public final String INSERT_DAMAGE = "UPDATE  bookCopy SET damage = ? WHERE id = ?;";
    public final String INSERT_DAMAGE_PHOTO = "INSERT INTO damagePhoto(img, copyid) VALUES (?, ?)";
    public final String UPDATE_ISSUE = "UPDATE issue SET returnDate = ?, rating = ?\n" +
            "WHERE readerId=? AND bookcopyid=? AND returnDate IS NULL;";
    public final String INSERT_PAYMENT = "insert into payment(readerId, paymDate, amount) values (?, ?, ?)";

    public void insertIssue(Connection connection, Issue issue, int copyId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ISSUE);
        preparedStatement.setInt(1, issue.getReaderId());
        preparedStatement.setInt(2, copyId);
        preparedStatement.setDate(3, issue.getDate());
        preparedStatement.setInt(4, issue.getDiscount());
        preparedStatement.executeUpdate();
    }

    public BookCopy selectBookCopy(Connection connection, String bookName) throws SQLException {
        BookCopy bookCopy = new BookCopy();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_COPY);
        preparedStatement.setString(1, bookName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            bookCopy.setId(resultSet.getInt(1));
            bookCopy.setDamage(resultSet.getString(2));
        }

        return bookCopy;
    }

    public int selectTakenCopy(Connection connection, String bookName, int readerId) throws SQLException {
        int id = 0;
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TAKEN_COPY);
        preparedStatement.setInt(1, readerId);
        preparedStatement.setString(2, bookName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }

        return id;
    }

    public String selectDamage(Connection connection, int copyId) throws SQLException {
        String damage = "";
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DAMAGE);
        preparedStatement.setInt(1, copyId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            damage = resultSet.getString(1);
        }

        return damage;
    }

    public void insertDamage(Connection connection, BookCopy bookCopy) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DAMAGE);
        preparedStatement.setString(1, bookCopy.getDamage());
        preparedStatement.setInt(2, bookCopy.getId());
        preparedStatement.executeUpdate();
    }

    public void insertDamagePhoto(Connection connection, InputStream photo, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DAMAGE_PHOTO);
        preparedStatement.setBinaryStream(1, photo);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
    }

    public void updateIssue(Connection connection, Issue issue, BookCopy bookCopy) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ISSUE);
        preparedStatement.setDate(1, issue.getDate());
        preparedStatement.setInt(2, bookCopy.getRating());
        preparedStatement.setInt(3, issue.getReaderId());
        preparedStatement.setInt(4, bookCopy.getId());
        preparedStatement.executeUpdate();
    }

    public void insertPayment(Connection connection, Issue issue) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PAYMENT);
        preparedStatement.setInt(1, issue.getReaderId());
        preparedStatement.setDate(2, issue.getDate());
        preparedStatement.setDouble(3, issue.getCost());
        preparedStatement.executeUpdate();
    }


}
