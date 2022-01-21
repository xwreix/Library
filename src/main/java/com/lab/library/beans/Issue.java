package com.lab.library.beans;

import java.sql.Date;
import java.util.List;

public class Issue {
    private int readerId;
    private String readerEmail;
    private List<String> books;
    private List<BookCopy> returned;
    private Date date;
    private int discount;

    public Issue() {
    }

    public int getReaderId() {
        return readerId;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }

    public String getReaderEmail() {
        return readerEmail;
    }

    public void setReaderEmail(String readerEmail) {
        this.readerEmail = readerEmail;
    }

    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        this.books = books;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<BookCopy> getReturned() {
        return returned;
    }

    public void setReturned(List<BookCopy> returned) {
        this.returned = returned;
    }
}
