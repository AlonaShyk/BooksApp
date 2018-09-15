package com.shyk.alena.booksapp.list;

import android.view.Menu;

import com.shyk.alena.booksapp.base.BasePresenter;
import com.shyk.alena.booksapp.base.BaseView;
import com.shyk.alena.booksapp.models.BooksVolume;

import java.util.List;

class ListContract {
    interface Model {
    }

    interface View extends BaseView {
        void initSearch(Menu menu);

        void initHistory(Menu menu);

        void initSignIn(Menu menu);

        void initList();

        void clearAdapter();

        void addNextPage(List<BooksVolume> items);

        void openDetail(String id);

        void openSignInActivity();
    }

    interface Presenter extends BasePresenter<View> {
        void setupSearch(Menu menu);

        void setupHistory(Menu menu);

        void setupSignIn(Menu menu);

        void onNextList(List<BooksVolume> items);

        void loadBooksFromDB();

        void onSignIn();
    }
}
