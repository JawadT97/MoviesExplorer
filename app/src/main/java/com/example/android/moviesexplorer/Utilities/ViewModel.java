
package com.example.android.moviesexplorer.Utilities;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import com.example.android.moviesexplorer.DataBase.AppDatabase;
import com.example.android.moviesexplorer.DataBase.FavoriteEntry;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    private LiveData<List<FavoriteEntry>> movies;

    public ViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        movies = database.favoriteDao().loadAllMovies();
    }

    public LiveData<List<FavoriteEntry>> getmovies() {
        return movies;
    }
}

