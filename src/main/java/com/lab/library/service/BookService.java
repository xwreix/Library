package com.lab.library.service;

import com.lab.library.beans.Author;
import com.lab.library.beans.Book;
import com.lab.library.beans.Status;
import com.lab.library.dao.AuthorDao;
import com.lab.library.dao.BookDao;
import com.lab.library.dao.ConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookService {
    private Logger logger;

    public BookService() {
        logger = Logger.getLogger(BookService.class.getName());
        try {
            logger.addHandler(new FileHandler("libraryLog"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Log exception ", e);
        }
    }

    public boolean addBook(Book book) {
        Connection connection = ConnectionPool.getConnection();
        boolean result = true;

        try {
            connection.setAutoCommit(false);

            ResultSet resultSet = BookDao.insertIntoBook(connection, book);
            resultSet.next();
            book.setId(resultSet.getInt(1));

            BookDao.insertIntoCover(connection, book);
            BookDao.insertIntoGenre(connection, book);

            for (Author author : book.getAuthors()) {
                resultSet = AuthorDao.selectAuthorId(connection, author);

                int authorId = 0;
                if (!resultSet.next()) {
                    resultSet = AuthorDao.insertAuthor(connection, author);
                    resultSet.next();
                }
                authorId = resultSet.getInt(1);
                author.setId(authorId);

                AuthorDao.insertAuthorBook(connection, author, book);
                AuthorDao.insertPhotos(connection, author);
            }

            BookDao.insertIntoBookCopy(connection, book);

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Rollback exception", e);
            }
            logger.log(Level.SEVERE, "Adding book exception", e);
            result = false;
        }


        ConnectionPool.releaseConnection(connection);
        return result;
    }

    public Map<Integer, String> selectGenres() {
        Connection connection = ConnectionPool.getConnection();
        Map<Integer, String> genres = new HashMap<>();

        try {
            ResultSet resultSet = BookDao.selectGenres(connection);

            while (resultSet.next()) {
                genres.put(resultSet.getInt(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Selecting genres exception", e);
        }

        ConnectionPool.releaseConnection(connection);
        return genres;
    }

    public List<Book> selectBooks() {
        Connection connection = ConnectionPool.getConnection();
        List<Book> books = new ArrayList<>();

        try {
            ResultSet resultSet = BookDao.selectFromBook(connection);

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setNameInRus(resultSet.getString("nameInRus"));
                book.setYear(resultSet.getInt("publYear"));

                ResultSet set = BookDao.selectTotal(connection, book.getId());
                set.next();
                book.setTotalAmount(set.getInt(1));

                set = BookDao.selectNotAvailable(connection, book.getId());
                set.next();
                book.setAvailableAmount(book.getTotalAmount() - set.getInt(1));

                List<String> genres = new ArrayList<>();
                set = BookDao.selectBookGenres(connection, book);
                while (set.next()) {
                    genres.add(set.getString("name"));
                }
                book.setGenres(genres);

                books.add(book);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Selecting books exception", e);
        }

        ConnectionPool.releaseConnection(connection);
        Collections.sort(books);
        return books;
    }

    public Status checkBook(String name) {
        Connection connection = ConnectionPool.getConnection();
        Status status = null;

        try {
            ResultSet resultSet = BookDao.selectBookId(connection, name);
            resultSet.next();
            int id = resultSet.getInt(1);

            resultSet = BookDao.selectTotal(connection, id);
            resultSet.next();
            int total = resultSet.getInt(1);

            if (total == 0) {
                status = Status.NON_EXISTENT;
            } else {
                resultSet = BookDao.selectNotAvailable(connection, id);
                resultSet.next();
                int available = total - resultSet.getInt(1);

                if (available > 0) {
                    status = Status.AVAIlABLE;
                } else {
                    status = Status.LOCKED;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Checking books exception", e);
        }

        ConnectionPool.releaseConnection(connection);
        return status;
    }

    public double selectCost(String name) {
        Connection connection = ConnectionPool.getConnection();
        double result = 0;

        try {
            ResultSet resultSet = BookDao.selectCost(connection, name);
            resultSet.next();
            result = resultSet.getDouble(1);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Selecting cost exception", e);
        }

        ConnectionPool.releaseConnection(connection);
        return result;
    }

}
