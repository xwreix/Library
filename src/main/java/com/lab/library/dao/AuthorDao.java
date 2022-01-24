package com.lab.library.dao;

import com.lab.library.beans.Author;
import com.lab.library.beans.Book;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorDao {
    public final String SELECT_AUTHOR_ID = "SELECT id, name FROM author WHERE name = ?";
    public final String INSERT_AUTHOR = "INSERT INTO author(name) VALUES (?) RETURNING id";
    public final String INSERT_AUTHOR_BOOK = "INSERT INTO authorsBooks(authorId, bookId) VALUES (?, ?)";
    public final String INSERT_PHOTO = "INSERT INTO authorPhoto (authorId, img) VALUES (?, ?)";

    public int selectAuthorId(Connection connection, Author author) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_AUTHOR_ID);
        preparedStatement.setString(1, author.getName());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        } else return 0;
    }

    public int insertAuthor(Connection connection, Author author) throws SQLException {
        int id = 0;
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_AUTHOR);
        preparedStatement.setString(1, author.getName());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        return id;
    }

    public void insertAuthorBook(Connection connection, Author author, Book book) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_AUTHOR_BOOK);
        preparedStatement.setInt(1, author.getId());
        preparedStatement.setInt(2, book.getId());
        preparedStatement.executeUpdate();
    }

    public void insertPhotos(Connection connection, int authorId, InputStream photo) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PHOTO);
        preparedStatement.setInt(1, authorId);
        preparedStatement.setBinaryStream(2, photo);
        preparedStatement.executeUpdate();

    }

}
