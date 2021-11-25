package com.example.wordify_00009987;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class WordsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list);

        // connect to db
        DictionaryDbManager dbManager = new DictionaryDbManager(this);
        SQLiteDatabase db = dbManager.getReadableDatabase();

        // get all the words from db
        Cursor dictionary = db.query("dictionary", null, null, null, null, null, null);

        // display words in listview
        DictionaryAdapter dcAdapter = new DictionaryAdapter(this, dictionary);
        ListView listView = findViewById(R.id.words_list);
        listView.setAdapter(dcAdapter);
    }
}