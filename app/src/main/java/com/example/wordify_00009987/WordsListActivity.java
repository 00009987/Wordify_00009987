package com.example.wordify_00009987;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class WordsListActivity extends AppCompatActivity {
    private final String[] options = new String[]{"spanish", "japanese", "german", "russian"};
    private SQLiteDatabase db;
    private String type;
    private TextView alertTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list);

        // get alert text for warnings
        alertTextView = findViewById(R.id.alert_text);

        onWordListStart();
        generateSpinnerOptions();
    }

    private void generateSpinnerOptions() {
        Spinner dropdown = findViewById(R.id.filter_spinner);

        // basic adapter to describe how the items are displayed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_style, options);
        dropdown.setAdapter(adapter);
    }

    private void renderListView(Cursor words) {
        // display words in listview
        DictionaryAdapter dcAdapter = new DictionaryAdapter(this, words, type);
        ListView listView = findViewById(R.id.words_list);
        listView.setAdapter(dcAdapter);
    }

    private Cursor getWords(String command) {
        return db.query("dictionary", null, command, null, null, null, null);
    }

    private void showAlertMsg(@NonNull Cursor data, String msg) {
        // show corresponding alert msg when there are no data
        if (data.getCount() == 0) {
            alertTextView.setText(msg);
        } else {
            alertTextView.setText("");
        }
    }

    private void onWordListStart() {
        // connect to db
        DictionaryDbManager dbManager = new DictionaryDbManager(this);
        db = dbManager.getReadableDatabase();
        Cursor dictionary = null;

        // get type of the word list
        Intent i = getIntent();
        type = i.getStringExtra("type");

        // get words and set title according to their type
        switch (type) {
            case "words":
                setTitle("all words");
                dictionary = getWords(null);
                break;
            case "favorites":
                setTitle("favorites");
                dictionary = getWords("isFavorite = 1");
                break;
            case "archives":
                setTitle("archives");
                dictionary = getWords("isArchived = 1");
                break;
        }

        renderListView(dictionary);
    }

    public void filterByLanguage(View view) {
        // get filtering word
        Spinner filterSpinner = findViewById(R.id.filter_spinner);
        String selectedLanguage = filterSpinner.getSelectedItem().toString();
        Cursor filteredWords = null;

        // get filtered words from db according to the type
        if (type.equals("favorites"))
            filteredWords = getWords("(language like '%" + selectedLanguage + "%' and isFavorite = 1)");
        else if (type.equals("archives"))
            filteredWords = getWords("language like '%" + selectedLanguage + "%' and isArchived = 1");
        else
            filteredWords = getWords("language like '%" + selectedLanguage + "%'");

        showAlertMsg(filteredWords, "no words found in " + selectedLanguage + " to filter");

        renderListView(filteredWords);
    }

    public void search(View view) {
        // get user input
        EditText searchInput = findViewById(R.id.search_input);
        String searchedWord = searchInput.getText().toString();

        // validate user input
        if (searchedWord.isEmpty())
            Toast.makeText(this, "search input cannot be empty", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isDigitsOnly(searchedWord))
            Toast.makeText(this, "search input cannot be numbers", Toast.LENGTH_SHORT).show();
        else {
            Cursor searchedWords = null;

            // get searched words according to type
            if (type.equals("favorites"))
                searchedWords = getWords("(originalWord like '%" + searchedWord + "%' or translation like '%" + searchedWord + "%') and isFavorite = 1");
            else if (type.equals("archives"))
                searchedWords = getWords("(originalWord like '%" + searchedWord + "%' or translation like '%" + searchedWord + "%') and isArchived = 1");
            else
                searchedWords = getWords("originalWord like '%" + searchedWord + "%' or translation like '%" + searchedWord + "%'");

            showAlertMsg(searchedWords, "there are no words for " + searchedWord);

            renderListView(searchedWords);
        }
    }
}