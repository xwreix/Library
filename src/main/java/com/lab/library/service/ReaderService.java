package com.lab.library.service;

import com.lab.library.beans.BookCopy;
import com.lab.library.beans.Reader;
import com.lab.library.beans.Status;
import com.lab.library.dao.ReaderDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReaderService {
    private static final Logger logger = Logger.getLogger(ReaderService.class.getName());

    public ReaderService() {
    }

    public boolean addReader(Reader reader, DataSource dataSource) {
        boolean result = true;

        try (Connection connection = dataSource.getConnection()) {
            ReaderDao.insertReader(connection, reader);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Insert reader exception ", e);
            result = false;
        }

        return result;
    }

    public List<Reader> selectReaders(DataSource dataSource) {
        List<Reader> readers = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()){
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

        return readers;
    }

    public Status checkReader(String email, DataSource dataSource) {
        Status status = null;

        try (Connection connection = dataSource.getConnection()){
            ResultSet resultSet = ReaderDao.selectReaderId(connection, email);

            if (!resultSet.next()) {
                status = Status.NON_EXISTENT;
            } else {
                int readerId = resultSet.getInt("id");
                resultSet = ReaderDao.selectNotReturned(connection, readerId);
                if (resultSet.next()) {
                    if (resultSet.getInt(1) == 0) {
                        status = Status.AVAIlABLE;
                    } else {
                        status = Status.LOCKED;
                    }
                }

            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Check reader exception", e);
        }

        return status;
    }

    public List<BookCopy> selectAllBooks(String email, DataSource dataSource) {
        List<BookCopy> result = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()){
            ResultSet resultSet = ReaderDao.selectAllBooks(connection, email);

            while (resultSet.next()) {
                String date = String.valueOf(resultSet.getDate("preliminaryDate"));
                int discount = resultSet.getInt("discount");
                String bookName = resultSet.getString(3);
                double priceForDay = resultSet.getDouble(4);

                result.add(new BookCopy(bookName, discount, date, priceForDay));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Selecting all books exception", e);
        }

        return result;
    }

}
