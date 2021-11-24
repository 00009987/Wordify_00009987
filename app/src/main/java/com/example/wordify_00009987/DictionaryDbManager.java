package com.example.wordify_00009987;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DictionaryDbManager extends SQLiteOpenHelper {
    public DictionaryDbManager(@Nullable Context context){
        super(context, "dictionary.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE dictionary (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, originalWord TEXT NOT NULL, translation TEXT NOT NULL, definition TEXT NOT NULL, language TEXT NOT NULL, isFavorite BOOLEAN NOT NULL, isArchived NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
