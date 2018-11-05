package com.example.android.moviesexplorer.DataBase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;
@Dao

public interface FavoriteDao {

        @Query("SELECT * FROM favorites ORDER BY id ")
        LiveData<List<FavoriteEntry>> loadAllMovies();

        @Query("SELECT name FROM favorites WHERE id = :id")
        String getName(int id);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertMovie(FavoriteEntry taskEntry);

        @Query("DELETE FROM favorites WHERE id = :id")
        void deleteById(int id);

        @Query("SELECT * FROM favorites WHERE id = :id")
        FavoriteEntry loadMovieById(int id);
}
