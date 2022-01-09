package com.lab.library.dao;

import com.lab.library.dao.DBService.GetFromDB;
import com.lab.library.dao.DBService.InsertIntoDb;
import com.lab.library.dao.beans.Book;
import com.lab.library.dao.beans.Reader;

import javax.servlet.http.HttpServletRequest;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DBManager {
    ConnectionPool connectionPool;

    public DBManager(){
        try {
            Properties props = new Properties();
            String dbProperties = "D:\\DBProperties\\dbInfo.properties";

            FileReader fileReader = new FileReader(dbProperties);
            props.load(fileReader);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            connectionPool = ConnectionPoolRealiz.create(url, user, password);
        } catch (SQLException | IOException e) {
            //TODO log
            e.printStackTrace();
        }
    }

    public Map<Integer, String> selectFromGenres(){
        Connection connection = connectionPool.getConnection();
        Map<Integer, String> result = GetFromDB.setGenres(connection);
        connectionPool.releaseConnection(connection);

        return result;
    }

    public List<Reader> selectFromReader(){
        Connection connection = connectionPool.getConnection();
        List<Reader> result = GetFromDB.setReaders(connection);
        connectionPool.releaseConnection(connection);

        return result;
    }

    public List<Book> selectFromBook(){
        Connection connection = connectionPool.getConnection();
        List<Book> result = GetFromDB.setBooks(connection);
        connectionPool.releaseConnection(connection);

        return result;
    }

    public boolean addReader(HttpServletRequest request){
        Connection connection = connectionPool.getConnection();
        boolean result = InsertIntoDb.addReader(request, connection);
        connectionPool.releaseConnection(connection);

        return result;
    }

    public boolean addBook(HttpServletRequest request){
        Connection connection = connectionPool.getConnection();
        boolean result = InsertIntoDb.addBook(request, connection);
        connectionPool.releaseConnection(connection);

        return result;
    }
}
