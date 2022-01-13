package com.lab.library.dao.beans;

public class BookCopy {
    private int id;
    private String name;
    private String damage;
    private int discount;
    private String preliminaryDate;
    private double priceForDay;

    public BookCopy(int id, String name, String damage, int discount) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.discount = discount;
    }

    public BookCopy(String name, int discount, String preliminaryDate, double priceForDay) {
        this.name = name;
        this.discount = discount;
        this.preliminaryDate = preliminaryDate;
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

    public String getPreliminaryDate() {
        return preliminaryDate;
    }

    public void setPreliminaryDate(String preliminaryDate) {
        this.preliminaryDate = preliminaryDate;
    }

    public double getPriceForDay() {
        return priceForDay;
    }

    public void setPriceForDay(double priceForDay) {
        this.priceForDay = priceForDay;
    }
}
