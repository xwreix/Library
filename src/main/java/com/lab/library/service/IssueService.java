package com.lab.library.service;

import com.lab.library.beans.BookCopy;
import com.lab.library.beans.Issue;
import com.lab.library.beans.Profitability;
import com.lab.library.dao.IssueDao;
import com.lab.library.dao.ProfitDao;
import com.lab.library.dao.ReaderDao;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IssueService {
    private final IssueDao issueDao = new IssueDao();
    private final ProfitDao profitDao = new ProfitDao();
    private final ReaderDao readerDao = new ReaderDao();

    public IssueService() {
    }

    public List<BookCopy> addNewIssue(Issue issue, DataSource dataSource) throws SQLException {
        List<BookCopy> givenBooks = new ArrayList<>();

        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        issue.setReaderId(readerDao.selectReaderId(connection, issue.getReaderEmail()));

        for (String book : issue.getBooks()) {
            BookCopy bookCopy = issueDao.selectBookCopy(connection, book);
            bookCopy.setDiscount(issue.getDiscount());
            bookCopy.setName(book);

            issueDao.insertIssue(connection, issue, bookCopy.getId());

            givenBooks.add(bookCopy);
        }

        connection.commit();
        connection.close();
        return givenBooks;
    }

    public void finishIssue(Issue issue, DataSource dataSource) throws SQLException {

        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        issue.setReaderId(readerDao.selectReaderId(connection, issue.getReaderEmail()));

        issueDao.insertPayment(connection, issue);

        for (BookCopy book : issue.getReturned()) {
            book.setId(issueDao.selectTakenCopy(connection, book.getName(), issue.getReaderId()));

            String existingDamage = issueDao.selectDamage(connection, book.getId());

            if (!Objects.equals(existingDamage, "")) {
                book.setDamage(existingDamage + " " + book.getDamage());
            }

            issueDao.insertDamage(connection, book);

            if (book.getDamagePhotos() != null) {
                for (InputStream photo : book.getDamagePhotos()) {
                    issueDao.insertDamagePhoto(connection, photo, book.getId());
                }
            }


            issueDao.updateIssue(connection, issue, book);
        }

        connection.commit();
        connection.close();
    }

    public Profitability calcProfit(Date start, Date finish, DataSource dataSource) throws SQLException {
        Profitability profitability = new Profitability();

        Connection connection = dataSource.getConnection();
        profitability.setBooksAmount(profitDao.selectBooksAmount(start, finish, connection));

        profitability.setRevenue(profitDao.selectPayments(start, finish, connection));

        profitability.setReadersAmount(profitDao.selectReadersAmount(start, finish, connection));

        connection.close();
        return profitability;
    }
}
