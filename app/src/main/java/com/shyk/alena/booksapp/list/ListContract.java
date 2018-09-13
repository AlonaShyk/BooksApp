package com.shyk.alena.booksapp.list;

import android.view.Menu;
import android.view.MenuInflater;

import com.shyk.alena.booksapp.base.BasePresenter;
import com.shyk.alena.booksapp.base.BaseView;
import com.shyk.alena.booksapp.models.BooksVolume;

import java.util.List;

public class ListContract {
    interface Model {
    }

    interface View extends BaseView {
        void initSearch(Menu menu);

        void initList();

        void clearAdapter();

        void addNextPage(List<BooksVolume> items);

        void openDetail(String id);
    }

    interface Presenter extends BasePresenter<View> {
        void setupSearch(Menu menu);

        void onNextList(List<BooksVolume> items);

        void loadBooksFromDB();
    }
}
