package com.lab.library;

import com.google.gson.Gson;
import com.lab.library.dao.DBManager;
import com.lab.library.dao.beans.BookCopy;
import com.lab.library.dao.beans.Status;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@WebServlet(name = "frontServlet", value = "/library/*")
@MultipartConfig
public class FrontServlet extends HttpServlet {
    DBManager dbManager = new DBManager();

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String referer = request.getRequestURI();
        String url = null;
        Status status;
        boolean valid = false;
        String message = "";

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
            case ("/library/issueBook"):
                url = "/issueBook.jsp";
                break;
            case ("/library/returnBook"):
                url = "/returnBook.jsp";
                break;
            case ("/library/checkReaderExistence"):
                status = dbManager.checkReader(request.getParameter("readerEmail"));
                if (status == Status.AVAIlABLE) {
                    valid = true;
                } else if (status == Status.NON_EXISTENT) {
                    message = "Читатель не существует";
                } else if (status == Status.LOCKED) {
                    message = "Читатель не вернул все книги";
                }
                sendJSON(valid, message, response);
                break;
            case ("/library/checkBook"):
                status = dbManager.checkBook(request.getParameter("bookName"));
                if (status == Status.AVAIlABLE) {
                    valid = true;
                } else if (status == Status.NON_EXISTENT) {
                    message = "Такой книги не существует";
                } else if (status == Status.LOCKED) {
                    message = "В наличии нет доступных экзмепляров";
                }
                sendJSON(valid, message, response);
                break;
            case ("/library/getCost"):
                double cost = dbManager.getCost(request.getParameter("bookName"));
                sendJSON(true, String.valueOf(cost), response);
                break;
            case ("/library/countGivenBooks"):
                int amount = dbManager.countReaderBooks(request.getParameter("readerEmail"));
                sendJSON(true, String.valueOf(amount), response);
                break;
            case ("/library/getGivenBooks"):
                List<BookCopy> books = dbManager.getReaderBooks(request.getParameter("readerEmail"));
                sendArrayJSON(books, response);
                break;
            case ("/library"):
                url = "/main.jsp";
                request.setAttribute("books", dbManager.selectFromBook());
                break;
        }

        if (url != null) {

            try {
                getServletContext().getRequestDispatcher(url).forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
                //TODO log
            }
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
            //TODO log
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
                case "/library/issueBook":
                    List<BookCopy> givenBooks = dbManager.addIssue(request);
                    request.setAttribute("givenBooks", givenBooks);
                    getServletContext().getRequestDispatcher("/givenBooks.jsp").forward(request, response);
                    break;
                case "/library/returnBook":
                    if (dbManager.returnBook(request)) {
                        request.setAttribute("result", "Возврат книг проведён успешно");
                    } else {
                        request.setAttribute("result", "Не удалось провести возврат книг");
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

    private void sendJSON(boolean valid, String message, HttpServletResponse response) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                .add("valid", valid)
                .add("value", message);
        JsonObject jsonObject = objectBuilder.build();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO log
        }
    }

    private void sendArrayJSON(List<BookCopy> books, HttpServletResponse response) {
        String json = new Gson().toJson(books);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
            //TODO log
        }
    }
}
