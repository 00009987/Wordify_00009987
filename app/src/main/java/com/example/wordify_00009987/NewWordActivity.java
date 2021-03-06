package com.example.wordify_00009987;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

public class NewWordActivity extends AppCompatActivity {
    private final String[] options = new String[]{"spanish", "japanese", "german", "russian"};

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        generateSpinnerOptions();

        // get the word id, archived status from intent
        Intent i = getIntent();
        long wordId = i.getLongExtra("word_id", 0);

        // get form elements
        Button submitBtn = findViewById(R.id.submitBtn);
        EditText originalWordText = findViewById(R.id.originalWord);
        EditText definitionText = findViewById(R.id.definition);
        EditText translationText = findViewById(R.id.translation);
        Spinner languageSpinner = findViewById(R.id.language_spinner);
        CheckBox isFavoriteCheckbox = findViewById(R.id.isFavorite);

        // check if the form should be in edit mode
        if (wordId != 0) {
            // get word properties from intent
            String originalWord = i.getStringExtra("originalWord");
            String translation = i.getStringExtra("translation");
            String definition = i.getStringExtra("definition");
            boolean isFavorite = i.getBooleanExtra("isFavorite", false);
            int languageIndex = Arrays.asList(options).indexOf(i.getStringExtra("language"));

            // set values on corresponding elements
            originalWordText.setText(originalWord);
            translationText.setText(translation);
            definitionText.setText(definition);
            languageSpinner.setSelection(languageIndex);
            isFavoriteCheckbox.setChecked(isFavorite);
            submitBtn.setText("save");
            setTitle("edit the word");
        }

        // add click listener to the form button
        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // get user inputs from the edit texts
                String originalWord = originalWordText.getText().toString();
                String translation = translationText.getText().toString();
                String definition = definitionText.getText().toString();
                String language = languageSpinner.getSelectedItem().toString();
                Boolean isFavorite = isFavoriteCheckbox.isChecked();

                // validate inputs
                if (originalWord.isEmpty() || translation.isEmpty() || definition.isEmpty())
                    Toast.makeText(NewWordActivity.this, "please, fill all the required input fields", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isDigitsOnly(originalWord) || TextUtils.isDigitsOnly(translation) || TextUtils.isDigitsOnly(definition))
                    Toast.makeText(NewWordActivity.this, "please, enter only alphabetical characters", Toast.LENGTH_SHORT).show();
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

                    if (wordId == 0) {
                        // set new word archived status
                        values.put("isArchived", false);

                        // create a new word
                        long id = db.insert("dictionary", null, values);

                        // check if word was added to the db
                        if (id > 0) {
                            Toast.makeText(NewWordActivity.this, "your word is successfully created", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(NewWordActivity.this, "something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // set existing word archived status
                        values.put("isArchived", i.getBooleanExtra("isArchived", false));

                        // update the existing word
                        db.update("dictionary", values, "_id = ?", new String[]{String.valueOf(wordId)});
                        Toast.makeText(NewWordActivity.this, "the word is successfully updated", Toast.LENGTH_LONG).show();

                        finish();
                    }
                }
            }
        });
    }

    public void generateSpinnerOptions() {
        Spinner dropdown = findViewById(R.id.language_spinner);

        // basic adapter to describe how the items are displayed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_style, options);
        dropdown.setAdapter(adapter);
    }
}