package com.lab.library.service;

import com.lab.library.beans.Author;
import com.lab.library.beans.Book;
import com.lab.library.beans.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class RequestHandler {
    private static Logger logger = Logger.getLogger(RequestHandler.class.getName());

    static {
        try {
            logger.addHandler(new FileHandler("libraryLog"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Log exception", e);
        }
    }

    public static Reader getReaderFromReq(HttpServletRequest request) {
        Reader reader = new Reader();

        reader.setSurname(request.getParameter("surname"));
        reader.setName(request.getParameter("name"));
        reader.setPatronymic(setNull(request.getParameter("patronymic")));
        reader.setPassportNumber(setNull(request.getParameter("passportNumber")));
        reader.setDateOfBirth(Date.valueOf(request.getParameter("dateOfBirth")));
        reader.setAddress(setNull(request.getParameter("address")));
        reader.setEmail(request.getParameter("email"));

        return reader;
    }

    public static Book getBookFromReq(HttpServletRequest request) {
        Book book = new Book();

        book.setNameInRus(request.getParameter("nameInRus"));
        book.setOriginalName(setNull(request.getParameter("originalName")));
        book.setCost(Double.parseDouble(request.getParameter("cost")));
        book.setPriceForDay(Double.parseDouble(request.getParameter("priceForDay")));

        String year = (request.getParameter("publYear"));
        if (Objects.equals(year, "")) {
            book.setYear(null);
        } else {
            book.setYear(Integer.valueOf(request.getParameter("publYear")));
        }

        String pageAmount = (request.getParameter("pageAmount"));
        if (Objects.equals(pageAmount, "")) {
            book.setPageAmount(null);
        } else {
            book.setPageAmount(Integer.valueOf(request.getParameter("pageAmount")));
        }

        Enumeration<String> params = request.getParameterNames();
        List<String> authorsParam = new ArrayList<>();
        while (params.hasMoreElements()) {
            String el = params.nextElement();
            if (Pattern.matches("author[0-9]*", el)) {
                authorsParam.add(el);
            }
        }
        List<Author> authors = new ArrayList<>();
        for (String param : authorsParam) {
            Author author = new Author();
            author.setName(request.getParameter(param));
            String photosParam = "authorPhotos" + param.substring(6) + "[]";
            List<InputStream> photos = new ArrayList<>();
            try {
                List<Part> parts = (List<Part>) request.getParts();
                for (Part part : parts) {
                    if (part.getName().equalsIgnoreCase(photosParam)) {
                        InputStream inputStream = part.getInputStream();
                        byte[] bytes = new byte[inputStream.available()];
                        if (bytes.length != 0) {
                            photos.add(inputStream);
                        }
                    }
                }
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }
            author.setPhotos(photos);

            authors.add(author);
        }
        book.setAuthors(authors);

        List<InputStream> covers = new ArrayList<>();
        try {
            List<Part> parts = (List<Part>) request.getParts();

            for (Part part : parts) {
                if (part.getName().equalsIgnoreCase("covers[]")) {
                    covers.add(part.getInputStream());
                }
            }
        } catch (IOException | ServletException e) {
            logger.log(Level.SEVERE, "Processing parts exception ", e);
        }
        book.setCovers(covers);

        String[] genres = request.getParameterValues("genres");
        List<Integer> genresId = new ArrayList<>();
        for (String genre : genres) {
            genresId.add(Integer.parseInt(genre));
        }
        book.setGenresId(genresId);

        book.setTotalAmount(Integer.parseInt(request.getParameter("amount")));
        book.setRegistrDate(Date.valueOf(request.getParameter("registrDate")));

        return book;
    }

    private static String setNull(String param) {
        if (Objects.equals(param, "")) {
            param = null;
        }
        return param;
    }
}
