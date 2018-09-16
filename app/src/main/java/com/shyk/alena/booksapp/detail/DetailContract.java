package com.shyk.alena.booksapp.detail;

import com.shyk.alena.booksapp.base.BasePresenter;
import com.shyk.alena.booksapp.base.BaseView;
import com.shyk.alena.booksapp.models.BooksVolume;
import com.shyk.alena.booksapp.models.VolumeInfo;

class DetailContract {
    interface Model {
    }

    interface View extends BaseView {
        void inflateDetail(String bookTitle,
                           String bookAuthor,
                           String imageLink,
                           int numberPage,
                           String bookPublisher,
                           String bookYear,
                           String bookDescription);
    }

    interface Presenter extends BasePresenter<View> {

        void showBook(VolumeInfo info);

        void addToDatabase(BooksVolume booksVolume);

        void getBook(String bookId);
    }
}
