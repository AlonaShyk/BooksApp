package com.shyk.alena.booksapp.data;

import com.shyk.alena.booksapp.retrofit.GoogleBooksRestClient;
import com.shyk.alena.booksapp.retrofit.RetrofitListener;

import static com.shyk.alena.booksapp.utils.Constants.MAX_RESULT;

public class RemoteDataProvider {


    public static void loadBook(String id, RetrofitListener listener) {
        GoogleBooksRestClient.getInstance().getBook(id, listener);
    }

    public static void loadList(String searchKeyword, RetrofitListener retrofitListener, String startIndex) {
        GoogleBooksRestClient.getInstance().getBookList(searchKeyword, retrofitListener, startIndex, String.valueOf(MAX_RESULT));
    }
}

