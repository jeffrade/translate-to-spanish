package com.devixgroup.translatetospanish.io;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.devixgroup.translatetospanish.model.Translation;

public final class TranslationsFileIO {

    public static final String LOG_TAG = TranslationsFileIO.class.getName();

    public List<Translation> readInTranslations(final Context context, final int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);

        BufferedReader buffReader = new BufferedReader(new InputStreamReader(inputStream));
        List<Translation> translations = new ArrayList<Translation>();
        String line;

        try {
            while (( line = buffReader.readLine()) != null) {
                String[] entries = line.split(",");
                translations.add(new Translation(entries[0], entries[1], true));
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error when reading in cached translations", e);
        }

        return translations;
    }
}
