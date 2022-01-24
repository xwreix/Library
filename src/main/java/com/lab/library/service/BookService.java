package com.lab.library.service;

import com.lab.library.beans.*;
import com.lab.library.dao.AuthorDao;
import com.lab.library.dao.BookDao;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BookService {
    private final AuthorDao authorDao = new AuthorDao();
    private final BookDao bookDao = new BookDao();

    public BookService() {
    }

    public void addBook(Book book, DataSource dataSource) throws SQLException {

        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        book.setId(bookDao.insertIntoBook(connection, book));

        for (InputStream cover : book.getCovers()) {
            bookDao.insertIntoCover(connection, book.getId(), cover);
        }

        for (int genre : book.getGenresId()) {
            bookDao.insertIntoGenre(connection, book.getId(), genre);
        }

        for (Author author : book.getAuthors()) {
            int authorId = authorDao.selectAuthorId(connection, author);

            if (authorId == 0) {
                authorId = authorDao.insertAuthor(connection, author);
            }
            author.setId(authorId);

            authorDao.insertAuthorBook(connection, author, book);
            for (InputStream photo : author.getPhotos()) {
                authorDao.insertPhotos(connection, author.getId(), photo);
            }
        }

        for (int i = 0; i < book.getTotalAmount(); i++) {
            bookDao.insertIntoBookCopy(connection, book);
        }

        connection.commit();
        connection.close();
    }

    public Map<Integer, String> selectGenres(DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();

        Map<Integer, String> genres = bookDao.selectGenres(connection);

        connection.close();
        return genres;
    }

    public List<Book> selectBooks(DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();

        List<Book> books = bookDao.selectFromBook(connection);

        for (Book book : books) {
            book.setTotalAmount(bookDao.selectTotal(connection, book.getId()));

            book.setAvailableAmount(book.getTotalAmount() - bookDao.selectNotAvailable(connection, book.getId()));

            book.setGenres(bookDao.selectBookGenres(connection, book));
        }

        Collections.sort(books);
        connection.close();
        return books;
    }

    public Status checkBook(String name, DataSource dataSource) throws SQLException {
        Status status;

        Connection connection = dataSource.getConnection();
        int id = bookDao.selectBookId(connection, name);

        int total = bookDao.selectTotal(connection, id);

        if (total == 0) {
            status = Status.NON_EXISTENT;
        } else {
            int available = total - bookDao.selectNotAvailable(connection, id);

            if (available > 0) {
                status = Status.AVAIlABLE;
            } else {
                status = Status.LOCKED;
            }
        }

        connection.close();
        return status;
    }

    public double selectCost(String name, DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();

        double result = bookDao.selectCost(connection, name);

        connection.close();
        return result;
    }

    public List<PopularBook> selectPopular(DataSource dataSource) throws SQLException, IOException {
        Connection connection = dataSource.getConnection();

        List<PopularBook> popular = bookDao.selectPopular(connection);

        for (PopularBook popularBook : popular) {

            InputStream inputStream = bookDao.selectCover(connection, popularBook.getId());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = outputStream.toByteArray();

            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            inputStream.close();
            outputStream.close();
            popularBook.setBase64Image(base64Image);

            popularBook.setRating(bookDao.selectRating(connection, popularBook.getId()));

        }

        connection.close();
        return popular;
    }

    public BookCopy selectCopyInfo(int id, DataSource dataSource) throws SQLException {
        BookCopy bookCopy = new BookCopy();

        Connection connection = dataSource.getConnection();
        if (bookDao.isCopyExisting(connection, id)) {
            if (!bookDao.isIssued(connection, id)) {
                bookCopy.setId(id);
                bookCopy = bookDao.selectCopyInfo(connection, bookCopy);

            } else {
                bookCopy.setId(0);
            }
        } else {
            bookCopy.setId(-1);
        }

        connection.close();
        return bookCopy;
    }

    public void writeOff(int id, DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        BookCopy bookCopy = new BookCopy();
        bookCopy.setId(id);
        bookCopy = bookDao.selectCopyInfo(connection, bookCopy);
        bookDao.insertArchive(connection, bookCopy.getId(), bookCopy.getDamage(), bookCopy.getName());
        bookDao.deleteCopy(connection, bookCopy.getId());

        connection.commit();
        connection.close();
    }
}
