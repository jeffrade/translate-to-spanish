package com.devixgroup.translatetospanish.persist.sqlite;

import android.provider.BaseColumns;

public final class TranslationContract {

    public TranslationContract() {
        super();
    }

    public static abstract class TranslationEntry implements BaseColumns {
        public static final String TABLE_NAME = "translations";
        public static final String COLUMN_NAME_ENGLISH_WORD = "en_word";
        public static final String COLUMN_NAME_SPANISH_WORD = "es_word";
        public static final String COLUMN_NAME_CACHED = "is_cached";
        public static final String COLUMN_NAME_NULLABLE = null;

        private static final String TEXT_TYPE = " TEXT";
        private static final String INT_TYPE = " INTEGER";
        private static final String COMMA_SEP = ", ";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + TranslationEntry.TABLE_NAME + " (" +
                        TranslationEntry._ID + " INTEGER PRIMARY KEY, " +
                        TranslationEntry.COLUMN_NAME_ENGLISH_WORD + TEXT_TYPE + COMMA_SEP +
                        TranslationEntry.COLUMN_NAME_SPANISH_WORD + TEXT_TYPE + COMMA_SEP +
                        TranslationEntry.COLUMN_NAME_CACHED + INT_TYPE +
                " )";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TranslationEntry.TABLE_NAME;

    }
}
