package com.example.wordify_00009987;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DictionaryAdapter extends BaseAdapter {
    private final Cursor dictionary;
    private final Context context;
    private final SQLiteDatabase db;
    private final String type;

    public DictionaryAdapter(Context context, Cursor dictionary, String type) {
        this.dictionary = dictionary;
        this.context = context;
        this.type = type;

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
            convertView = inflater.inflate(R.layout.activity_word_item, null);
        }

        // get word properties
        Cursor cursor = (Cursor) getItem(position);

        String originalWord = cursor.getString(cursor.getColumnIndex("originalWord"));
        String translation = cursor.getString(cursor.getColumnIndex("translation"));
        String language = cursor.getString((cursor.getColumnIndex("language")));
        String definition = cursor.getString(cursor.getColumnIndex("definition"));
        String isFavorite = cursor.getString(cursor.getColumnIndex("isFavorite"));
        String isArchived = cursor.getString(cursor.getColumnIndex("isArchived"));

        // set properties to corresponding fields
        TextView originalWordText = convertView.findViewById(R.id.original_word_text);
        originalWordText.setText(originalWord + " — " + translation);

        TextView languageText = convertView.findViewById(R.id.language_text);
        languageText.setText(language);

        ImageView icon = convertView.findViewById(R.id.word_icon);

        // set icon according to the type
        if (type.equals("favorites"))
            icon.setImageResource(R.drawable.heart_icon_red);
        else if (type.equals(("archives")))
            icon.setImageResource(R.drawable.archive_icon_yellow);

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
                i.putExtra("isArchived", isArchived);
                context.startActivity(i);
            }
        });

        return convertView;
    }
}
