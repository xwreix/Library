package com.lab.library.dao;

import com.lab.library.beans.Book;
import com.lab.library.beans.BookCopy;
import com.lab.library.beans.Reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReaderDao {
    public final String INSERT_READER = "INSERT INTO reader" +
            " (surname, name, patronymic, passportNumber, dateOfBirth, address, email)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";
    public final String SELECT_READERS = "SELECT id, surname, name, dateOfBirth, " +
            "address, email FROM reader ORDER BY surname";
    public final String SELECT_READER_ID = "SELECT id FROM reader WHERE email = ?";
    public final String SELECT_NOT_RETURNED = "SELECT COUNT(*) FROM issue WHERE readerId = ? " +
            "and returnDate IS NULL";
    public final String SELECT_ALL_BOOKS = "SELECT preliminarydate, discount, book.nameinrus, book.priceforday FROM issue\n" +
            "JOIN reader ON reader.id = issue.readerid\n" +
            "JOIN bookcopy ON bookcopy.id = issue.bookcopyid\n" +
            "JOIN book ON book.id = bookcopy.bookid\n" +
            "WHERE reader.email = ? AND returndate IS NULL";
    public final String SELECT_OVERDUE = "select reader.email, (now()-preliminaryDate) from issue" +
            " join reader on reader.id = issue.readerId \n" +
            "where returnDate is NULL and preliminaryDate < now()";
    public final String SELECT_OVERDUE_BOOKS = "select book.nameInRus, book.priceForDay from issue\n" +
            "join bookcopy on bookcopy.id = issue.bookcopyid\n" +
            "join book on book.id = bookcopy.bookid\n" +
            " join reader on reader.id = issue.readerid " +
            "where reader.email = ?";

    public void insertReader(Connection connection, Reader reader) throws SQLException {
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

    public List<Reader> selectReaders(Connection connection) throws SQLException {
        List<Reader> readers = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_READERS);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            readers.add(new Reader(resultSet.getInt("id"),
                    resultSet.getString("surname"),
                    resultSet.getString("name"),
                    resultSet.getDate("dateOfBirth"),
                    resultSet.getString("address"),
                    resultSet.getString("email")));
        }

        return readers;
    }

    public int selectReaderId(Connection connection, String email) throws SQLException {
        int id = 0;
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_READER_ID);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }

        return id;
    }

    public int selectNotReturned(Connection connection, int id) throws SQLException {
        int amount = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NOT_RETURNED);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            amount = resultSet.getInt(1);
        }

        return amount;
    }

    public List<BookCopy> selectAllBooks(Connection connection, String email) throws SQLException {
        List<BookCopy> result = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BOOKS);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            result.add(new BookCopy(resultSet.getString(3), resultSet.getInt("discount"),
                    String.valueOf(resultSet.getDate("preliminaryDate")), resultSet.getDouble(4)));
        }

        return result;
    }

    public HashMap<String, String> selectOverdue(Connection connection) throws SQLException {
        HashMap<String, String> result = new HashMap<>();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_OVERDUE);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            result.put(resultSet.getString(1), resultSet.getString(2));
        }

        return result;
    }

    public List<Book> selectOverdueBooks(Connection connection, String email) throws SQLException {
        List<Book> books = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_OVERDUE_BOOKS);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            Book book = new Book();
            book.setNameInRus(resultSet.getString(1));
            book.setPriceForDay(resultSet.getDouble(2));
            books.add(book);
        }

        return books;
    }
}
