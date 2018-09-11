package com.shyk.alena.booksapp.list;

import android.view.Menu;
import android.view.MenuInflater;

import com.shyk.alena.booksapp.models.BooksVolume;
import com.shyk.alena.booksapp.retrofit.GoogleBooksRestClient;
import com.shyk.alena.booksapp.retrofit.RetrofitListener;

import java.util.ArrayList;
import java.util.List;

import static com.shyk.alena.booksapp.utils.Constants.MAX_RESULT;

public class ListPresenter implements ListContract.Presenter {
    private ListContract.View view;
    private String searchKeyword;
    private static final int PAGE_START = 0;
    private boolean isLastPage = false;
    private int startIndex = PAGE_START;
    private RetrofitListener retrofitListener;

    public ListPresenter(ListContract.View view, RetrofitListener retrofitListener) {
        this.view = view;
        this.retrofitListener = retrofitListener;
        this.view.initList();
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    @Override
    public void setupSearch(Menu menu) {
        view.initSearch(menu);
    }


    public void loadFirstPage(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        view.clearAdapter();
        GoogleBooksRestClient.getInstance().getBookList(searchKeyword, retrofitListener, "0", String.valueOf(MAX_RESULT));
    }

    public void loadNextPage() {
        startIndex += MAX_RESULT + 1;
        GoogleBooksRestClient.getInstance().getBookList(searchKeyword, retrofitListener, String.valueOf(startIndex), String.valueOf(MAX_RESULT));


    }

    List<BooksVolume> getBooksFromDB() {
        ArrayList<BooksVolume> list=new ArrayList();
        return list;
    }

    @Override
    public void onNextList(List<BooksVolume> items) {
        isLastPage = items.size() < MAX_RESULT;
        view.addNextPage(items);
    }


    @Override
    public void destroy() {
        view = null;
    }


}
