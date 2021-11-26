package com.example.wordify_00009987;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.util.Log;
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

        // populating the sample data on app launch
        populateSampleData();
    }

    private void populateSampleData() {
        // connect to db
        DictionaryDbManager dbManager = new DictionaryDbManager(this);
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor dictionary = db.query("dictionary", null, null, null, null, null, null);

        if (dictionary.getCount() == 0) {
            // sample data
            String[] originalWords = new String[]{"言葉", "diccionario", "удивительный"};
            String[] translations = new String[]{"word", "dictionary", "amazing"};
            String[] definitions = new String[]{"a single distinct meaningful element of speech or writing, used with others (or sometimes alone) to form a sentence and typically shown with a space on either side when written or printed.", "gives the equivalent words in a different language, often also providing information about pronunciation, origin, and usage", "causing great surprise or wonder; astonishing"};
            String[] languages = new String[]{"japanese", "spanish", "russian"};
            Boolean[] isFavorites = new Boolean[]{true, false, false};
            Boolean[] isArchives = new Boolean[]{false, false, true};

            // insert samplate data into db
            ContentValues values = new ContentValues();

            for (int i = 0; i < originalWords.length; i++) {
                values.put("originalWord", originalWords[i]);
                values.put("translation", translations[i]);
                values.put("definition", definitions[i]);
                values.put("language", languages[i]);
                values.put("isFavorite", isFavorites[i]);
                values.put("isArchived", isArchives[i]);
                db.insert("dictionary", null, values);
            }
        }
    }

    public void openAboutActivity(View view) {
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }

    public void openNewWordActivity(View view) {
        Intent i = new Intent(this, NewWordActivity.class);
        startActivity(i);
    }

    public void openWordsListActivity(View view) {
        Intent i = new Intent(this, WordsListActivity.class);
        i.putExtra("type", "words");
        startActivity(i);
    }

    public void openFavoritesListActivity(View view) {
        Intent i = new Intent(this, WordsListActivity.class);
        i.putExtra("type", "favorites");
        startActivity(i);
    }

    public void openArchivesListActivity(View view) {
        Intent i = new Intent(this, WordsListActivity.class);
        i.putExtra("type", "archives");
        startActivity(i);
    }
}