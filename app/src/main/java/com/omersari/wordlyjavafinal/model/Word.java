package com.omersari.wordlyjavafinal.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Entity
public class Word implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "color")
    private int color;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "meaning")
    private String meaning;
    @ColumnInfo(name = "synonym")
    private String synonym;
    @ColumnInfo(name = "example")
    private String example;
    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "categoryId")
    private int categoryId;

    @ColumnInfo(name = "stats")
    private List<Boolean> stats = new ArrayList<>();




    public Word() {

    }

    public List<Boolean> getStats() {
        return stats;
    }

    public void setStats(List<Boolean> stats) {
        this.stats = stats;
    }
    public void addStat(Boolean stat) {
        stats.add(stat);
    }
    public void removeStat(int index) {
        stats.remove(index);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


}
