package com.example.myapplication;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {
    private String url_head= "https://czhou240-csci571-hw8-nodejs.wm.r.appspot.com";
    private String pic_head="https://image.tmdb.org/t/p/original";


    private void getSearchResults(final View rootView, RequestQueue rq,String searchString) {
        searchString=searchString.replace(" ","%20");
        String url= url_head+"/search/"+searchString;
        Log.d("SEARCH", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject o = new JSONObject(response);
                            JSONArray values=o.getJSONArray("results");
                            ArrayList<SeachResult> searchResultsArrayList = new ArrayList<>();
                            RecyclerView searchView = rootView.findViewById(R.id.search_results);
                            int cnt=0;
                            if(values.length()==0){
                                TextView tv=rootView.findViewById(R.id.no_result);
                                tv.setVisibility(View.VISIBLE);
                                RecyclerView rv=rootView.findViewById(R.id.search_results);
                                rv.setVisibility(View.GONE);
                            }
                            else {
                                for (int index = 0; index < values.length(); index++) {
                                    if (cnt == 20) break;
                                    JSONObject jo = values.getJSONObject(index);
                                    if(!jo.has("backdrop_path")) continue;
                                    if (jo.getString("backdrop_path").equals("null")) continue;
                                    if (jo.getString("media_type").equals("movie")) {
                                        cnt++;
                                        double rate = jo.getDouble("vote_average") / 2;
                                        double result = new BigDecimal(rate).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                                        String rate_str = String.valueOf(result);
                                        searchResultsArrayList.add(new SeachResult(jo.getString("title"), "MOVIE (" + jo.getString("release_date").substring(0, 4) + ")", jo.getString("id"), pic_head + jo.getString("backdrop_path"), rate_str));
                                    } else if (jo.getString("media_type").equals("tv")) {
                                        cnt++;
                                        double rate = jo.getDouble("vote_average") / 2;
                                        double result = new BigDecimal(rate).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                                        String rate_str = String.valueOf(result);
                                        searchResultsArrayList.add(new SeachResult(jo.getString("name"), "TV (" + jo.getString("first_air_date").substring(0, 4) + ")", jo.getString("id"), pic_head + jo.getString("backdrop_path"), rate_str));
                                    }
                                }
                                if(cnt!=0){
                                    TextView tv=rootView.findViewById(R.id.no_result);
                                    tv.setVisibility(View.GONE);
                                    RecyclerView rv=rootView.findViewById(R.id.search_results);
                                    rv.setVisibility(View.VISIBLE);
                                }
                                else{
                                    TextView tv=rootView.findViewById(R.id.no_result);
                                    tv.setVisibility(View.VISIBLE);
                                    RecyclerView rv=rootView.findViewById(R.id.search_results);
                                    rv.setVisibility(View.GONE);
                                }
                                SearchAdapter adapter = new SearchAdapter(getContext(), searchResultsArrayList);
                                RecyclerView.LayoutManager layoutManager
                                        = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                searchView.setLayoutManager(layoutManager);
                                searchView.setItemAnimator(new DefaultItemAnimator());
                                searchView.setAdapter(adapter);
                            }

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.fragment_search,container,false);
        SearchView search_view=rootView.findViewById(R.id.search_view);
        search_view.requestFocus();
        final RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    TextView textView=rootView.findViewById(R.id.no_result);
                    RecyclerView searchView = rootView.findViewById(R.id.search_results);
                    textView.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                }
                else {
                    getSearchResults(rootView, rq, newText);
                }
                return true;
            }
        });

        /*
        int searchCloseButtonId = search_view.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) rootView.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        */
        return rootView;
    }
}
