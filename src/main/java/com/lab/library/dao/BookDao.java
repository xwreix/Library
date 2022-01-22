package com.lab.library.dao;

import com.lab.library.beans.Book;

import java.io.InputStream;
import java.sql.*;

public class BookDao {
    private final String INSERT_INTO_BOOK = "INSERT INTO book(nameInRus, originalName, cost," +
            " priceForDay, publYear, pageAmount) " +
            "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
    private final String INSERT_INTO_COVER = "INSERT INTO cover (bookId, img) VALUES (?, ?)";
    private final String INSERT_INTO_GENRE = "INSERT INTO BooksGenres (genreId, bookId) VALUES (?, ?)";
    private final String INSERT_BOOK_COPY = "INSERT INTO bookCopy(bookId, registrDate) VALUES (?, ?)";
    private final String SELECT_GENRES = "SELECT id, name FROM genre";
    private final String SELECT_FROM_BOOK = "SELECT id, nameInRus, publYear from book";
    private final String SELECT_TOTAL = "SELECT COUNT(*) FROM bookcopy WHERE bookId = ?";
    private final String SELECT_NOT_AVALABLE = "select count(*) from issue\n" +
            " join bookcopy on bookcopy.id = issue.bookcopyid\n" +
            " where bookcopy.bookId = ? AND returnDate is NULL";
    private final String SELECT_BOOK_GENRES = "select name from genre\n" +
            "join booksgenres on booksgenres.genreId = genre.id\n" +
            "where bookId = ?";
    private final String SELECT_BOOK_ID = "SELECT id FROM book WHERE nameInRus = ?";
    private final String SELECT_COST = "SELECT priceForDay FROM book WHERE nameInRus = ?";
    private final String SELECT_POPULAR = "SELECT book.id, COUNT(book.id) FROM issue\n" +
            "join bookcopy on bookcopy.id = issue.bookcopyid\n" +
            "join book on book.id = bookcopy.bookid\n" +
            "where returnDate > now() - interval '3 months'\n" +
            "GROUP BY book.id\n" +
            "order by count desc\n" +
            "limit 3";
    private final String SELECT_COVER = "SELECT img from cover where id=?";
    private final String SELECT_RATING = "select avg(rating) from issue\n" +
            "join bookcopy on bookcopy.id = issue.bookcopyid\n" +
            "join book on book.id = bookcopy.bookid\n" +
            "where book.id = ?";
    private final String SELECT_COPY_INFO = "select book.nameinRus, damage from bookcopy" +
            " join book on book.id = bookcopy.bookid" +
            " where bookcopy.id = ?";
    private final String IS_ISSUED = "select id from issue\n" +
            "where bookcopyid = ? and returnDate is null;";
    private final String IS_COPY_EXISTING = "select * from bookcopy where id = ?";
    private final String INSERT_ARCHIVE = "insert into archive(bookcopyid, damage, name) values (?, ?, ?)";
    private final String DELETE_COPY = "delete from bookcopy where id = ?";

    public ResultSet insertIntoBook(Connection connection, Book book) throws SQLException {
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

    public void insertIntoCover(Connection connection, int bookId, InputStream cover) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_INTO_COVER);
        statement.setInt(1, bookId);
        statement.setBinaryStream(2, cover);
        statement.executeUpdate();

    }

    public void insertIntoGenre(Connection connection, int bookId, int genre) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_INTO_GENRE);
        statement.setInt(1, genre);
        statement.setInt(2, bookId);
        statement.executeUpdate();

    }

    public void insertIntoBookCopy(Connection connection, Book book) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOK_COPY);
        preparedStatement.setInt(1, book.getId());
        preparedStatement.setDate(2, book.getRegistrDate());
        preparedStatement.executeUpdate();
    }

    public ResultSet selectGenres(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GENRES);
        return preparedStatement.executeQuery();
    }

    public ResultSet selectFromBook(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_BOOK);
        return preparedStatement.executeQuery();
    }

    public ResultSet selectTotal(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TOTAL);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeQuery();
    }

    public ResultSet selectNotAvailable(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NOT_AVALABLE);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeQuery();
    }

    public ResultSet selectBookGenres(Connection connection, Book book) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_GENRES);
        preparedStatement.setInt(1, book.getId());
        return preparedStatement.executeQuery();
    }

    public ResultSet selectBookId(Connection connection, String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_ID);
        preparedStatement.setString(1, name);
        return preparedStatement.executeQuery();
    }

    public ResultSet selectCost(Connection connection, String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COST);
        preparedStatement.setString(1, name);
        return preparedStatement.executeQuery();
    }

    public ResultSet selectPopular(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_POPULAR);
        return preparedStatement.executeQuery();
    }

    public ResultSet selectCover(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COVER);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeQuery();
    }

    public ResultSet selectRating(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RATING);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeQuery();
    }

    public ResultSet selectCopyInfo(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COPY_INFO);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeQuery();
    }

    public ResultSet isIssued(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(IS_ISSUED);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeQuery();
    }

    public ResultSet isCopyExisting(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(IS_COPY_EXISTING);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeQuery();
    }

    public void insertArchive(Connection connection, int id, String damage, String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ARCHIVE);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, damage);
        preparedStatement.setString(3, name);
        preparedStatement.executeUpdate();
    }

    public void deleteCopy(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COPY);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }
}
