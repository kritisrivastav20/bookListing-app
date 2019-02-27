package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String GOOGLE_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=search+";
    private BookAdapter mAdapter;
    private static final int Book_loading = 1;
    private TextView emptyText;
    EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        ListView listView = (ListView) findViewById(R.id.list);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(mAdapter);
        emptyText = (TextView) findViewById(R.id.empty);
        listView.setEmptyView(emptyText);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        final ConnectivityManager netManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = netManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(Book_loading, null, this);
        } else {
            emptyText.setText("No network.");
        }


        inputSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {


                final ConnectivityManager netManager = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = netManager.getActiveNetworkInfo();

                if (networkInfo != null) {
                    if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                            (i == KeyEvent.KEYCODE_ENTER)) {

                        if (networkInfo.isConnected()) {

                            getLoaderManager().restartLoader(Book_loading, null, MainActivity.this);
                            Toast.makeText(MainActivity.this, "Search results",
                                    Toast.LENGTH_LONG).show();
                            return true;
                        } else {
                            Toast.makeText(MainActivity.this, "No Network",
                                    Toast.LENGTH_LONG).show();
                            return false;
                        }

                    } else {
                        return false;
                    }

                } else {
                    Toast.makeText(MainActivity.this, "No Network",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        });
    }

    private String getUserInput() {
        String editText = inputSearch.getText().toString();
        String formatUserInput = editText.trim().replaceAll("\\s+", "+");
        String url = GOOGLE_REQUEST_URL + formatUserInput;
        return url;
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        String url = getUserInput();
        return new BookLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        emptyText.setText("No books found.");
        mAdapter.clear();
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

}

