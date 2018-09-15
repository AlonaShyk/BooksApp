package com.shyk.alena.booksapp.detail;

import android.content.Context;

import com.shyk.alena.booksapp.data.LocalDataProvider;
import com.shyk.alena.booksapp.models.BooksVolume;
import com.shyk.alena.booksapp.models.VolumeInfo;
import com.shyk.alena.booksapp.retrofit.GoogleBooksRestClient;
import com.shyk.alena.booksapp.retrofit.RetrofitListener;

public class DetailPresenter implements DetailContract.Presenter {
    private DetailContract.View view;
    private final RetrofitListener listener;
    private final Context context;
    private LocalDataProvider localDataProvider;

    public DetailPresenter(Context context, DetailContract.View view, RetrofitListener listener) {
        this.view = view;
        this.listener = listener;
        this.context = context;
    }

    public void createDBAccess() {
        localDataProvider = new LocalDataProvider(context);
        localDataProvider.setupDB();
    }

    @Override
    public void destroy() {
        view = null;
    }


    @Override
    public void loadBook(String id) {
        GoogleBooksRestClient.getInstance().getBook(id, listener);
    }

    @Override
    public void getBookFromDB(String id) {
       showBook(localDataProvider.getBooksFromDB(id).get(0).getVolumeInfo());
    }

    @Override
    public void showBook(VolumeInfo info) {
        if (info.getAuthors() == null) {
            info.setAuthor("");
        }
        view.inflateDetail(info.getTitle(),
                info.getAuthors().get(0),
                info.getImageLinks().getThumbnail(),
                info.getPageCount(),
                info.getPublisher(),
                info.getPublishedDate(),
                info.getDescription()
        );
    }

    @Override
    public boolean isInDatabase(String bookId) {
       return localDataProvider.isInDatabase(bookId);
    }

    @Override
    public void addToDatabase(BooksVolume booksVolume) {
       localDataProvider.addToDatabase(booksVolume);
    }
}
