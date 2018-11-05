package com.example.android.moviesexplorer.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.moviesexplorer.DataBase.FavoriteEntry;
import com.example.android.moviesexplorer.R;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {


    final private ItemClickListener mItemClickListener;

    private List<FavoriteEntry> mFavoriteEntries;
    private Context mContext;


    public FavoriteAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }


    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.favorites_layout, parent, false);

        return new FavoriteViewHolder(view);
    }


    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        FavoriteEntry favoriteEntry = mFavoriteEntries.get(position);
        String name = favoriteEntry.getName();
        holder.mFavName.setText(name);

    }

    @Override
    public int getItemCount() {
        if (mFavoriteEntries == null) {
            return 0;
        }
        return mFavoriteEntries.size();
    }

    public List<FavoriteEntry> getFavorites() {
        return mFavoriteEntries;
    }


    public void setFavorites(List<FavoriteEntry> favoriteEntries) {
        mFavoriteEntries = favoriteEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }
    class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mFavName;

        public FavoriteViewHolder(View itemView) {
            super(itemView);

            mFavName =(TextView) itemView.findViewById(R.id.tv_favName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mFavoriteEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}