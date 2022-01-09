package com.lab.library;

import com.lab.library.dao.DBManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@WebServlet(name = "frontServlet", value = "/library/*")
@MultipartConfig
public class FrontServlet extends HttpServlet {
    DBManager dbManager = new DBManager();

    public void init() {
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
                request.setAttribute("genres", dbManager.selectFromGenres());
                break;
            case ("/library/readerList"):
                url = "/readerList.jsp";
                request.setAttribute("readers", dbManager.selectFromReader());
                break;
            case ("/library"):
                url = "/main.jsp";
                request.setAttribute("books", dbManager.selectFromBook());
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
                    if (dbManager.addReader(request)) {
                        request.setAttribute("result", "Пользователь создан успешно.");
                    } else {
                        request.setAttribute("result", "Не удалось создать пользователя.");
                    }
                    getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
                    break;
                case "/library/addBook":
                    if (dbManager.addBook(request)) {
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
