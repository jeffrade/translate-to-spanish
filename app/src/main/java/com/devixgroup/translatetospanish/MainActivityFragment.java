package com.devixgroup.translatetospanish;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.devixgroup.translatetospanish.model.TranslationResponseJson;
import com.devixgroup.translatetospanish.model.TranslationUrl;
import com.devixgroup.translatetospanish.network.VolleySingleton;
import com.devixgroup.translatetospanish.persist.sqlite.TranslationDAO;

import java.util.List;

public class MainActivityFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = MainActivityFragment.class.getName();
    private static final String ERROR_MESSAGE = "Sorry, could not translate";
    private static final String NETWORK_ERROR = "Network error!";

    private EditText mEnglishEditText;
    private TextView mSpansihTextView;
    private Button mTranslateButton;
    private TranslationDAO mTranslationDao;

    public MainActivityFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTranslationDao = new TranslationDAO(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mEnglishEditText = (EditText) view.findViewById(R.id.textInput);
        mEnglishEditText.setOnEditorActionListener(createTextEditListener());
        mSpansihTextView = (TextView) view.findViewById(R.id.textBox);
        mTranslateButton = (Button) view.findViewById(R.id.translateButton);
        mTranslateButton.setOnClickListener(this);
        initUrlLinks(view);
        initViews();
        return view;
    }

    private void initViews() {
        Log.d(LOG_TAG, "in initViews...");
        mEnglishEditText.setText(R.string.initialEnglishWord);
        mSpansihTextView.setText(R.string.initialWordTranslated);
    }

    @Override
    public void onClick(View view) {
        final String englishText = mEnglishEditText.getText().toString().trim();
        final String spanishText = translate(englishText);
        mSpansihTextView.setText(spanishText);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(LOG_TAG, "in onAttach...");
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        Log.d(LOG_TAG, "in onDestroyView...");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "in onDestroy...");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(LOG_TAG, "in onDetach...");
        super.onDetach();
    }

    @Override
    public void onStop() {
        Log.d(LOG_TAG, "in onStop...");
        super.onStop();
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "in onPause...");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "in onResume...");
        super.onResume();
        initViews();
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG, "in onStart...");
        super.onStart();
    }

    private void initUrlLinks(View view) {
        TextView translateServiceLink = (TextView) view.findViewById(R.id.translateServicePlug);
        translateServiceLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private String translate(final String text) {
        mSpansihTextView.setText("");
        String result = null;
        try {
            List<String> list = mTranslationDao.readFromDb(text);
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

    private EditText.OnEditorActionListener createTextEditListener() {
        return new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(LOG_TAG, "onEditorAction: actionId=" + actionId + ", event=" + event);
                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Log.d(LOG_TAG, "onEditorAction: true, performClick()...");
                    mTranslateButton.performClick();
                    return true;
                }
                return false;
            }
        };
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
        final String url = new TranslationUrl(getArguments().getString(MainActivity.K), text).getRequestUrl();
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
                        Toast.makeText(getActivity(), NETWORK_ERROR, Toast.LENGTH_LONG).show();
                    }
                });

        VolleySingleton.getInstance(getActivity()).add(stringRequest);
    }
}
