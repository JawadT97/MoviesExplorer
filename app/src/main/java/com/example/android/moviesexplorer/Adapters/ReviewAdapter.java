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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MovieViewHolder> {

    JSONMovies jsonMovies;

    public ReviewAdapter( JSONMovies jsonMovies1) {
        jsonMovies=jsonMovies1;

    }

    @Override
    public ReviewAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int itemId = R.layout.review_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(itemId, parent, false);
        return new ReviewAdapter.MovieViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ReviewAdapter.MovieViewHolder holder, int position) {
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


    public  class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView mReviewAuthor , mReviewContent;
        View view;
        public MovieViewHolder(View itemView) {
            super(itemView);
            mReviewAuthor=(TextView) itemView.findViewById(R.id.tv_Author);
            mReviewContent=(TextView) itemView.findViewById(R.id.tv_content);
            view = itemView;
        }

        public View getView(){
            return view;
        }

        public void bind(int position) throws JSONException {
            String [] trailer = jsonMovies.getReviews(position);
            mReviewAuthor.setText(trailer[0]+" : ");
            mReviewContent.setText(trailer[1]);
        }
    }




}
