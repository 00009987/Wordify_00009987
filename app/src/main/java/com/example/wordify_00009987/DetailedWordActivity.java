package com.example.wordify_00009987;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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
    private boolean isFavorite;
    private boolean isArchived;

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
        isFavorite = i.getStringExtra("isFavorite").equalsIgnoreCase("1");
        isArchived = i.getStringExtra("isArchived").equalsIgnoreCase("1");

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
        i.putExtra("isArchived", isArchived);
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

    public void changeFavoriteStatus(View view) {
        // connect to db
        DictionaryDbManager dbManager = new DictionaryDbManager(this);
        SQLiteDatabase db = dbManager.getWritableDatabase();

        if (isArchived) {
            Toast.makeText(this, "archived word cannot be added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            // change favorite status
            isFavorite = !isFavorite;

            // update word properties in db
            ContentValues values = new ContentValues();
            values.put("originalWord", originalWord);
            values.put("translation", translation);
            values.put("definition", definition);
            values.put("language", language);
            values.put("isFavorite", isFavorite);
            values.put("isArchived", isArchived);

            // update the existing word
            db.update("dictionary", values, "_id = ?", new String[]{String.valueOf(wordId)});

            // show msg according to status
            if (isFavorite)
                Toast.makeText(this, "the word is added to favorites", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "the word is removed from favorites", Toast.LENGTH_SHORT).show();

            // open main activity
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
        }
    }

    public void changeArchivedStatus(View view) {
        // connect to db
        DictionaryDbManager dbManager = new DictionaryDbManager(this);
        SQLiteDatabase db = dbManager.getWritableDatabase();

        if (isFavorite)
            Toast.makeText(this, "favorite word cannot be added to archives", Toast.LENGTH_SHORT).show();
        else {
            // change archived status
            isArchived = !isArchived;

            // update word properties in db
            ContentValues values = new ContentValues();
            values.put("originalWord", originalWord);
            values.put("translation", translation);
            values.put("definition", definition);
            values.put("language", language);
            values.put("isFavorite", isFavorite);
            values.put("isArchived", isArchived);

            // update the existing word
            db.update("dictionary", values, "_id = ?", new String[]{String.valueOf(wordId)});

            // show msg according to status
            if (isArchived)
                Toast.makeText(this, "the word is added to archives", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "the word is removed from archives", Toast.LENGTH_SHORT).show();

            // open main activity
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
        }
    }
}