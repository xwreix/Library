package com.lab.library.service;

import com.lab.library.beans.*;
import com.lab.library.dao.AuthorDao;
import com.lab.library.dao.BookDao;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookService {
    private static final Logger logger = Logger.getLogger(BookService.class.getName());

    public BookService() {
    }

    public boolean addBook(Book book, DataSource dataSource) {
        boolean result = true;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            ResultSet resultSet = BookDao.insertIntoBook(connection, book);
            if (resultSet.next()) {
                book.setId(resultSet.getInt(1));
            }

            for (InputStream cover : book.getCovers()) {
                BookDao.insertIntoCover(connection, book.getId(), cover);
            }

            for (int genre : book.getGenresId()) {
                BookDao.insertIntoGenre(connection, book.getId(), genre);
            }

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
                for (InputStream photo : author.getPhotos()) {
                    AuthorDao.insertPhotos(connection, author.getId(), photo);
                }
            }

            for (int i = 0; i < book.getTotalAmount(); i++) {
                BookDao.insertIntoBookCopy(connection, book);
            }


            connection.commit();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Adding book exception", e);
            result = false;
        }

        return result;
    }

    public Map<Integer, String> selectGenres(DataSource dataSource) {
        Map<Integer, String> genres = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = BookDao.selectGenres(connection);

            while (resultSet.next()) {
                genres.put(resultSet.getInt(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Selecting genres exception", e);
        }

        return genres;
    }

    public List<Book> selectBooks(DataSource dataSource) {
        List<Book> books = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
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

        Collections.sort(books);
        return books;
    }

    public Status checkBook(String name, DataSource dataSource) {
        Status status = null;

        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = BookDao.selectBookId(connection, name);
            int id = 0;
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }


            resultSet = BookDao.selectTotal(connection, id);
            int total = 0;
            if (resultSet.next()) {
                total = resultSet.getInt(1);
            }

            if (total == 0) {
                status = Status.NON_EXISTENT;
            } else {
                resultSet = BookDao.selectNotAvailable(connection, id);
                int available = 0;
                if (resultSet.next()) {
                    available = total - resultSet.getInt(1);
                }

                if (available > 0) {
                    status = Status.AVAIlABLE;
                } else {
                    status = Status.LOCKED;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Checking books exception", e);
        }

        return status;
    }

    public double selectCost(String name, DataSource dataSource) {
        double result = 0;

        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = BookDao.selectCost(connection, name);
            if (resultSet.next()) {
                result = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Selecting cost exception", e);
        }

        return result;
    }

    public List<PopularBook> selectPopular(DataSource dataSource) {
        List<PopularBook> popular = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = BookDao.selectPopular(connection);
            while (resultSet.next()) {
                PopularBook popularBook = new PopularBook();
                int bookId = resultSet.getInt(1);
                popularBook.setAmount(resultSet.getInt(2));

                ResultSet result = BookDao.selectCover(connection, bookId);
                while (result.next()) {
                    InputStream inputStream = result.getBinaryStream(1);
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
                }

                result = BookDao.selectRating(connection, bookId);
                if (result.next()) {
                    popularBook.setRating(result.getDouble(1));
                }

                popular.add(popularBook);
            }
        } catch (SQLException | IOException e) {
            logger.log(Level.SEVERE, "Selecting popular books exception ", e);
        }

        return popular;
    }

    public BookCopy selectCopyInfo(int id, DataSource dataSource) {
        BookCopy bookCopy = new BookCopy();

        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = BookDao.isCopyExisting(connection, id);
            if (resultSet.next()) {
                resultSet = BookDao.isIssued(connection, id);
                if (!resultSet.next()) {
                    resultSet = BookDao.selectCopyInfo(connection, id);
                    bookCopy.setId(id);
                    if (resultSet.next()) {
                        bookCopy.setName(resultSet.getString(1));
                        bookCopy.setDamage(resultSet.getString(2));
                    }


                } else {
                    bookCopy.setId(0);
                }
            } else {
                bookCopy.setId(-1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Selecting copy info exception ", e);
        }

        return bookCopy;
    }

    public boolean writeOff(int id, DataSource dataSource){
        boolean result = true;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            ResultSet resultSet = BookDao.selectCopyInfo(connection, id);
            if(resultSet.next()){
                BookDao.insertArchive(connection, id, resultSet.getString(2), resultSet.getString(1));
            }
            BookDao.deleteCopy(connection, id);
            connection.commit();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Writing off exception ", e);
            result = false;
        }

        return result;
    }
}
