package com.lab.library.service;

import com.lab.library.beans.BookCopy;
import com.lab.library.beans.Reader;
import com.lab.library.beans.Status;
import com.lab.library.dao.ConnectionPool;
import com.lab.library.dao.ReaderDao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReaderService {
    Logger logger;

    public ReaderService() {
        logger = Logger.getLogger(ReaderService.class.getName());
        try {
            logger.addHandler(new FileHandler("libraryLog"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Log exception ", e);
        }
    }

    public boolean addReader(Reader reader) {

        boolean result = true;
        Connection connection = ConnectionPool.getConnection();
        try {
            ReaderDao.insertReader(connection, reader);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Insert reader exception ", e);
            result = false;
        }

        ConnectionPool.releaseConnection(connection);
        return result;
    }

    public List<Reader> selectReaders() {
        Connection connection = ConnectionPool.getConnection();
        List<Reader> readers = new ArrayList<>();

        try {
            ResultSet resultSet = ReaderDao.selectReaders(connection);

            while (resultSet.next()) {
                readers.add(new Reader(resultSet.getInt("id"),
                        resultSet.getString("surname"),
                        resultSet.getString("name"),
                        resultSet.getDate("dateOfBirth"),
                        resultSet.getString("address"),
                        resultSet.getString("email")));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Select reader exception", e);
        }

        ConnectionPool.releaseConnection(connection);
        return readers;
    }

    public Status checkReader(String email) {
        Connection connection = ConnectionPool.getConnection();
        Status status = null;

        try {
            ResultSet resultSet = ReaderDao.selectReaderId(connection, email);

            if (!resultSet.next()) {
                status = Status.NON_EXISTENT;
            } else {
                int readerId = resultSet.getInt("id");
                resultSet = ReaderDao.selectNotReturned(connection, readerId);
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    status = Status.AVAIlABLE;
                } else {
                    status = Status.LOCKED;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Check reader exception", e);
        }

        ConnectionPool.releaseConnection(connection);
        return status;
    }

    public List<BookCopy> selectAllBooks(String email){
        Connection connection = ConnectionPool.getConnection();
        List<BookCopy> result = new ArrayList<>();

        try {
            ResultSet resultSet = ReaderDao.selectAllBooks(connection, email);

            while(resultSet.next()){
                String date = String.valueOf(resultSet.getDate("preliminaryDate"));
                int discount = resultSet.getInt("discount");
                String bookName = resultSet.getString(3);
                double priceForDay = resultSet.getDouble(4);

                result.add(new BookCopy(bookName, discount, date, priceForDay));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Selecting all books exception", e);
        }

        ConnectionPool.releaseConnection(connection);
        return result;
    }
}
