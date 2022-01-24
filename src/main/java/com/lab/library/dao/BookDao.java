package com.lab.library.dao;

import com.lab.library.beans.Book;
import com.lab.library.beans.BookCopy;
import com.lab.library.beans.PopularBook;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public int insertIntoBook(Connection connection, Book book) throws SQLException {
        int id = 0;
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

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }

        return id;
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

    public Map<Integer, String> selectGenres(Connection connection) throws SQLException {
        Map<Integer, String> genres = new HashMap<>();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GENRES);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            genres.put(resultSet.getInt(1), resultSet.getString(2));
        }

        return genres;
    }

    public List<Book> selectFromBook(Connection connection) throws SQLException {
        List<Book> books = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_BOOK);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Book book = new Book();
            book.setId(resultSet.getInt("id"));
            book.setNameInRus(resultSet.getString("nameInRus"));
            book.setYear(resultSet.getInt("publYear"));
            books.add(book);
        }

        return books;
    }

    public int selectTotal(Connection connection, int id) throws SQLException {
        int result = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TOTAL);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            result = resultSet.getInt(1);
        }

        return result;
    }

    public int selectNotAvailable(Connection connection, int id) throws SQLException {
        int result = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NOT_AVALABLE);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            result = resultSet.getInt(1);
        }

        return result;
    }

    public List<String> selectBookGenres(Connection connection, Book book) throws SQLException {
        List<String> genres = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_GENRES);
        preparedStatement.setInt(1, book.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            genres.add(resultSet.getString("name"));
        }

        return genres;
    }

    public int selectBookId(Connection connection, String name) throws SQLException {
        int id = 0;
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_ID);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }

        return id;
    }

    public double selectCost(Connection connection, String name) throws SQLException {
        double result = 0;
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COST);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            result = resultSet.getDouble(1);
        }

        return result;
    }

    public List<PopularBook> selectPopular(Connection connection) throws SQLException {
        List<PopularBook> popular = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_POPULAR);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            PopularBook popularBook = new PopularBook();
            popularBook.setId(resultSet.getInt(1));
            popularBook.setAmount(resultSet.getInt(2));
            popular.add(popularBook);
        }

        return  popular;
    }

    public InputStream selectCover(Connection connection, int id) throws SQLException {
        InputStream cover = null;
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COVER);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            cover = resultSet.getBinaryStream(1);
        }

        return cover;
    }

    public double selectRating(Connection connection, int id) throws SQLException {
        double rating = 0;
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RATING);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            rating = resultSet.getDouble(1);
        }

        return rating;
    }

    public BookCopy selectCopyInfo(Connection connection, BookCopy copy) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COPY_INFO);
        preparedStatement.setInt(1, copy.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            copy.setName(resultSet.getString(1));
            copy.setDamage(resultSet.getString(2));
        }

        return copy;
    }

    public boolean isIssued(Connection connection, int id) throws SQLException {
        boolean result = false;
        PreparedStatement preparedStatement = connection.prepareStatement(IS_ISSUED);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            result = true;
        }

        return result;
    }

    public boolean isCopyExisting(Connection connection, int id) throws SQLException {
        boolean result = false;
        PreparedStatement preparedStatement = connection.prepareStatement(IS_COPY_EXISTING);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            result = true;
        }

        return result;
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
