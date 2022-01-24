package com.lab.library.service;

import com.lab.library.beans.Book;
import com.lab.library.beans.BookCopy;
import com.lab.library.beans.Reader;
import com.lab.library.beans.Status;
import com.lab.library.dao.ReaderDao;

import javax.mail.MessagingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReaderService {
    ReaderDao readerDao = new ReaderDao();

    public ReaderService() {
    }

    public void addReader(Reader reader, DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        readerDao.insertReader(connection, reader);

        connection.close();
    }

    public List<Reader> selectReaders(DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();

        List<Reader> readers = readerDao.selectReaders(connection);

        connection.close();
        return readers;
    }

    public Status checkReader(String email, DataSource dataSource) throws SQLException {
        Status status;

        Connection connection = dataSource.getConnection();
        int readerId = readerDao.selectReaderId(connection, email);

        if (readerId == 0) {
            status = Status.NON_EXISTENT;
        } else {
            if (readerDao.selectNotReturned(connection, readerId) == 0) {
                status = Status.AVAIlABLE;
            } else {
                status = Status.LOCKED;
            }
        }

        connection.close();
        return status;
    }

    public List<BookCopy> selectAllBooks(String email, DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();

        List<BookCopy> result = readerDao.selectAllBooks(connection, email);

        connection.close();
        return result;
    }

    public void sendMails(DataSource dataSource) throws SQLException, MessagingException, IOException {
        Connection connection = dataSource.getConnection();

        HashMap<String, String> overdue = readerDao.selectOverdue(connection);
        for (Map.Entry<String, String> entry : overdue.entrySet()) {
            SendMail sendMail = new SendMail();
            String email = entry.getKey();
            String days = entry.getValue();
            String[] parts = days.split(" ");
            int amount = Integer.parseInt(parts[0]);

            if (amount > 5) {
                StringBuilder message = new StringBuilder("Книги которые необходимо вернуть: ");
                double total = 0;
                List<Book> books = readerDao.selectOverdueBooks(connection, email);
                for (Book book : books) {
                    double cost = book.getPriceForDay();
                    total += cost * amount + (cost * amount) / 100;
                    message.append(book.getNameInRus()).append(" ");
                }
                message.append("Штраф: ").append(total);
                sendMail.send(message.toString(), email);
            } else if (amount == 0) {
                StringBuilder message = new StringBuilder("Книги которые необходимо вернуть: ");
                List<Book> books = readerDao.selectOverdueBooks(connection, email);
                for (Book book : books) {
                    message.append(book.getNameInRus()).append(" ");
                }
                sendMail.send(message.toString(), email);
            }
        }
        connection.close();
    }

}
