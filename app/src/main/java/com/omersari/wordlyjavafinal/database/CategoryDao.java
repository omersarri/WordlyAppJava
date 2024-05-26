package com.omersari.wordlyjavafinal.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.omersari.wordlyjavafinal.model.Category;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Category")
    Flowable<List<Category>> getAll();

    @Insert
    Completable insert(Category category);

    @Delete
    Completable delete(Category category);



}
