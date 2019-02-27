package com.example.android.booklistingapp;

import java.util.Arrays;

public class Book {

    private String mBookName;
    private String[] mBookauthor;
    private String mBookDate;

    public Book(String BookName, String[] BookAuthor, String BookDate) {
        mBookName = BookName;
        mBookauthor = BookAuthor;
        mBookDate = BookDate;
    }

    public String getBookName() {
        return mBookName;
    }

    public String[] getBookauthor() {
        return mBookauthor;
    }

    public String getmBookDate() {
        return mBookDate;
    }

}

