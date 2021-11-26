package com.example.wordify_00009987;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class WordsListActivity extends AppCompatActivity {
    private Cursor dictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list);

        // connect to db
        DictionaryDbManager dbManager = new DictionaryDbManager(this);
        SQLiteDatabase db = dbManager.getReadableDatabase();

        // get type of the word list
        Intent i = getIntent();
        String type = i.getStringExtra("type");

        if(type.equals("words")) {
            setTitle("all words");

            // get all the words from db
            dictionary = db.query("dictionary", null, null, null, null, null, null);
        }else if (type.equals("favorites")) {
            setTitle("favorites");

            // get all favorites
            dictionary = db.query("dictionary", null, "isFavorite = 1", null, null, null, null);
        }else if (type.equals("archives")){
            setTitle("archives");

            // get all favorites
            dictionary = db.query("dictionary", null, "isArchived = 1", null, null, null, null);
        }

        // display words in listview
        DictionaryAdapter dcAdapter = new DictionaryAdapter(this, dictionary, type);
        ListView listView = findViewById(R.id.words_list);
        listView.setAdapter(dcAdapter);
    }
}