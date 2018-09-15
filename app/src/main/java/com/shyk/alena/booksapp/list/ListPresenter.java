package com.shyk.alena.booksapp.list;

import android.content.Context;
import android.view.Menu;

import com.shyk.alena.booksapp.data.LocalDataProvider;
import com.shyk.alena.booksapp.data.RemoteDataProvider;
import com.shyk.alena.booksapp.models.BooksVolume;
import com.shyk.alena.booksapp.retrofit.RetrofitListener;

import java.util.List;

import static com.shyk.alena.booksapp.utils.Constants.MAX_RESULT;

public class ListPresenter implements ListContract.Presenter {
    private ListContract.View view;
    private String searchKeyword;
    private static final int PAGE_START = 0;
    private boolean isLastPage = false;
    private int startIndex = PAGE_START;
    private final RetrofitListener retrofitListener;
    private final RemoteDataProvider remoteDataProvider = new RemoteDataProvider();
    private LocalDataProvider localDataProvider;
    private final Context context;


    public ListPresenter(Context context, ListContract.View view, RetrofitListener retrofitListener) {
        this.context = context;
        this.view = view;
        this.retrofitListener = retrofitListener;

    }

    public void create() {
        localDataProvider = new LocalDataProvider(context);
        this.view.initList();
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    @Override
    public void setupSearch(Menu menu) {
        view.initSearch(menu);
    }

    @Override
    public void setupHistory(Menu menu) {
        view.initHistory(menu);
    }

    @Override
    public void setupSignIn(Menu menu) {
        view.initSignIn(menu);
    }


    public void loadFirstPage(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        view.clearAdapter();
        remoteDataProvider.loadList(searchKeyword, retrofitListener, "0");
    }

    public void loadNextPage() {
        startIndex += MAX_RESULT + 1;
        remoteDataProvider.loadList(searchKeyword, retrofitListener, String.valueOf(startIndex));
    }

    private List<BooksVolume> getBooksFromDB() {
        return localDataProvider.getBooksFromDB(null);
    }

    @Override
    public void onNextList(List<BooksVolume> items) {
        isLastPage = items.size() < MAX_RESULT;
        if (view != null) {
            view.addNextPage(items);
        }
    }

    @Override
    public void loadBooksFromDB() {
        List<BooksVolume> volumes = getBooksFromDB();
        view.clearAdapter();
        retrofitListener.onList(volumes);
    }

    @Override
    public void onSignIn() {
        view.openSignInActivity();
    }


    @Override
    public void destroy() {
        view = null;
    }


}
