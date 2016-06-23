package com.devixgroup.translatetospanish.persist.sqlite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;

import com.devixgroup.translatetospanish.R;
import com.devixgroup.translatetospanish.io.TranslationsFileIO;
import com.devixgroup.translatetospanish.model.Translation;
import com.devixgroup.translatetospanish.persist.sqlite.TranslationContract.TranslationEntry;

import java.util.List;

public class TranslationDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = TranslationDbHelper.class.getName();
    public static final int DATABASE_VERSION = 1;// If you change the database schema, you must increment the database version.
    public static final String DATABASE_NAME = "Translation.db";

    private TranslationsFileIO mTranslationFileIO;
    private Context mContext;

    public TranslationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mTranslationFileIO = new TranslationsFileIO();
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, " onCreate: ...");
        db.execSQL(TranslationEntry.SQL_CREATE_ENTRIES);
        seedDb(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(TranslationEntry.SQL_DROP_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement only if needed
        onUpgrade(db, oldVersion, newVersion);
    }

    private void seedDb(SQLiteDatabase db) {
        Log.d(LOG_TAG, " seedDb: ...");
        deleteAllRows(db);

        List<Translation> translations = mTranslationFileIO.readInTranslations(mContext, R.raw.init_words);
        for(Translation t : translations) {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_ENGLISH_WORD, t.getEnWord());
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_SPANISH_WORD, t.getEsWord());
            values.put(TranslationContract.TranslationEntry.COLUMN_NAME_CACHED, t.getIsCached());
            insertRow(db, values);
        }
    }

    private void deleteAllRows(SQLiteDatabase db) {
        final int rowsDeleted = db.delete(TranslationContract.TranslationEntry.TABLE_NAME, null, null);
        Log.d(LOG_TAG, "deleteAllRows: Deleted " + rowsDeleted + " from table " + TranslationContract.TranslationEntry.TABLE_NAME);
    }

    private void insertRow(SQLiteDatabase db, ContentValues values) {
        // Insert the new row, returning the primary key value of the new row
        long rowId = db.insert(TranslationContract.TranslationEntry.TABLE_NAME, TranslationContract.TranslationEntry.COLUMN_NAME_NULLABLE, values);
        Log.d(LOG_TAG, "Inserted row " + rowId + " with values " + values.toString());
    }
}
