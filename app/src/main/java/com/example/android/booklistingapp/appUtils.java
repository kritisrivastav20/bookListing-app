package com.example.android.booklistingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class appUtils {

    public static final String LOG_TAG = appUtils.class.getSimpleName();

    private static final String ITEMS = "items";
    private static final String VOLUME_INFO = "volumeInfo";
    private static final String TITLE = "title";
    private static final String AUTHORS = "authors";
    private static final String DATE = "publishedDate";

    public static List<Book> getBookinfo(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the books JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Book> extractFeatureFromJson(String bookJsonStr) {

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject bookJsonObject = new JSONObject(bookJsonStr);
            if (!bookJsonObject.isNull(ITEMS)) {

                JSONArray bookJsonArray = bookJsonObject.getJSONArray(ITEMS);

                for (int i = 0; i < bookJsonArray.length(); i++) {

                    JSONObject itemJsonObject = bookJsonArray.getJSONObject(i);
                    JSONObject volumeInfoObject = itemJsonObject.getJSONObject(VOLUME_INFO);

                    String mBookName = volumeInfoObject.getString(TITLE);
                    String mBookDate = volumeInfoObject.getString(DATE);

                    String[] mBookauthor = new String[]{"No Authors"};

                    if (!volumeInfoObject.isNull(AUTHORS)) {
                        JSONArray authorsArray = volumeInfoObject.getJSONArray(AUTHORS);
                        Log.d(LOG_TAG, "authors #" + authorsArray.length());
                        mBookauthor = new String[authorsArray.length()];
                        for (int j = 0; j < authorsArray.length(); j++) {
                            mBookauthor[j] = authorsArray.getString(j);
                        }
                    }
                    books.add(new Book(mBookName, mBookauthor, mBookDate));
                }
            } else {
                books = null;
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, e.toString());
        }
        return books;
    }
}


