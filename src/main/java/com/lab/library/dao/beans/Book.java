package com.lab.library.dao.beans;

import java.util.List;

public class Book implements Comparable<Book>{
    private  int id;
    private String name;
    private List<String> genres;
    private int year;
    private int totalAmount;
    private int availableAmount;

    public Book(int id, String name, List<String> genres, int year, int totalAmount, int availableAmount) {
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
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

    @Override
    public int compareTo(Book o) {
        int result;
        result = Integer.compare(availableAmount, o.availableAmount);
        if(result!=0){
            return  result;
        }
        result = name.compareTo(o.name);
        return result;
    }
}
