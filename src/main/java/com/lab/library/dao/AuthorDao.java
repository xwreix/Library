package com.lab.library.dao;

import com.lab.library.beans.Author;
import com.lab.library.beans.Book;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorDao {
    public static final String SELECT_AUTHOR_ID = "SELECT id, name FROM author WHERE name = ?";
    public static final String INSERT_AUTHOR = "INSERT INTO author(name) VALUES (?) RETURNING id";
    public static final String INSERT_AUTHOR_BOOK = "INSERT INTO authorsBooks(authorId, bookId) VALUES (?, ?)";
    public static final String INSERT_PHOTO = "INSERT INTO authorPhoto (authorId, img) VALUES (?, ?)";

    public static ResultSet selectAuthorId(Connection connection, Author author) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_AUTHOR_ID);
        preparedStatement.setString(1, author.getName());
        return preparedStatement.executeQuery();
    }

    public static ResultSet insertAuthor(Connection connection, Author author) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_AUTHOR);
        preparedStatement.setString(1, author.getName());
        return preparedStatement.executeQuery();
    }

    public static void insertAuthorBook(Connection connection, Author author, Book book) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_AUTHOR_BOOK);
        preparedStatement.setInt(1, author.getId());
        preparedStatement.setInt(2, book.getId());
        preparedStatement.executeUpdate();
    }

    public static void insertPhotos(Connection connection, int authorId, InputStream photo) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PHOTO);
        preparedStatement.setInt(1, authorId);
        preparedStatement.setBinaryStream(2, photo);
        preparedStatement.executeUpdate();

    }

}
