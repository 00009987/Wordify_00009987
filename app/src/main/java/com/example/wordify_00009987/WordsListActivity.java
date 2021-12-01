package com.example.wordify_00009987;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class WordsListActivity extends AppCompatActivity {
    private final String[] options = new String[]{"spanish", "japanese", "german", "russian"};
    private SQLiteDatabase db;
    private String type;
    private TextView filterAlert;

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
        db = dbManager.getReadableDatabase();
        Cursor dictionary = null;

        // get type of the word list
        Intent i = getIntent();
        type = i.getStringExtra("type");

        switch (type) {
            case "words":
                setTitle("all words");
                // get all the words from db
                dictionary = db.query("dictionary", null, null, null, null, null, null);
                break;
            case "favorites":
                setTitle("favorites");
                // get all favorites
                dictionary = db.query("dictionary", null, "isFavorite = 1", null, null, null, null);
                break;
            case "archives":
                setTitle("archives");
                // get all favorites
                dictionary = db.query("dictionary", null, "isArchived = 1", null, null, null, null);
                break;
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

    @SuppressLint({"Recycle", "SetTextI18n"})
    public void filterByLanguage(View view) {
        // get filtering word
        Spinner filterSpinner = findViewById(R.id.filter_spinner);
        String selectedLanguage = filterSpinner.getSelectedItem().toString();
        Cursor filteredWords = null;

        // get filtered words from db according to the type
        if (type.equals("favorites"))
            filteredWords = db.query("dictionary", null, "language = ? and isFavorite = 1", new String[]{selectedLanguage}, null, null, null, null);
        else if (type.equals("archives"))
            filteredWords = db.query("dictionary", null, "language = ? and isArchived = 1", new String[]{selectedLanguage}, null, null, null, null);
        else
            filteredWords = db.query("dictionary", null, "language = ?", new String[]{selectedLanguage}, null, null, null, null);

        // show user alert msg when there are no words of selected language
        filterAlert = findViewById(R.id.alert_text);

        if (filteredWords.getCount() == 0) {
            filterAlert.setText("no words found in " + selectedLanguage + " to filter");
        } else {
            filterAlert.setText("");
        }
        // display words in listview
        DictionaryAdapter dcAdapter = new DictionaryAdapter(this, filteredWords, type);
        ListView listView = findViewById(R.id.words_list);
        listView.setAdapter(dcAdapter);
    }

    public void Search(View view) {

    }
}