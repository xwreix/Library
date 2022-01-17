package com.lab.library.dao;

import com.lab.library.beans.Book;
import com.lab.library.beans.BookCopy;
import com.lab.library.beans.Status;
import com.lab.library.dao.DBService.CheckElement;
import com.lab.library.dao.DBService.InsertIntoDb;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class manager {

    public manager(){    }

    public Map<Integer, String> selectFromGenres(){
        Connection connection = ConnectionPool.getConnection();
        Map<Integer, String> result = GetFromDB.setGenres(connection);
        ConnectionPool.releaseConnection(connection);

        return result;
    }

    public List<Book> selectFromBook(){
        Connection connection = ConnectionPool.getConnection();
        List<Book> result = GetFromDB.setBooks(connection);
        ConnectionPool.releaseConnection(connection);

        return result;
    }

    public boolean addBook(HttpServletRequest request){
        Connection connection = ConnectionPool.getConnection();
        boolean result = InsertIntoDb.addBook(request, connection);
        ConnectionPool.releaseConnection(connection);

        return result;
    }

    public Status checkBook(String bookName){
        Connection connection = ConnectionPool.getConnection();
        Status result = CheckElement.checkBook(bookName, connection);
        ConnectionPool.releaseConnection(connection);

        return result;
    }

    public double getCost(String bookName){
        Connection connection = ConnectionPool.getConnection();
        double result = GetFromDB.getCost(bookName, connection);
        ConnectionPool.releaseConnection(connection);

        return result;
    }

    public List<BookCopy> addIssue(HttpServletRequest request){
        Connection connection = ConnectionPool.getConnection();
        List<BookCopy> result = InsertIntoDb.addIssue(request, connection);
        ConnectionPool.releaseConnection(connection);

        return result;
    }

    public int countReaderBooks(String email){
        Connection connection = ConnectionPool.getConnection();
        int result = CheckElement.countGivenBooks(email, connection);
        ConnectionPool.releaseConnection(connection);

        return result;
    }

    public List<BookCopy> getReaderBooks(String email){
        Connection connection = ConnectionPool.getConnection();
        List<BookCopy> result = GetFromDB.getReaderBooks(email, connection);
        ConnectionPool.releaseConnection(connection);

        return result;
    }

    public boolean returnBook(HttpServletRequest request){
        Connection connection = ConnectionPool.getConnection();
        boolean result = InsertIntoDb.returnBook(request, connection);
        ConnectionPool.releaseConnection(connection);

        return result;
    }

}
