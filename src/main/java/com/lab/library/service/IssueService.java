package com.lab.library.service;

import com.lab.library.beans.BookCopy;
import com.lab.library.beans.Issue;
import com.lab.library.dao.IssueDao;
import com.lab.library.dao.ReaderDao;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IssueService {
    private static final Logger logger = Logger.getLogger(IssueService.class.getName());

    public IssueService() {
    }

    public List<BookCopy> addNewIssue(Issue issue, DataSource dataSource) {
        List<BookCopy> givenBooks = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);

            ResultSet resultSet = ReaderDao.selectReaderId(connection, issue.getReaderEmail());
            if(resultSet.next()){
                issue.setReaderId(resultSet.getInt(1));
            }

            for (String book : issue.getBooks()) {
                BookCopy bookCopy = new BookCopy();
                resultSet = IssueDao.selectBookCopy(connection, book);
                if(resultSet.next()){
                    bookCopy.setId(resultSet.getInt(1));
                    bookCopy.setDamage(resultSet.getString(2));
                }
                bookCopy.setDiscount(issue.getDiscount());
                bookCopy.setName(book);

                IssueDao.insertIssue(connection, issue, bookCopy.getId());

                givenBooks.add(bookCopy);
            }

            connection.commit();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Adding new issue exception ", e);
        }

        return givenBooks;
    }

    public boolean finishIssue(Issue issue, DataSource dataSource) {
        boolean result = true;

        try (Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);

            ResultSet resultSet = ReaderDao.selectReaderId(connection, issue.getReaderEmail());
            if(resultSet.next()){
                issue.setReaderId(resultSet.getInt(1));
            }

            for (BookCopy book : issue.getReturned()) {
                resultSet = IssueDao.selectTakenCopy(connection, book.getName(), issue.getReaderId());
                if(resultSet.next()){
                    book.setId(resultSet.getInt(1));
                }

                resultSet = IssueDao.selectDamage(connection, book.getId());
                String existingDamage = "";
                if(resultSet.next()){
                    existingDamage = resultSet.getString(1);
                }

                if (existingDamage != null) {
                    book.setDamage(existingDamage + " " + book.getDamage());
                }

                IssueDao.insertDamage(connection, book);

                if(book.getDamagePhotos() != null){
                    for(InputStream photo: book.getDamagePhotos()){
                        IssueDao.insertDamagePhoto(connection, photo, book.getId());
                    }
                }


                IssueDao.updateIssue(connection, issue, book);
            }

            connection.commit();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Finishing issue exception ", e);
            result = false;
        }

        return result;
    }
}
