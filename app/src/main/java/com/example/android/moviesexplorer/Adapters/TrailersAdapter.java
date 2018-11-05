package com.example.android.moviesexplorer.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moviesexplorer.Utilities.JSONMovies;
import com.example.android.moviesexplorer.R;

import org.json.JSONException;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MovieViewHolder> implements View.OnClickListener {

    JSONMovies jsonMovies;
    private final TrailersAdapter.TrailersAdapterClickHandler mHandler;
    public interface TrailersAdapterClickHandler {
        void onClick(int postion);
    }

    public TrailersAdapter(TrailersAdapter.TrailersAdapterClickHandler clickHandler , JSONMovies jsonMovies1) {
        jsonMovies=jsonMovies1;
        mHandler = clickHandler;

    }

    @Override
    public TrailersAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int itemId = R.layout.trailers_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(itemId, parent, false);
        return new TrailersAdapter.MovieViewHolder(view);

    }

    @Override
    public void onBindViewHolder(TrailersAdapter.MovieViewHolder holder, int position) {
        try {
            holder.bind(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (jsonMovies!= null)return  jsonMovies.getDataCount();
        else return 0;
    }

    @Override
    public void onClick(View view) {

    }

    public  class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTrailerName ;
        View view;
        public MovieViewHolder(View itemView) {
            super(itemView);
            mTrailerName=(TextView) itemView.findViewById(R.id.tv_trailerName);
            view = itemView;
            itemView.setOnClickListener(this);
        }

        public View getView(){
            return view;
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mHandler.onClick(adapterPosition);
        }

        public void bind(int position) throws JSONException {
            String  trailer = jsonMovies.getVideos(position);
            mTrailerName.setText(trailer);
        }
    }




}
