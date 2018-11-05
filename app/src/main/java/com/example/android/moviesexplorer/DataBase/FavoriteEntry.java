package com.example.android.moviesexplorer.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
@Entity(tableName = "favorites")

public class FavoriteEntry {
    @PrimaryKey()
    private int id;
    private String description;
    private String name;
    private String rate;
    @ColumnInfo(name = "release_date")
    private String releaseDate;

    public FavoriteEntry(int id, String description, String name, String releaseDate, String rate) {
        this.id = id;
        this.rate = rate;
        this.description = description;
        this.name = name;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
