package com.omersari.wordlyjavafinal.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;


import com.omersari.wordlyjavafinal.model.Word;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface WordDao {

    @Query("SELECT * FROM Word")
    Flowable<List<Word>> getAll();
    @Query("SELECT * FROM Word WHERE categoryId == (:selectedCategoryId)")
    Flowable<List<Word>> getWithCategory(int selectedCategoryId);

    @Upsert
    Completable insert(Word word);

    @Delete
    Completable delete(Word word);

    @Query("DELETE FROM Word WHERE categoryId == (:selectedCategoryId)")
    Completable deleteListItems(int selectedCategoryId);


}
