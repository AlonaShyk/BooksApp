package com.shyk.alena.booksapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shyk.alena.booksapp.models.BooksVolume;
import com.shyk.alena.booksapp.models.ImageLinks;
import com.shyk.alena.booksapp.models.VolumeInfo;

import java.util.ArrayList;

public class LocalDataProvider {
    private final Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public LocalDataProvider(Context context) {
        this.context = context;
    }

    public void setupDB() {
        dbHelper = DBHelper.getInstance(context);
        database = dbHelper.getWritableDatabase();
    }

    public ArrayList<BooksVolume> getBooksFromDB(String id) {
        Cursor cursor;
        if (id != null) {
            cursor = database.rawQuery(queryBookById(id), null);
        } else {
            if (database == null && dbHelper == null) {
                setupDB();
            }
            cursor = database.rawQuery(queryAllBooks(), null);
        }
        ArrayList<BooksVolume> booksVolumes = new ArrayList<>();
        if (cursor.moveToFirst() || id != null) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_BOOK_ID);
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int authorIndex = cursor.getColumnIndex(DBHelper.KEY_AUTHOR);
            int imageIndex = cursor.getColumnIndex(DBHelper.KEY_IMG);
            int pageIndex = cursor.getColumnIndex(DBHelper.KEY_NUMBER_OF_PAGES);
            int publisherIndex = cursor.getColumnIndex(DBHelper.KEY_PUBLISHER);
            int yearIndex = cursor.getColumnIndex(DBHelper.KEY_YEAR);
            int descriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
            do {
                VolumeInfo info = new VolumeInfo();
                info.setTitle(cursor.getString(titleIndex));
                info.setAuthor(cursor.getString(authorIndex));
                info.setImageLinks(new ImageLinks(cursor.getString(imageIndex)));
                info.setPageCount(cursor.getInt(pageIndex));
                info.setPublisher(cursor.getString(publisherIndex));
                info.setPublishedDate(cursor.getString(yearIndex));
                info.setDescription(cursor.getString(descriptionIndex));
                BooksVolume volume = new BooksVolume();
                volume.setId(cursor.getString(idIndex));
                volume.setVolumeInfo(info);
                booksVolumes.add(volume);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return booksVolumes;
    }

    private String queryBookById(String id) {
        return String.format("SELECT * FROM %1$s WHERE %2$s = '%3$s'",
                DBHelper.TABLE_BOOKS,
                DBHelper.KEY_BOOK_ID,
                id);
    }

    private String queryAllBooks() {
        return String.format("SELECT * FROM %1$s",
                DBHelper.TABLE_BOOKS);
    }

    public boolean isInDatabase(String bookId) {
        Cursor cursor = database.rawQuery(queryBookById(bookId), null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public void addToDatabase(final BooksVolume booksVolume) {
        String sqlInsert = "insert into " + DBHelper.TABLE_BOOKS + " ("
                + DBHelper.KEY_BOOK_ID
                + ", " + DBHelper.KEY_TITLE
                + ", " + DBHelper.KEY_AUTHOR
                + ", " + DBHelper.KEY_NUMBER_OF_PAGES
                + ", " + DBHelper.KEY_PUBLISHER
                + ", " + DBHelper.KEY_YEAR
                + ", " + DBHelper.KEY_IMG
                + ", " + DBHelper.KEY_DESCRIPTION
                + ")" + " values( ?, ?, ?, ?, ?, ?, ?, ?)";

        database.beginTransaction();
        SQLiteStatement stmt = database.compileStatement(sqlInsert);
        stmt.bindString(1, booksVolume.getId());
        stmt.bindString(2, booksVolume.getVolumeInfo().getTitle());
        stmt.bindString(3, booksVolume.getVolumeInfo().getAuthors().get(0));
        stmt.bindLong(4, booksVolume.getVolumeInfo().getPageCount());
        stmt.bindString(5, booksVolume.getVolumeInfo().getPublisher());
        stmt.bindString(6, booksVolume.getVolumeInfo().getPublishedDate());
        stmt.bindString(7, booksVolume.getVolumeInfo().getImageLinks().getThumbnail());
        String description = booksVolume.getVolumeInfo().getDescription();
        if (description == null) {
            stmt.bindString(8, "");
        } else stmt.bindString(8, description);
        stmt.executeInsert();
        stmt.clearBindings();
        database.setTransactionSuccessful();
        database.endTransaction();
        dbHelper.close();
    }

    public void closeDB() {
        dbHelper.close();
        database.close();
    }

}
