package com.example.wordify_00009987;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class WordsListActivity extends AppCompatActivity {
    private final String[] options = new String[]{"spanish", "japanese", "german", "russian"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list);

        onWordListStart();
        generateSpinnerOptions();
    }

    private void onWordListStart() {
        // connect to db
        DictionaryDbManager dbManager = new DictionaryDbManager(this);
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor dictionary = null;

        // get type of the word list
        Intent i = getIntent();
        String type = i.getStringExtra("type");

        if (type.equals("words")) {
            setTitle("all words");

            // get all the words from db
            dictionary = db.query("dictionary", null, null, null, null, null, null);
        } else if (type.equals("favorites")) {
            setTitle("favorites");

            // get all favorites
            dictionary = db.query("dictionary", null, "isFavorite = 1", null, null, null, null);
        } else if (type.equals("archives")) {
            setTitle("archives");

            // get all favorites
            dictionary = db.query("dictionary", null, "isArchived = 1", null, null, null, null);
        }

        // display words in listview
        DictionaryAdapter dcAdapter = new DictionaryAdapter(this, dictionary, type);
        ListView listView = findViewById(R.id.words_list);
        listView.setAdapter(dcAdapter);
    }

    public void generateSpinnerOptions() {
        Spinner dropdown = findViewById(R.id.filter_spinner);

        // basic adapter to describe how the items are displayed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_style, options);
        dropdown.setAdapter(adapter);
    }

    public void filterByLanguage(View view) {
        Spinner filterSpinner = findViewById(R.id.filter_spinner);
        String selectedLanguage = filterSpinner.getSelectedItem().toString();
        Log.d("language", selectedLanguage);
    }
}