package com.lab.library;

import com.google.gson.Gson;
import com.lab.library.beans.Book;
import com.lab.library.beans.BookCopy;
import com.lab.library.beans.Reader;
import com.lab.library.beans.Status;
import com.lab.library.dao.ConnectionPool;
import com.lab.library.service.BookService;
import com.lab.library.service.ReaderService;
import com.lab.library.service.RequestHandler;

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
import java.sql.SQLException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "frontServlet", value = "/library/*")
@MultipartConfig
public class FrontServlet extends HttpServlet {
    Logger logger = Logger.getLogger(FrontServlet.class.getName());
    ReaderService readerService = new ReaderService();
    BookService bookService = new BookService();
    //manager dbManager = new manager();

    public void init() {
        try {
            logger.addHandler(new FileHandler("libraryLog"));
            ConnectionPool.create();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Log exception ", e);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Creating connection exception ", e);
        }
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
                request.setAttribute("genres", bookService.selectGenres());
                break;
            case ("/library/readerList"):
                url = "/readerList.jsp";
                request.setAttribute("readers", readerService.selectReaders());
                break;
            case ("/library/issueBook"):
                url = "/issueBook.jsp";
                break;
            case ("/library/returnBook"):
                url = "/returnBook.jsp";
                break;
            case ("/library/checkReaderExistence"):
                status = readerService.checkReader(request.getParameter("readerEmail"));
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
                status = bookService.checkBook(request.getParameter("bookName"));
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
                double cost = bookService.selectCost(request.getParameter("bookName"));
                sendJSON(true, String.valueOf(cost), response);
                break;
            case ("/library/countGivenBooks"):
                int amount = dbManager.countReaderBooks(request.getParameter("readerEmail"));
                sendJSON(true, String.valueOf(amount), response);
                break;
            case ("/library/getGivenBooks"):
                List<BookCopy> books = readerService.selectAllBooks(request.getParameter("readerEmail"));
                sendArrayJSON(books, response);
                break;
            case ("/library"):
                url = "/main.jsp";
                request.setAttribute("books", bookService.selectBooks());
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
            logger.log(Level.SEVERE, "Referer exception ", e);
        }

        if (referer != null) {
            switch (referer) {
                case "/library/addReader":
                    Reader reader = RequestHandler.getReaderFromReq(request);
                    if (readerService.addReader(reader)) {
                        request.setAttribute("result", "Пользователь создан успешно.");
                    } else {
                        request.setAttribute("result", "Не удалось создать пользователя.");
                    }
                    getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
                    break;
                case "/library/addBook":
                    Book book = RequestHandler.getBookFromReq(request);
                    if (bookService.addBook(book)) {
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
            logger.log(Level.SEVERE, "Writing JSON exception ", e);
        }
    }

    private void sendArrayJSON(List<BookCopy> books, HttpServletResponse response) {
        String json = new Gson().toJson(books);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(json);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Writing JSON exception ", e);
        }
    }
