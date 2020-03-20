package com.aaa.mymovies.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;

import com.aaa.mymovies.R;
import com.aaa.mymovies.data.MovieAdapter;
import com.aaa.mymovies.model.Movie;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieArrayList;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_list);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        movieArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        
        getMovies();
    }

    private void getMovies() {
        String url = "http://www.omdbapi.com/?apikey=1781650d&s=superman";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Search");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String title = jsonObject.getString("Title");
                                String year = jsonObject.getString("Year");
                                String posterUrl = jsonObject.getString("Poster");

                                Movie movie = new Movie();
                                movie.setPosterUrl(posterUrl);
                                movie.setTitle(title);
                                movie.setYear(year);

                                movieArrayList.add(movie);
                            }
                            movieAdapter = new MovieAdapter(MainActivity.this, movieArrayList);
                            recyclerView.setAdapter(movieAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }
}
