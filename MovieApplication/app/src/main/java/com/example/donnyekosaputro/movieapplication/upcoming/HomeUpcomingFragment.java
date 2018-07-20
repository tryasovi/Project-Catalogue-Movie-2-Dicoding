package com.example.donnyekosaputro.movieapplication.upcoming;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.donnyekosaputro.movieapplication.BuildConfig;
import com.example.donnyekosaputro.movieapplication.adapternowplayupcoming.MovieItems;
import com.example.donnyekosaputro.movieapplication.R;
import com.example.donnyekosaputro.movieapplication.adapternowplayupcoming.NowPlayingAndUpcomingAdapter;



public class HomeUpcomingFragment extends Fragment {
    private RecyclerView rvCategory;
    private RecyclerView.Adapter adapter;
    private View view;
    private List<MovieItems> movieLists;

    private static final String APIURL= BuildConfig.MOVIEURL+"/upcoming?api_key="+ BuildConfig.MOVIEAPIKEY+"&language=en-US";
    public HomeUpcomingFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home_now_playing,container,false);
        rvCategory=(RecyclerView)view.findViewById(R.id.rv_category);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        movieLists=new ArrayList<>();
        loadUrlData();
        return view;
    }

    private void loadUrlData() {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading!!");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                APIURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray array = jsonObject.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++){

                        MovieItems movies = new MovieItems();

                        JSONObject data = array.getJSONObject(i);
                        movies.setMovie_title(data.getString("title"));
                        movies.setMovie_description(data.getString("overview"));
                        movies.setMovie_date(data.getString("release_date"));
                        movies.setMovie_image(data.getString("poster_path"));
                        movies.setMovie_rate_count(data.getString("vote_count"));
                        movies.setMovie_rate(data.getString("vote_average"));
                        movieLists.add(movies);

                    }

                    adapter = new NowPlayingAndUpcomingAdapter(movieLists, getActivity());
                    rvCategory.setAdapter(adapter);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "Error" + error.toString(), Toast.LENGTH_SHORT).show();
                loadUrlData();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
