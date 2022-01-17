package com.lab.library.beans;

import java.io.InputStream;
import java.sql.Date;
import java.util.List;

public class Book implements Comparable<Book> {
    private int id;
    private String nameInRus;
    private String originalName;
    private List<String> genres;
    private List<Integer> genresId;
    private double cost;
    private double priceForDay;
    private List<Author> authors;
    private List<InputStream> covers;
    private Date registrDate;
    private Integer pageAmount;
    private Integer year;
    private int totalAmount;
    private int availableAmount;

    public Book(){}

    public Book(int id, String nameInRus, List<String> genres, Integer year, int totalAmount, int availableAmount) {
        this.id = id;
        this.nameInRus = nameInRus;
        this.genres = genres;
        this.year = year;
        this.totalAmount = totalAmount;
        this.availableAmount = availableAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameInRus() {
        return nameInRus;
    }

    public void setNameInRus(String nameInRus) {
        this.nameInRus = nameInRus;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPriceForDay() {
        return priceForDay;
    }

    public void setPriceForDay(double priceForDay) {
        this.priceForDay = priceForDay;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<InputStream> getCovers() {
        return covers;
    }

    public void setCovers(List<InputStream> covers) {
        this.covers = covers;
    }

    public Date getRegistrDate() {
        return registrDate;
    }

    public void setRegistrDate(Date registrDate) {
        this.registrDate = registrDate;
    }

    public Integer getPageAmount() {
        return pageAmount;
    }

    public void setPageAmount(Integer pageAmount) {
        this.pageAmount = pageAmount;
    }

    public List<Integer> getGenresId() {
        return genresId;
    }

    public void setGenresId(List<Integer> genresId) {
        this.genresId = genresId;
    }

    @Override
    public int compareTo(Book o) {
        int result;
        result = Integer.compare(availableAmount, o.availableAmount);
        if (result != 0) {
            return result;
        }
        result = nameInRus.compareTo(o.nameInRus);
        return result;
    }
}
