package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private String url_head= "https://czhou240-csci571-hw8-nodejs.wm.r.appspot.com";
    private String pic_head="https://image.tmdb.org/t/p/original";
    private int start_cnt=0;


    private void display(View rootView){
        ProgressBar spinner = (ProgressBar)rootView.findViewById(R.id.spinner_part);
        TextView word=(TextView)rootView.findViewById(R.id.spinner_word);
        spinner.setVisibility(View.GONE);
        word.setVisibility(View.GONE);
        LinearLayout topLayout=(LinearLayout)rootView.findViewById(R.id.home_top_layout);
        topLayout.setVisibility(View.VISIBLE);
        LinearLayout footerLayout=(LinearLayout)rootView.findViewById(R.id.home_footer_layout);
        footerLayout.setVisibility(View.VISIBLE);
        final LinearLayout movieLayout=(LinearLayout)rootView.findViewById(R.id.home_movie_layout);
        final LinearLayout tvLayout=(LinearLayout)rootView.findViewById(R.id.home_tv_layout);
        movieLayout.setVisibility(View.VISIBLE);
        final Button movie_button = (Button) rootView.findViewById(R.id.movie_layout_button);
        final Button tv_button = (Button) rootView.findViewById(R.id.tv_layout_button);
        movie_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                movieLayout.setVisibility(View.VISIBLE);
                tvLayout.setVisibility(View.GONE);
                movie_button.setTextColor(Color.parseColor("#FFFFFF"));
                tv_button.setTextColor(Color.parseColor("#156EB4"));
            }
        });
        tv_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                movieLayout.setVisibility(View.GONE);
                tvLayout.setVisibility(View.VISIBLE);
                movie_button.setTextColor(Color.parseColor("#156EB4"));
                tv_button.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });
        final TextView TMDB_txt=(TextView) rootView.findViewById(R.id.tmdb_textview);
        TMDB_txt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/"));
                startActivity(browserIntent);
            }
            });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        start_cnt=0;
        getMovieNowPlayingData(rootView,rq);
        getTvTrendingData(rootView,rq);
        getMovieTopData(rootView,rq);
        getMoviePopData(rootView,rq);
        getTvTopData(rootView,rq);
        getTvPopData(rootView,rq);




        return rootView;
    }

    private void getMovieNowPlayingData(final View rootView, RequestQueue rq){
        String url= url_head+"/now_playing";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject o = new JSONObject(response);
                            JSONArray values=o.getJSONArray("results");
                            ArrayList<Image> sliderDataArrayList = new ArrayList<>();
                            SliderView sliderView = rootView.findViewById(R.id.movie_slider);
                            for(int index=0;index<6;index++){
                                JSONObject jo = values.getJSONObject(index);
                                Image myimg=new Image();
                                myimg.setMedia("movie");
                                myimg.setId(jo.getString("id"));
                                myimg.setShowname(jo.getString("title"));
                                myimg.setSrc(pic_head+jo.getString("poster_path"));
                                sliderDataArrayList.add(myimg);
                            }
                            SliderAdapter adapter = new SliderAdapter(getContext(), sliderDataArrayList);
                            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                            sliderView.setSliderAdapter(adapter);
                            sliderView.setAutoCycle(true);
                            sliderView.startAutoCycle();
                            start_cnt++;
                            if(start_cnt==6) display(rootView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", "Error occurred ", error);
                    }
                });
        rq.add(stringRequest);
    }


    private void getTvTrendingData(final View rootView, RequestQueue rq){
        String url= url_head+"/other/tv/trending";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject o = new JSONObject(response);
                            JSONArray values=o.getJSONArray("results");
                            ArrayList<Image> sliderDataArrayList = new ArrayList<>();
                            SliderView sliderView = rootView.findViewById(R.id.tv_slider);
                            for(int index=0;index<6;index++){

                                JSONObject jo = values.getJSONObject(index);
                                Image myimg=new Image();
                                myimg.setMedia("tv");
                                myimg.setId(jo.getString("id"));
                                myimg.setShowname(jo.getString("name"));
                                myimg.setSrc(pic_head+jo.getString("poster_path"));
                                sliderDataArrayList.add(myimg);
                            }
                            SliderAdapter adapter = new SliderAdapter(getContext(), sliderDataArrayList);
                            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                            sliderView.setSliderAdapter(adapter);
                            sliderView.setAutoCycle(true);
                            sliderView.startAutoCycle();
                            start_cnt++;
                            if(start_cnt==6) display(rootView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", "Error occurred ", error);
                    }
                });
        rq.add(stringRequest);
    }

    private void getMovieTopData(final View rootView, RequestQueue rq){
        String url= url_head+"/other/movie/top_rated";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject o = new JSONObject(response);
                            JSONArray values=o.getJSONArray("results");
                            ArrayList<Image> imageArrayList = new ArrayList<>();
                            RecyclerView recyclerView = rootView.findViewById(R.id.movie_recycler_view_top);
                            for(int index=0;index<10;index++){
                                JSONObject jo = values.getJSONObject(index);
                                imageArrayList.add(new Image(jo.getString("title"),"movie",jo.getString("id"),pic_head+jo.getString("poster_path")));
                            }
                            recyclerAdapter adapter=new recyclerAdapter(getContext(),imageArrayList);
                            RecyclerView.LayoutManager layoutManager
                                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                            start_cnt++;
                            if(start_cnt==6) display(rootView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", "Error occurred ", error);
                    }
                });
        rq.add(stringRequest);
    }

    private void getMoviePopData(final View rootView, RequestQueue rq){
        String url= url_head+"/other/movie/popular";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject o = new JSONObject(response);
                            JSONArray values=o.getJSONArray("results");
                            ArrayList<Image> imageArrayList = new ArrayList<>();
                            RecyclerView recyclerView = rootView.findViewById(R.id.movie_recycler_view_pop);
                            for(int index=0;index<10;index++){
                                JSONObject jo = values.getJSONObject(index);
                                imageArrayList.add(new Image(jo.getString("title"),"movie",jo.getString("id"),pic_head+jo.getString("poster_path")));
                            }
                            recyclerAdapter adapter=new recyclerAdapter(getContext(),imageArrayList);
                            RecyclerView.LayoutManager layoutManager
                                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                            start_cnt++;
                            if(start_cnt==6) display(rootView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", "Error occurred ", error);
                    }
                });
        rq.add(stringRequest);
    }

    private void getTvTopData(final View rootView, RequestQueue rq){
        String url= url_head+"/other/tv/top_rated";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject o = new JSONObject(response);
                            JSONArray values=o.getJSONArray("results");
                            ArrayList<Image> imageArrayList = new ArrayList<>();
                            RecyclerView recyclerView = rootView.findViewById(R.id.tv_recycler_view_top);
                            for(int index=0;index<10;index++){
                                JSONObject jo = values.getJSONObject(index);
                                imageArrayList.add(new Image(jo.getString("name"),"tv",jo.getString("id"),pic_head+jo.getString("poster_path")));
                            }
                            recyclerAdapter adapter=new recyclerAdapter(getContext(),imageArrayList);
                            RecyclerView.LayoutManager layoutManager
                                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                            start_cnt++;
                            if(start_cnt==6) display(rootView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", "Error occurred ", error);
                    }
                });
        rq.add(stringRequest);
    }

    private void getTvPopData(final View rootView, RequestQueue rq){
        String url= url_head+"/other/tv/popular";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject o = new JSONObject(response);
                            JSONArray values=o.getJSONArray("results");
                            ArrayList<Image> imageArrayList = new ArrayList<>();
                            RecyclerView recyclerView = rootView.findViewById(R.id.tv_recycler_view_pop);
                            for(int index=0;index<10;index++){
                                JSONObject jo = values.getJSONObject(index);
                                imageArrayList.add(new Image(jo.getString("name"),"tv",jo.getString("id"),pic_head+jo.getString("poster_path")));
                            }
                            recyclerAdapter adapter=new recyclerAdapter(getContext(),imageArrayList);
                            RecyclerView.LayoutManager layoutManager
                                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                            start_cnt++;
                            if(start_cnt==6) display(rootView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", "Error occurred ", error);
                    }
                });
        rq.add(stringRequest);
    }

    private void getAllData(View rootView,RequestQueue rq){
        getMovieNowPlayingData(rootView, rq);
        getTvTrendingData(rootView,rq);
    }





}



