package com.example.android.moviesexplorer.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.moviesexplorer.Utilities.JSONMovies;
import com.example.android.moviesexplorer.R;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
public class MovieAdapter  extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> implements View.OnClickListener  {
    JSONMovies jsonMovies;
    private final MovieAdapterClickHandler mHandler;
    private final String KEY_BACKDROP_PATH="http://image.tmdb.org/t/p/w185/";

    public interface MovieAdapterClickHandler {
        void onClick(int postion);
    }

    public MovieAdapter(MovieAdapterClickHandler clickHandler , JSONMovies jsonMovies1) {
        jsonMovies=jsonMovies1;
        mHandler = clickHandler;

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int itemId = R.layout.movies_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(itemId, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
         if (jsonMovies!= null)return  jsonMovies.getDataCount();
        else return 0;
    }

    @Override
    public void onClick(View view) {

    }

    public  class MovieViewHolder extends ViewHolder implements View.OnClickListener{
        public  ImageView imageView ;
        View view;
        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.iv_movie_poster);
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

        public void bind(int position){
            try {
                Picasso.with(getView().getContext()).load(KEY_BACKDROP_PATH + jsonMovies.getBackdrop_path(position))
                        .fit()
                        .noFade()
                        .into(imageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }





}
