package com.shyk.alena.booksapp.models;

import java.util.List;

public class Items {
    private List<BooksVolume> items;

    public Items(List<BooksVolume> booksVolumes) {
        this.items = booksVolumes;
    }

    public List<BooksVolume> getBooksVolumes() {
        return items;
    }

    public void setBooksVolumes(List<BooksVolume> booksVolumes) {
        this.items = booksVolumes;
    }
}
