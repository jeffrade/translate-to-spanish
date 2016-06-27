package com.devixgroup.translatetospanish;

import java.io.IOException;
import java.util.Properties;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();
    public static final String K = "k";

    private Properties mProperties;
    private String mKey;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "#################### onSaveInstanceState:");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "#################### onCreate:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        Fragment mainFragment = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putString(K, mKey);
        mainFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_frame, mainFragment, MainActivityFragment.LOG_TAG)
                .commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void initialize() {
        initProperties();
        initToolBar();
    }

    private void initToolBar() {
        Toolbar appToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appToolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, " onOptionsItemSelected: entering... item=" + item);
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_quit) {
            if(!isFinishing()) {
                finish();
            }
            return true;
        } else if (id == R.id.action_about) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, new AboutFragment(), AboutFragment.LOG_TAG)
                    .addToBackStack(null)
                    .commit();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(LOG_TAG, "#################### onSupportNavigateUp:");
        getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "#################### onStart:");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "#################### onStop:");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "#################### onStop:");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "#################### onPause:");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "#################### onResume:");
        super.onResume();
    }
}
