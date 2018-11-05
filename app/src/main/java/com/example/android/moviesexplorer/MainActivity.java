package com.example.android.moviesexplorer;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.moviesexplorer.Adapters.FavoriteAdapter;
import com.example.android.moviesexplorer.Adapters.MovieAdapter;
import com.example.android.moviesexplorer.DataBase.AppDatabase;
import com.example.android.moviesexplorer.DataBase.FavoriteEntry;
import com.example.android.moviesexplorer.Utilities.APIConnection;
import com.example.android.moviesexplorer.Utilities.AppExecutors;
import com.example.android.moviesexplorer.Utilities.JSONMovies;
import com.example.android.moviesexplorer.Utilities.ViewModel;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.json.JSONException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterClickHandler ,FavoriteAdapter.ItemClickListener {
    JSONMovies jsonMoviesPopuler=null;
    JSONMovies jsonMoviesTopRated=null;
    JSONMovies jsonMovies=null;
    int SortFlag=1;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private FavoriteAdapter favoriteAdapter;
    GridLayoutManager layoutManager;

    private final String INTENT_KEY_POSTER="POSTER";
    private final String INTENT_KEY_RATE="RATE";
    private final String INTENT_KEY_OVERVIEW="OVERVIEW";
    private final String INTENT_KEY_RELEASE_DATE="RELEASE_DATE";
    private final String INTENT_KEY_TITLE="TITLE";
    private final String INTENT_KEY_ORIGINAL_TITLE="ORIGINAL_TITLE";
    private final String INTENT_KEY_ID="ID";
    private AppDatabase mDateBase;
    private final String SAVED_ADAPTER_TAG = "saved adapter";
    private  int SAVED_ADAPTER_VALUE = 1;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_ADAPTER_TAG,SAVED_ADAPTER_VALUE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

        if(savedInstanceState!= null )
            SAVED_ADAPTER_VALUE = savedInstanceState.getInt(SAVED_ADAPTER_TAG);

            try {
                URL url = APIConnection.getURL("popular");
                new FitchData().execute(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_posters_view);
        layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDateBase = AppDatabase.getInstance(getApplicationContext());
        favoriteAdapter = new FavoriteAdapter(this, this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        final LiveData<List<FavoriteEntry>>  entries = mDateBase.favoriteDao().loadAllMovies();
        ViewModel viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getmovies().observe(this, new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {
                favoriteAdapter.setFavorites(favoriteEntries);
                Log.i("INTERNET", "DataBase");
            }
        });

    }

    @Override
    public void onClick(int postion) {
        Intent intent = new Intent(this , DetailsActivity.class);
        try {
            intent.putExtra(INTENT_KEY_POSTER,jsonMovies.getPoster_path(postion));
            intent.putExtra(INTENT_KEY_ORIGINAL_TITLE,jsonMovies.getOriginal_title(postion));
            intent.putExtra(INTENT_KEY_TITLE,jsonMovies.getTitle(postion));
            intent.putExtra(INTENT_KEY_RATE,jsonMovies.getVote_average(postion));
            intent.putExtra(INTENT_KEY_RELEASE_DATE,jsonMovies.getRelease_date(postion));
            intent.putExtra(INTENT_KEY_OVERVIEW,jsonMovies.getOverview(postion));
            intent.putExtra(INTENT_KEY_ID,jsonMovies.getID(postion));
            Log.i("jawx2",jsonMovies.getID(postion)+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }

    @Override
    public void onItemClickListener(final int itemId) {
        Intent intent = new Intent(MainActivity.this,OfflineDetails.class);
        intent.putExtra(INTENT_KEY_ID,itemId);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    public class FitchData extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String data = null;
            try {
                data = APIConnection.getData(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String data) {

            if (data != null && !data.equals("")) {
                Log.i("INTERNET", "hi");
                try {
                    if(SortFlag==1) {
                        jsonMoviesPopuler = new JSONMovies(data);
                    }
                    else {
                        jsonMoviesTopRated = new JSONMovies(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(SortFlag==1) {
                    URL url = null;
                    try {
                        url = APIConnection.getURL("top_rated");
                        new FitchData().execute(url);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    SortFlag=0;
                }
                if (SortFlag == 0) {
                    switch (SAVED_ADAPTER_VALUE){
                        case 1: {
                            jsonMovies = jsonMoviesPopuler;
                            mMovieAdapter = new MovieAdapter(MainActivity.this, jsonMovies);
                            mRecyclerView.setAdapter(mMovieAdapter);
                            break;
                        }
                        case 2:{
                            jsonMovies = jsonMoviesTopRated;
                            mMovieAdapter = new MovieAdapter(MainActivity.this, jsonMovies);
                            mRecyclerView.setAdapter(mMovieAdapter);
                            break;
                        }
                        case 3:{
                            mRecyclerView.setAdapter(favoriteAdapter);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();

        if (itemClicked == R.id.Action_Show_most_popular) {

            mRecyclerView.setLayoutManager(layoutManager);
            jsonMovies=jsonMoviesPopuler;
            mMovieAdapter=new MovieAdapter(MainActivity.this,jsonMovies);
            mRecyclerView.setAdapter(mMovieAdapter);
            SAVED_ADAPTER_VALUE = 1;

            return true;
        }
        else if(itemClicked == R.id.Action_Show_Top_Rated){

            mRecyclerView.setLayoutManager(layoutManager);
            jsonMovies=jsonMoviesTopRated;
            mMovieAdapter=new MovieAdapter(MainActivity.this,jsonMovies);
            mRecyclerView.setAdapter(mMovieAdapter);
            SAVED_ADAPTER_VALUE = 2;

            return true;

        } else if(itemClicked == R.id.Action_Show_favorites){

            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(favoriteAdapter);
            SAVED_ADAPTER_VALUE = 3;

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
