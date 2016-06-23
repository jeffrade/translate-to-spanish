package com.devixgroup.translatetospanish.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

public class VolleySingleton {

    private static final String LOG_TAG = VolleySingleton.class.getName();
    private static final int VOLLEY_1MB_CAP = 1024 * 1024;

    private RequestQueue mRequestQueue;
    private Context mContext;

    public static VolleySingleton VOLLEY_INSTANCE;

    public static synchronized VolleySingleton getInstance(Context context) {
        Log.d(LOG_TAG, " getInstance: entering...");
        if(VOLLEY_INSTANCE == null) {
            VOLLEY_INSTANCE = new VolleySingleton(context);
        }

        return VOLLEY_INSTANCE;
    }

    private VolleySingleton(Context context) {
        super();
        mContext = context;
        initVolley();
    }

    private void initVolley() {
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), VOLLEY_1MB_CAP); // Instantiate the cache, 1MB cap
        Network network = new BasicNetwork(new HurlStack());// Set up the network to use HttpURLConnection as the HTTP client.
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
    }

    public void add(StringRequest request) {
        Log.d(LOG_TAG, " add: entering...");
        mRequestQueue.add(request);
    }

}
