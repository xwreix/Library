package com.lab.library;

import com.lab.library.dao.ConnectionPool;
import com.lab.library.dao.ConnectionPoolRealiz;
import com.lab.library.dao.GetFromDB;
import com.lab.library.dao.InsertIntoDb;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

@WebServlet(name = "frontServlet", value = "/library/*")
@MultipartConfig
public class FrontServlet extends HttpServlet {
    private ConnectionPool connectionPool;

    public void init() {
        try {
            connectionPool = ConnectionPoolRealiz.create
                    ("jdbc:postgresql://localhost:5432/library?characterEncoding=UTF8&useUnicode=true",
                            "postgres", "mnr1209");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Not successful connection");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String referer = request.getRequestURI();
        String url = "/main.jsp";

        switch (referer) {
            case ("/library/addReader"):
                url = "/addReader.jsp";
                break;
            case ("/library/addBook"):
                url = "/addBook.jsp";
                request.setAttribute("genres", GetFromDB.setGenres(connectionPool.getConnection()));
                break;
            case ("/library"):
                url = "/main.jsp";
                request.setAttribute("books", GetFromDB.setBooks(connectionPool.getConnection()));
                break;

        }

        try {
            getServletContext().getRequestDispatcher(url).forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String referer = null;
        try {
            referer = new URI(request.getHeader("referer")).getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (referer != null) {
            switch (referer) {
                case "/library/addReader":
                    if (InsertIntoDb.addReader(request, connectionPool.getConnection())) {
                        request.setAttribute("result", "Пользователь создан успешно.");
                    } else {
                        request.setAttribute("result", "Не удалось создать пользователя.");
                    }
                    getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
                    break;
                case "/library/addBook":
                    if (InsertIntoDb.addBook(request, connectionPool.getConnection())) {
                        request.setAttribute("result", "Книга добавлена успешно.");
                    } else {
                        request.setAttribute("result", "Не удалось добавить книгу");
                    }
                    getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
                    break;
                default:
                    getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
            }
        }

    }

    public void destroy() {
    }

}