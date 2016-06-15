package com.devixgroup.translatetospanish;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.devixgroup.translatetospanish.model.TranslationResponseJson;
import com.devixgroup.translatetospanish.model.TranslationUrl;
import com.devixgroup.translatetospanish.persist.sqlite.TranslationContract.TranslationEntry;
import com.devixgroup.translatetospanish.persist.sqlite.TranslationDbHelper;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MainActivity";
    private static final String ERROR_MESSAGE = "Sorry, could not translate";
    private static final String NETWORK_ERROR = "Network error!";
    private static final int VOLLEY_1MB_CAP = 1024 * 1024;

    private EditText mEnglishEditText;
    private TextView mSpansihTextView;
    private Button mTranslateButton;
    private TranslationDbHelper mDbHelper;
    private SQLiteDatabase mTranslationDb;
    private Properties mProperties;
    private String mKey;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        mEnglishEditText = (EditText) findViewById(R.id.textInput);
        mEnglishEditText.setOnEditorActionListener(createTextEditListener());
        mSpansihTextView = (TextView) findViewById(R.id.textBox);
        mTranslateButton = (Button) findViewById(R.id.translateButton);
        mDbHelper = new TranslationDbHelper(this);
        mTranslationDb = mDbHelper.getWritableDatabase();
        initUrlLinks();
        initProperties();
        initVolley();
    }

    private void initUrlLinks() {
        TextView translateServiceLink = (TextView) findViewById(R.id.translateServicePlug);
        translateServiceLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initProperties() {
        try {
            mProperties = new Properties();
            mProperties.load(getAssets().open("app.properties"));
            mKey = mProperties.getProperty("k");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Encountered an error while trying to get properties", e);
        }
    }

    private void initVolley() {
        Cache cache = new DiskBasedCache(getCacheDir(), VOLLEY_1MB_CAP); // Instantiate the cache, 1MB cap
        Network network = new BasicNetwork(new HurlStack());// Set up the network to use HttpURLConnection as the HTTP client.
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
    }

    public void translate(View view) {
        final String englishText = mEnglishEditText.getText().toString().trim();
        final String spanishText = translate(englishText);
        mSpansihTextView.setText(spanishText);
    }

    private String translate(final String text) {
        String result = null;
        try {
            List<String> list = readFromDb(text);
            if(list.isEmpty()) {
                Log.d(LOG_TAG, "Results from db were empty, making network request...");
                executeNetworkTranslate(text);
            } else {
                result = parseResultList(list);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Encountered an error while trying to translate " + text, e);
            result = ERROR_MESSAGE;
        }
        return result;
    }

    private String parseResultList(List<String> list) {
        StringBuilder results = new StringBuilder();
        for (String word : list) {
            results.append(word);
            results.append(", ");
        }
        return results.delete(results.length() - 2, results.length() - 1).toString();
    }

    private void executeNetworkTranslate(String text) {
        final String url = new TranslationUrl(mKey, text).getRequestUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final String result = new TranslationResponseJson(response).getText();
                        mSpansihTextView.setText(result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Log.e(LOG_TAG, "Encountered an error while sending request to url=" + url, e);
                        mSpansihTextView.setText(NETWORK_ERROR);
                    }
                });
        mRequestQueue.add(stringRequest);
    }

    private EditText.OnEditorActionListener createTextEditListener() {
        return new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(LOG_TAG, "onEditorAction: actionId=" + actionId + ", event=" + event.toString());
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Log.d(LOG_TAG, "onEditorAction: true, performClick()...");
                    mTranslateButton.performClick();
                    return true;
                }
                return false;
            }
        };
    }

    private List<String> readFromDb(final String enWord) {
        // Define a projection that specifies which columns from the database you will actually use after this query.
        String[] projection = {
                TranslationEntry.COLUMN_NAME_SPANISH_WORD
        };

        String selection = TranslationEntry.COLUMN_NAME_ENGLISH_WORD + " = ? ";
        String[] selectionArgs = { enWord };

        Cursor cursor = mTranslationDb.query(
                TranslationEntry.TABLE_NAME, // The table to query
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
