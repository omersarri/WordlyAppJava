package com.omersari.wordlyjavafinal.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.omersari.wordlyjavafinal.model.Category;


@Database(entities = {Category.class}, version = 1)
public abstract class CategoryDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
}
