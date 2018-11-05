package com.example.android.moviesexplorer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesexplorer.Adapters.ReviewAdapter;
import com.example.android.moviesexplorer.Adapters.TrailersAdapter;
import com.example.android.moviesexplorer.DataBase.AppDatabase;
import com.example.android.moviesexplorer.DataBase.FavoriteEntry;
import com.example.android.moviesexplorer.Utilities.APIConnection;
import com.example.android.moviesexplorer.Utilities.AppExecutors;
import com.example.android.moviesexplorer.Utilities.JSONMovies;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailsActivity extends AppCompatActivity  implements TrailersAdapter.TrailersAdapterClickHandler {
    private final String INTENT_KEY_POSTER="POSTER";

    private final String INTENT_KEY_ORIGINAL_TITLE="ORIGINAL_TITLE";
    private final String INTENT_KEY_RATE="RATE";
    private final String INTENT_KEY_OVERVIEW="OVERVIEW";
    private final String INTENT_KEY_RELEASE_DATE="RELEASE_DATE";
    private final String INTENT_KEY_TITLE="TITLE";
    private final String INTENT_KEY_ID="ID";
    private final String KEY_POSTER_PATH="http://image.tmdb.org/t/p/w185/";
    private final String TAG_ADD_TO_FAV = "Add to favorites";
    private final String TAG_REMOVE_FROM_FAV = "Remove from favorites";

    ImageView imageView ;
    Button favoriteButton ;
    TextView mReleasDate;
    TextView mRate;
    TextView mOriginalName;
    TextView mOverview;
    JSONMovies movieVideos , movieReviews;

    private RecyclerView mRecyclerView;
    private TrailersAdapter mTrilerAdapter;
    private RecyclerView mRecyclerViewREVIEW;
    private ReviewAdapter mReviewAdapter;
    private AppDatabase mDateBase;
    Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        intent = getIntent();
        imageView=(ImageView) findViewById(R.id.iv_details_poster);
        mReleasDate = (TextView) findViewById(R.id.tv_date);
        mRate = (TextView) findViewById(R.id.tv_rate);
        mOriginalName=(TextView) findViewById(R.id.tv_original_name);
        mOverview=(TextView) findViewById(R.id.tv_overview);
        favoriteButton = (Button) findViewById(R.id.bt_fav);
        filldata();

        final String name = intent.getStringExtra(INTENT_KEY_TITLE);
        final String desc = mOverview.getText().toString();
        final String rate = mRate.getText().toString();
        final String date = mReleasDate.getText().toString();
        final int id = Integer.parseInt(intent.getStringExtra(INTENT_KEY_ID));
        final FavoriteEntry entry = new FavoriteEntry(id,desc,name,date,rate);

        mDateBase=AppDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final String  nameFlag = mDateBase.favoriteDao().getName(id);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(nameFlag == null ) favoriteButton.setText(TAG_ADD_TO_FAV);
                        else favoriteButton.setText(TAG_REMOVE_FROM_FAV);

                    }
                });
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(favoriteButton.getText().toString().equals(TAG_ADD_TO_FAV)) {
                            mDateBase.favoriteDao().insertMovie(entry);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    favoriteButton.setText(TAG_REMOVE_FROM_FAV);
                                }
                            });
                        }
                        else {
                            mDateBase.favoriteDao().deleteById(id);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    favoriteButton.setText(TAG_ADD_TO_FAV);
                                }
                            });
                        }
                    }
                });
                if(favoriteButton.getText().toString().equals(TAG_ADD_TO_FAV))
                     Toast.makeText(DetailsActivity.this, "I LIKE THIS ONE", Toast.LENGTH_SHORT).show();
                else
                     Toast.makeText(DetailsActivity.this, "FAREWELL MY FRIEND", Toast.LENGTH_SHORT).show();

            }
        });

        try {
            URL url= APIConnection.getOtherURL(getIntent().getStringExtra(INTENT_KEY_ID),"videos");
            new FitchData().execute(url);
            URL urlREVIEW= APIConnection.getOtherURL(getIntent().getStringExtra(INTENT_KEY_ID),"reviews");
            new FitchReview().execute(urlREVIEW);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        mRecyclerViewREVIEW =(RecyclerView) findViewById(R.id.rv_Reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        mRecyclerViewREVIEW.setLayoutManager(layoutManager2);
        mRecyclerViewREVIEW.setHasFixedSize(true);
    }

    public void filldata(){
        Picasso.with(this).load(KEY_POSTER_PATH + intent.getStringExtra(INTENT_KEY_POSTER))
                .into(imageView);
        mReleasDate.setText(intent.getStringExtra(INTENT_KEY_RELEASE_DATE));
        mRate.setText(intent.getStringExtra(INTENT_KEY_RATE));
        mOriginalName.setText(intent.getStringExtra(INTENT_KEY_ORIGINAL_TITLE));
        mOverview.setText(intent.getStringExtra(INTENT_KEY_OVERVIEW)+"\n");

        setTitle(intent.getStringExtra(INTENT_KEY_TITLE));
    }

    @Override
    public void onClick(int postion) {
        Intent videoClient = new Intent(Intent.ACTION_VIEW);
        try {
            String s = movieVideos.getVideoKey(postion);
            videoClient.setData(Uri.parse("http://m.youtube.com/watch?v="+s));
            startActivity(videoClient);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class FitchData extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String data = null;
            try {
                data = APIConnection.getOtherData(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }


        @Override
        protected void onPostExecute(String data) {

            if (data != null && !data.equals("")) {
                try {
                    movieVideos = new JSONMovies(data);
                    mTrilerAdapter=new TrailersAdapter(DetailsActivity.this,movieVideos);
                    mRecyclerView.setAdapter(mTrilerAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class FitchReview extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String data = null;
            try {
                data = APIConnection.getOtherData(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }


        @Override
        protected void onPostExecute(String data) {
            if (data != null && !data.equals("")) {
                try {
                    movieReviews = new JSONMovies(data);
                    mReviewAdapter=new ReviewAdapter(movieReviews);
                    mRecyclerViewREVIEW.setAdapter(mReviewAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
