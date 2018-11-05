package com.example.android.moviesexplorer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesexplorer.DataBase.AppDatabase;
import com.example.android.moviesexplorer.DataBase.FavoriteEntry;
import com.example.android.moviesexplorer.Utilities.AppExecutors;

public class OfflineDetails extends AppCompatActivity {
    private final String INTENT_KEY_ID="ID";

   AppDatabase mDataBase;
    TextView mReleasDate,mRate,mOverViwe ;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_details);
        Intent intent = getIntent();

        final int id = intent.getIntExtra(INTENT_KEY_ID,-1);
        mReleasDate = (TextView) findViewById(R.id.tv_of_date);
        mRate= (TextView) findViewById(R.id.tv_of_rate);
        mOverViwe = (TextView) findViewById(R.id.tv_of_desc);
        mButton = (Button) findViewById(R.id.bt_remove_fav);

        mDataBase = AppDatabase.getInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final FavoriteEntry entry = mDataBase.favoriteDao().loadMovieById(id);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTitle(entry.getName());
                        mReleasDate.setText("Release Date : " + entry.getReleaseDate().toString());
                        mRate.setText("Rate : " + entry.getRate().toString());
                        mOverViwe.setText(entry.getDescription().toString());
                    }
                });
            }
        });

        final AppDatabase mDateBase = AppDatabase.getInstance(getApplicationContext());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDateBase.favoriteDao().deleteById(id);
                    }
                });
                Toast.makeText(OfflineDetails.this, "FAREWELL MY FRIEND", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
