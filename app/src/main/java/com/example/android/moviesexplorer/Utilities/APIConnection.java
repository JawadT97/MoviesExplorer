package com.example.android.moviesexplorer.Utilities;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class APIConnection {
    static String BASE_URL="";
    static final String API_KEY = "2ac3c23d3c7f3b86952f9c800cb4e1b0";
    public static URL getURL( String SortType) throws MalformedURLException {
        BASE_URL = "http://api.themoviedb.org/3/movie/";
        BASE_URL+=SortType;
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter("api_key",API_KEY)
                .build();
        URL url = null;
            url = new URL(builtUri.toString());
            return url;
    }
    public static String getData (URL url) throws IOException {
        StringBuffer response = new StringBuffer();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            BufferedReader bufferedInputStream=new BufferedReader(new InputStreamReader( con.getInputStream()));
            String data;
            while ((data = bufferedInputStream.readLine()) != null) {
                response.append(data);
            }
            bufferedInputStream.close();
        } finally {
            con.disconnect();
        }
        return response.toString();
    }
    public static URL getOtherURL(String id , String SortType) throws MalformedURLException {
        BASE_URL = "https://api.themoviedb.org/3/movie/";
        BASE_URL+=""+id+"/";
        BASE_URL+=SortType;
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter("api_key",API_KEY)
                .build();
        URL url = null;
        url = new URL(builtUri.toString());
        return url;
    }
    public static String getOtherData (URL url) throws IOException {
        StringBuffer response = new StringBuffer();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            BufferedReader bufferedInputStream=new BufferedReader(new InputStreamReader( con.getInputStream()));
            String data;
            while ((data = bufferedInputStream.readLine()) != null) {
                response.append(data);
            }
            bufferedInputStream.close();
        } finally {
            con.disconnect();
        }
        return response.toString();
    }

}



