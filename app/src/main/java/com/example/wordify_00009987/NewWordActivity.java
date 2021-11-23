package com.example.wordify_00009987;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NewWordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        generateSpinnerOptions();
    }

    private void generateSpinnerOptions(){
        Spinner dropdown = findViewById(R.id.language_spinner);
        String[] items = new String[]{"spanish", "japanese", "german", "russian"};

        // basic adapter to describe how the items are displayed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }
}