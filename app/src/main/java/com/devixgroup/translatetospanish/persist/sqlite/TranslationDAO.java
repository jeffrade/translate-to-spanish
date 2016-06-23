package com.devixgroup.translatetospanish.persist.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TranslationDAO {

    private static final String LOG_TAG = TranslationDAO.class.getName();

    private SQLiteDatabase mTranslationDb;
    private TranslationDbHelper mDbHelper;

    public TranslationDAO(Context context) {
        super();
        mDbHelper = new TranslationDbHelper(context);
        mTranslationDb = mDbHelper.getWritableDatabase();
    }

    public List<String> readFromDb(final String enWord) {
        // Define a projection that specifies which columns from the database you will actually use after this query.
        String[] projection = {
                TranslationContract.TranslationEntry.COLUMN_NAME_SPANISH_WORD
        };

        String selection = TranslationContract.TranslationEntry.COLUMN_NAME_ENGLISH_WORD + " = ? ";
        String[] selectionArgs = { enWord };

        Cursor cursor = mTranslationDb.query(
                TranslationContract.TranslationEntry.TABLE_NAME, // The table to query
                projection,                  // The columns to return
                selection,                   // The columns for the WHERE clause
                selectionArgs,               // The values for the WHERE clause
                null,                        // don't group the rows
                null,                        // don't filter by row groups
                null                         // The sort order
        );

        List<String> results = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                results.add(cursor.getString(0));
            }
        } finally {
            cursor.close();
        }

        Log.d(LOG_TAG, "readFromDb: Retrieved " + results.toString() + " from database");
        return results;
    }

}
