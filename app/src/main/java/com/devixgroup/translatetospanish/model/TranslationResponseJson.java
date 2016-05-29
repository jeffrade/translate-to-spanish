package com.devixgroup.translatetospanish.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class TranslationResponseJson {

    public static final String LOG_TAG = TranslationResponseJson.class.getName();

    private String mJson;

    private String mText;

    private String mCode;

    public TranslationResponseJson(String json) {
        this.mJson = json;
        parseJson();
    }

    public String getText() {
        return mText;
    }

    public String getCode() {
        return mCode;
    }

    private void parseJson() {
        try {
            JSONObject jsonObject = new JSONObject(mJson);
            mText = jsonObject.getJSONArray("text").getString(0);
            mCode = jsonObject.getString("code");
            Log.d(LOG_TAG, mText);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing json response " + mJson, e);
        }
    }

}
