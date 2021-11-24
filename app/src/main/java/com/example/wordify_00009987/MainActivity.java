package com.example.wordify_00009987;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openAboutActivity(View view) {
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }

    public void openNewWordActivity(View view) {
        Intent i = new Intent(this, NewWordActivity.class);
        startActivity(i);
    }
}