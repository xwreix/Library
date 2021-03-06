package com.lab.library.beans;

public class PopularBook {
    private int id;
    private String base64Image;
    private int amount;
    private double rating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
