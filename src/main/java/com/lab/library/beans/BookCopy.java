package com.lab.library.beans;

import java.io.InputStream;
import java.util.List;

public class BookCopy {
    private int id;
    private String name;
    private String damage;
    private List<InputStream> damagePhotos;
    private int rating;
    private int discount;
    private String date;
    private double priceForDay;

    public BookCopy() {
    }

    public BookCopy(int id, String name, String damage, int discount) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.discount = discount;
    }

    public BookCopy(String name, int discount, String date, double priceForDay) {
        this.name = name;
        this.discount = discount;
        this.date = date;
        this.priceForDay = priceForDay;
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

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPriceForDay() {
        return priceForDay;
    }

    public void setPriceForDay(double priceForDay) {
        this.priceForDay = priceForDay;
    }

    public List<InputStream> getDamagePhotos() {
        return damagePhotos;
    }

    public void setDamagePhotos(List<InputStream> damagePhotos) {
        this.damagePhotos = damagePhotos;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
