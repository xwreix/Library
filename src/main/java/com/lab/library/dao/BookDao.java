package com.lab.library.dao;

import com.lab.library.beans.Book;

import java.io.InputStream;
import java.sql.*;

public class BookDao {
    private static final String INSERT_INTO_BOOK = "INSERT INTO book(nameInRus, originalName, cost," +
            " priceForDay, publYear, pageAmount) " +
            "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
    private static final String INSERT_INTO_COVER = "INSERT INTO cover (bookId, img) VALUES (?, ?)";
    private static final String INSERT_INTO_GENRE = "INSERT INTO BooksGenres (genreId, bookId) VALUES (?, ?)";
    private static final String INSERT_BOOK_COPY = "INSERT INTO bookCopy(bookId, registrDate) VALUES (?, ?)";
    private static final String SELECT_GENRES = "SELECT id, name FROM genre";
    private static final String SELECT_FROM_BOOK = "SELECT id, nameInRus, publYear from book";
    private static final String SELECT_TOTAL = "SELECT COUNT(*) FROM bookcopy WHERE bookId = ?";
    private static final String SELECT_NOT_AVALABLE = "select count(*) from issue\n" +
            " join bookcopy on bookcopy.id = issue.bookcopyid\n" +
            " where bookcopy.bookId = ? AND returnDate is NULL";
    private static final String SELECT_BOOK_GENRES = "select name from genre\n" +
            "join booksgenres on booksgenres.genreId = genre.id\n" +
            "where bookId = ?";
    private static final String SELECT_BOOK_ID = "SELECT id FROM book WHERE nameInRus = ?";
    private static final String SELECT_COST = "SELECT priceForDay FROM book WHERE nameInRus = ?";

    public static ResultSet insertIntoBook(Connection connection, Book book) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_INTO_BOOK);
        statement.setString(1, book.getNameInRus());
        statement.setString(2, book.getOriginalName());
        statement.setDouble(3, book.getCost());
        statement.setDouble(4, book.getPriceForDay());

        if (book.getYear() == null) {
            statement.setNull(5, Types.INTEGER);
        } else {
            statement.setInt(5, book.getYear());
        }

        if (book.getPageAmount() == null) {
            statement.setNull(6, Types.INTEGER);
        } else {
            statement.setInt(6, book.getPageAmount());
        }

        return statement.executeQuery();
    }

    public static void insertIntoCover(Connection connection, Book book) throws SQLException {
        for (InputStream cover : book.getCovers()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_INTO_COVER);
            statement.setInt(1, book.getId());
            statement.setBinaryStream(2, cover);
            statement.executeUpdate();
        }
    }

    public static void insertIntoGenre(Connection connection, Book book) throws SQLException {
        for (int genre : book.getGenresId()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_INTO_GENRE);
            statement.setInt(1, genre);
            statement.setInt(2, book.getId());
            statement.executeUpdate();
        }
    }

    public static void insertIntoBookCopy(Connection connection, Book book) throws SQLException {
        for (int i = 0; i < book.getTotalAmount(); i++) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOK_COPY);
            preparedStatement.setInt(1, book.getId());
            preparedStatement.setDate(2, book.getRegistrDate());
            preparedStatement.executeUpdate();
        }
    }

    public static ResultSet selectGenres(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GENRES);
        return preparedStatement.executeQuery();
    }

    public static ResultSet selectFromBook(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_BOOK);
        return preparedStatement.executeQuery();
    }

    public static ResultSet selectTotal(Connection connection, int id) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TOTAL);
        preparedStatement.setInt(1, id);
        return  preparedStatement.executeQuery();
    }

    public static ResultSet selectNotAvailable(Connection connection,int id) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NOT_AVALABLE);
        preparedStatement.setInt(1, id);
        return  preparedStatement.executeQuery();
    }

    public static ResultSet selectBookGenres(Connection connection, Book book) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_GENRES);
        preparedStatement.setInt(1, book.getId());
        return  preparedStatement.executeQuery();
    }

    public static ResultSet selectBookId(Connection connection, String name) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_ID);
        preparedStatement.setString(1, name);
        return preparedStatement.executeQuery();
    }

    public static ResultSet selectCost(Connection connection, String name) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COST);
        preparedStatement.setString(1, name);
        return preparedStatement.executeQuery();
    }
}
