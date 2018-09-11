package com.shyk.alena.booksapp.detail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shyk.alena.booksapp.data.DBHelper;
import com.shyk.alena.booksapp.models.BooksVolume;
import com.shyk.alena.booksapp.models.VolumeInfo;
import com.shyk.alena.booksapp.retrofit.GoogleBooksRestClient;
import com.shyk.alena.booksapp.retrofit.RetrofitListener;

public class DetailPresenter implements DetailContract.Presenter {
    private DetailContract.View view;
    private RetrofitListener listener;
    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public DetailPresenter(Context context, DetailContract.View view, RetrofitListener listener) {
        this.view = view;
        this.listener = listener;
        this.context = context;
    }

    public void setupDB() {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
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
        String query = queryBookById(id);
        Cursor cursor = database.rawQuery(query, null);
        int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
        int authorIndex = cursor.getColumnIndex(DBHelper.KEY_AUTHOR);
        int imageIndex = cursor.getColumnIndex(DBHelper.KEY_IMG);
        int pageIndex = cursor.getColumnIndex(DBHelper.KEY_NUMBER_OF_PAGES);
        int publisherIndex = cursor.getColumnIndex(DBHelper.KEY_PUBLISHER);
        int yearIndex = cursor.getColumnIndex(DBHelper.KEY_YEAR);
        int descriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
        cursor.moveToFirst();
        view.inflateDetail(cursor.getString(titleIndex),
                cursor.getString(authorIndex),
                cursor.getString(imageIndex),
                cursor.getInt(pageIndex),
                cursor.getString(publisherIndex),
                cursor.getString(yearIndex),
                cursor.getString(descriptionIndex));
        cursor.close();
    }

    private String queryBookById(String id) {
        return String.format("SELECT * FROM %1$s WHERE %2$s = '%3$s'",
                DBHelper.TABLE_BOOKS,
                DBHelper.KEY_BOOK_ID,
                id);
    }

    @Override
    public void showBook(BooksVolume booksVolume) {
        VolumeInfo info = booksVolume.getVolumeInfo();
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
        Cursor cursor = database.rawQuery(queryBookById(bookId), null);
        cursor.moveToFirst();
        int idIndex = cursor.getColumnIndex(DBHelper.KEY_BOOK_ID);
        if (cursor.getString(idIndex) == null) {
            cursor.close();
            return false;
        } else cursor.close();
        return true;
    }

    @Override
    public void showBook(String id) {

    }

    @Override
    public void addToDatabase(BooksVolume booksVolume) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_BOOK_ID, booksVolume.getId());
        contentValues.put(DBHelper.KEY_TITLE, booksVolume.getVolumeInfo().getTitle());
        contentValues.put(DBHelper.KEY_AUTHOR, booksVolume.getVolumeInfo().getAuthors().get(0));
        contentValues.put(DBHelper.KEY_NUMBER_OF_PAGES, booksVolume.getVolumeInfo().getPageCount());
        contentValues.put(DBHelper.KEY_PUBLISHER, booksVolume.getVolumeInfo().getPublisher());
        contentValues.put(DBHelper.KEY_YEAR, booksVolume.getVolumeInfo().getPublishedDate());
        contentValues.put(DBHelper.KEY_IMG, booksVolume.getVolumeInfo().getImageLinks().getThumbnail());
        contentValues.put(DBHelper.KEY_DESCRIPTION, booksVolume.getVolumeInfo().getDescription());
        database.insert(DBHelper.TABLE_BOOKS, null, contentValues);
        dbHelper.close();
    }
}
