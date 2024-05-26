package com.omersari.wordlyjavafinal.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Category implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private String categoryName;

    public Category() {

    }

    public int getId() {
        return id;
    }
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
