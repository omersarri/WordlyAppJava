package com.omersari.wordlyjavafinal.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.omersari.wordlyjavafinal.model.Word;


@Database(entities = {Word.class}, version = 1)
@TypeConverters(BooleanListConverters.class)
public abstract class WordDatabase extends RoomDatabase {
    public abstract WordDao wordDao();
}
