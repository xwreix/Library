package com.lab.library.beans;

import java.io.InputStream;
import java.util.List;

public class Author {
    private int id;
    private String name;
    private List<InputStream> photos;

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

    public List<InputStream> getPhotos() {
        return photos;
    }

    public void setPhotos(List<InputStream> photos) {
        this.photos = photos;
    }
}
