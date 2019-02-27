package com.example.android.booklistingapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookAdapter extends ArrayAdapter<Book> {

    static class ViewHolder {
        TextView bookName;
        TextView bookAuthor;
        TextView bookDate;
    }

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Book currentBook = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_format, parent, false);
            holder = new ViewHolder();
            holder.bookName = (TextView) convertView.findViewById(R.id.book_title);
            holder.bookAuthor = (TextView) convertView.findViewById(R.id.author_name);
            holder.bookDate = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bookName.setText(currentBook.getBookName());
        holder.bookAuthor.setText(TextUtils.join(", ", currentBook.getBookauthor()));
        holder.bookDate.setText(currentBook.getmBookDate());

        return convertView;
    }

}




