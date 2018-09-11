package com.shyk.alena.booksapp.retrofit;

import com.shyk.alena.booksapp.models.BooksVolume;

import java.util.List;

public interface RetrofitListener {
    void onBook(BooksVolume booksVolume);
    void onList(List<BooksVolume> items);
}
