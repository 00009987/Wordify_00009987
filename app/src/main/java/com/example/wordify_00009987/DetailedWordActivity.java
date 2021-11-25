package com.example.wordify_00009987;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedWordActivity extends AppCompatActivity {
    private long wordId;
    private String originalWord;
    private String translation;
    private String language;
    private String definition;
    private String isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_word);

        renderSelectedWord();
    }

    @SuppressLint({"Range", "SetTextI18n", "Recycle"})
    private void renderSelectedWord() {
        // get elements
        TextView originalWordText = findViewById(R.id.detailed_original_word_text);
        TextView languageText = findViewById(R.id.detailed_language_text);
        TextView definitionText = findViewById(R.id.detailed_definition_text);

        // get the word details from the intent
        Intent i = getIntent();
        wordId = i.getLongExtra("word_id", 0);
        originalWord = i.getStringExtra("originalWord");
        translation = i.getStringExtra("translation");
        language = i.getStringExtra("language");
        definition = i.getStringExtra("definition");
        isFavorite = i.getStringExtra("isFavorite");

        // set word details
        originalWordText.setText(originalWord + " — " + translation);
        languageText.setText(language);
        definitionText.setText(definition);
        setTitle(originalWord);
    }

    public void openEditWordActivity(View view) {
        // setting the corresponding word properties into intent
        Intent i = new Intent(this, NewWordActivity.class);
        i.putExtra("word_id", wordId);
        i.putExtra("originalWord", originalWord);
        i.putExtra("translation", translation);
        i.putExtra("language", language);
        i.putExtra("definition", definition);
        i.putExtra("isFavorite", isFavorite);
        startActivity(i);
    }

    public void deleteSelectedWord(View view) {
        // connect to db
        DictionaryDbManager dbManager = new DictionaryDbManager(this);
        SQLiteDatabase db = dbManager.getWritableDatabase();

        // delete the word from db
        db.delete("dictionary", "_id = ?", new String[]{String.valueOf(wordId)});

        // show success msg & jump to home screen
        Toast.makeText(this, "the word is successfully deleted", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}