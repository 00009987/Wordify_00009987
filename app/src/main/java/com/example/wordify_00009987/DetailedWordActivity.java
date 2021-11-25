package com.example.wordify_00009987;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailedWordActivity extends AppCompatActivity {
    private long wordId;

    @SuppressLint({"Range", "SetTextI18n", "Recycle"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_word);

        // get elements
        TextView originalWordText = findViewById(R.id.detailed_original_word_text);
        TextView languageText = findViewById(R.id.detailed_language_text);
        TextView definitionText = findViewById(R.id.detailed_definition_text);

        // get the word from db
        DictionaryDbManager dbManager = new DictionaryDbManager(this);
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor word = db.query("dictionary", null, "_id = ?", new String[]{String.valueOf(wordId)}, null, null, null, null);

        // get the word details from the intent
        Intent i = getIntent();
        wordId = i.getLongExtra("word_id", 0);
        String originalWord = i.getStringExtra("originalWord");
        String translation = i.getStringExtra("translation");
        String language = i.getStringExtra("language");
        String definition = i.getStringExtra("definition");

        // set word details
        originalWordText.setText(originalWord + " — " + translation);
        languageText.setText(language);
        definitionText.setText(definition);
    }
}