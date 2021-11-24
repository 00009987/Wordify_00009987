package com.example.wordify_00009987;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewWordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        generateSpinnerOptions();

        // add click listener to the form button
        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // get user inputs from the edit texts
                String originalWord = ((EditText) findViewById(R.id.originalWord)).getText().toString();
                String translation = ((EditText) findViewById(R.id.translation)).getText().toString();
                String definition = ((EditText) findViewById(R.id.definition)).getText().toString();
                String language = ((Spinner) findViewById(R.id.language_spinner)).getSelectedItem().toString();
                Boolean isFavorite = ((CheckBox) findViewById(R.id.isFavorite)).isChecked();

                // validate inputs
                if (originalWord.isEmpty() || translation.isEmpty() || definition.isEmpty())
                    Toast.makeText(NewWordActivity.this, "Please fill all the required input fields", Toast.LENGTH_SHORT).show();
                else {
                    // connect to db
                    DictionaryDbManager dbManager = new DictionaryDbManager(NewWordActivity.this);
                    SQLiteDatabase db = dbManager.getWritableDatabase();

                    // insert user values into db
                    ContentValues values = new ContentValues();
                    values.put("originalWord", originalWord);
                    values.put("translation", translation);
                    values.put("definition", definition);
                    values.put("language", language);
                    values.put("isFavorite", isFavorite);
                    values.put("isArchived", false);

                    long id = db.insert("dictionary", null, values);

                    // check if word was added to the db
                    if (id > 0) {
                        Toast.makeText(NewWordActivity.this, "Your word was successfully added", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(NewWordActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void generateSpinnerOptions() {
        Spinner dropdown = findViewById(R.id.language_spinner);
        String[] items = new String[]{"spanish", "japanese", "german", "russian"};

        // basic adapter to describe how the items are displayed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }


}