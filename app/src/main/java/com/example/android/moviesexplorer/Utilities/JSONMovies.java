package com.example.android.moviesexplorer.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMovies {
    private int dataCount=0;
    String data;
    private JSONArray jsonArray;
    private final static String KEY_RESULTS="results";
    private final static String KEY_POSTER="poster_path";
    private final static String KEY_BACKDROP="backdrop_path";
    private final static String KEY_TITLE="title";
    private final static String KEY_ORIGINAL="original_title";
    private final static String KEY_OVERVIEW="overview";
    private final static String KEY_RATE="vote_average";
    private final static String KEY_RELEASE_DATE="release_date";
    private final static String KEY_ID="id";
    private final static String KEY_TRAILER_KEY="key";
    private final static String KEY_TRAILER_NAME="name";
    private final static String KEY_REVIEWS_AUTHOR="author";
    private final static String KEY_REVIEWS_CONTENT="content";

    public JSONMovies(String JSON_String) throws JSONException {
        JSONObject obj= new JSONObject(JSON_String);
        jsonArray = obj.getJSONArray(KEY_RESULTS);
        dataCount = jsonArray.length();
    }

    public int getDataCount(){
         return dataCount;
    }

    public String getPoster_path(int position) throws JSONException {
        JSONObject object = new JSONObject(jsonArray.getString(position));
        return object.getString(KEY_POSTER);
    }

    public String getBackdrop_path(int position) throws JSONException {
        JSONObject object = new JSONObject(jsonArray.getString(position));
        return object.getString(KEY_BACKDROP);
    }

    public String getTitle(int position) throws JSONException {
        JSONObject object = new JSONObject(jsonArray.getString(position));
        return object.getString(KEY_TITLE);
    }

    public String getID(int position) throws JSONException {
        JSONObject object = new JSONObject(jsonArray.getString(position));
        return object.getString(KEY_ID);
    }

    public String getOriginal_title(int position) throws JSONException {
        JSONObject object = new JSONObject(jsonArray.getString(position));
        return object.getString(KEY_ORIGINAL);
    }

    public String getOverview(int position) throws JSONException {
        JSONObject object = new JSONObject(jsonArray.getString(position));
        return object.getString(KEY_OVERVIEW);
    }

    public String getVote_average(int position) throws JSONException {
        JSONObject object = new JSONObject(jsonArray.getString(position));
        return object.getInt(KEY_RATE)+"";
    }

    public String getRelease_date(int position) throws JSONException {
        JSONObject object = new JSONObject(jsonArray.getString(position));
        return object.getString(KEY_RELEASE_DATE);
    }

    public String getVideos (int position) throws JSONException {
        JSONObject object;
        String videos ;
            object = new JSONObject(jsonArray.getString(position));
            videos= object.getString(KEY_TRAILER_NAME);
        return videos;
    }
    public String getVideoKey (int position) throws JSONException {
        JSONObject object;
        String key ;
        object = new JSONObject(jsonArray.getString(position));
        key= object.getString(KEY_TRAILER_KEY);
        return key;
    }

    public String [] getReviews(int position) throws JSONException {
        JSONObject object;
        String[] reviews = new String[2];
        object = new JSONObject(jsonArray.getString(position));
        reviews[0]= object.getString(KEY_REVIEWS_AUTHOR);
        reviews[1] = object.getString(KEY_REVIEWS_CONTENT);
        return reviews;
    }
}
