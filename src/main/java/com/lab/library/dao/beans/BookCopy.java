package com.lab.library.dao.beans;

public class BookCopy {
    private int id;
    private String name;
    private String damage;
    private int discount;

    public BookCopy(int id, String name, String damage, int discount) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.discount = discount;
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

    @Override
    public String toString() {
        return "BookCopy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", damage='" + damage + '\'' +
                ", discount=" + discount +
                '}';
    }
}
