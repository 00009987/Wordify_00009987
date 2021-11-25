package com.example.wordify_00009987;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DictionaryAdapter extends BaseAdapter {
    private final Cursor dictionary;
    private final Context context;
    private final SQLiteDatabase db;

    public DictionaryAdapter(Context context, Cursor dictionary) {
        this.dictionary = dictionary;
        this.context = context;

        DictionaryDbManager dbManager = new DictionaryDbManager(context);
        this.db = dbManager.getWritableDatabase();
    }

    @Override
    public int getCount() {
        return dictionary.getCount();
    }

    @Override
    public Object getItem(int position) {
        dictionary.moveToPosition(position);
        return dictionary;
    }

    @SuppressLint("Range")
    @Override
    public long getItemId(int position) {
        Cursor cursor = (Cursor) getItem(position);
        return cursor.getLong(cursor.getColumnIndex("_id"));
    }

    @SuppressLint({"Range", "SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.word_item, null);
        }

        // get word properties
        Cursor cursor = (Cursor) getItem(position);

        String originalWord = cursor.getString(cursor.getColumnIndex("originalWord"));
        String translation = cursor.getString(cursor.getColumnIndex("translation"));
        String language = cursor.getString((cursor.getColumnIndex("language")));
        String definition = cursor.getString(cursor.getColumnIndex("definition"));
        boolean isFavorite = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isFavorite")));

        // set properties to corresponding fields
        TextView originalWordText = convertView.findViewById(R.id.original_word_text);
        originalWordText.setText(originalWord + " — " + translation);

        TextView languageText = convertView.findViewById(R.id.language_text);
        languageText.setText(language);

        // attaching event listener on the word container
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setting the corresponding word properties into intent
                Intent i = new Intent(context, DetailedWordActivity.class);
                i.putExtra("word_id", getItemId(position));
                i.putExtra("originalWord", originalWord);
                i.putExtra("translation", translation);
                i.putExtra("language", language);
                i.putExtra("definition", definition);
                i.putExtra("isFavorite", isFavorite);
                context.startActivity(i);
            }
        });

        return convertView;
    }
}
