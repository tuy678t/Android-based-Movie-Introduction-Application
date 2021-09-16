package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    private int start_cnt=0;
    private String media="";
    private String id="";
    private YouTubePlayerView youTubePlayerView;

    private int findIndex(String tmedia,String tid){
        SharedPreferences prefs=getApplicationContext().getSharedPreferences("LocalStorage", Context.MODE_PRIVATE);
        String MyWatchListString = prefs.getString("MyWatchList", "{}");
        Log.d("findIndex",MyWatchListString);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(MyWatchListString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonArray==null){
            Log.d("recyc","jsonArray is empty");
            return -1;

        }
        else{
            Log.d("recyc","jsonArray.length="+jsonArray.length());
            for(int jai=0;jai<jsonArray.length();jai++) {
                String cmedia="";
                String cid="";
                try {
                    cmedia=jsonArray.getJSONObject(jai).getString("media");
                    cid=jsonArray.getJSONObject(jai).getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(cmedia.equals(tmedia)&&cid.equals(tid)){
                    return jai;
                }
            }
        }
        return -1;
    }

    private int removeIndex(int index){
        SharedPreferences prefs=getApplicationContext().getSharedPreferences("LocalStorage", Context.MODE_PRIVATE);
        String MyWatchListString = prefs.getString("MyWatchList", "{}");
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(MyWatchListString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.remove(index);
        SharedPreferences.Editor prefsedit= prefs.edit();
        prefsedit.putString("MyWatchList",jsonArray.toString());
        prefsedit.commit();
        return 0;
    }

    private int addIndex(Image curImage) throws JSONException {
        SharedPreferences prefs=getApplicationContext().getSharedPreferences("LocalStorage", Context.MODE_PRIVATE);
        String MyWatchListString = prefs.getString("MyWatchList", "{}");
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(MyWatchListString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonArray==null) jsonArray=new JSONArray();
        JSONObject jojo=new JSONObject();
        jojo.put("media",curImage.getMedia());
        jojo.put("id",curImage.getId());
        jojo.put("showname",curImage.getShowname());
        jojo.put("src",curImage.getSrc());
        jsonArray.put(jojo);
        SharedPreferences.Editor prefsedit= prefs.edit();
        prefsedit.putString("MyWatchList",jsonArray.toString());
        prefsedit.commit();
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent details_intent = getIntent();
        media = details_intent.getStringExtra("media"); // will return "FirstKeyValue"
        id= details_intent.getStringExtra("id"); // will return "SecondKeyValue"
        Log.d("Details",media+" "+id);

        RequestQueue rq = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        start_cnt=0;

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);


        ImageView dwb=findViewById(R.id.details_watchlist_btn);
        if(findIndex(media,id)!=-1){
            Log.d("findIndex","yes");
            dwb.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
        }
        ImageView dfb=findViewById(R.id.details_facebook_btn);
        dfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=https%3A//www.themoviedb.org/" + media + "/" + id));
                DetailsActivity.this.startActivity(browserIntent);
            }
        });
        ImageView dtb=findViewById(R.id.details_twitter_btn);
        dtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Check%20this%20out!%0ahttps%3A//www.themoviedb.org/" + media + "/" + id));
                DetailsActivity.this.startActivity(browserIntent);
            }
        });

        start_cnt=0;
        getVideo(rq);
        getDetails(rq);
        getCast(rq);
        getReview(rq);
        getRecommend(rq);

    }


    private void display(){
        RelativeLayout rl=findViewById(R.id.details_load);
        LinearLayout ll=findViewById(R.id.details_loaded);
        rl.setVisibility(View.GONE);
        ll.setVisibility(View.VISIBLE);
    }

    private String url_head= "https://czhou240-csci571-hw8-nodejs.wm.r.appspot.com";
    private String pic_head="https://image.tmdb.org/t/p/original";


    private void getDetails(RequestQueue rq){
        String url= url_head+"/details/"+media+"/"+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject o = new JSONObject(response);
                            String img_url=pic_head+o.getString("backdrop_path");
                            Glide.with(getApplicationContext())
                                    .load(img_url)
                                    .centerCrop()
                                    .into((ImageView) findViewById(R.id.details_default_picture));
                            TextView txtviw=findViewById(R.id.details_showname);
                            final Image img=new Image();
                            img.setId(id);
                            img.setMedia(media);
                            if(media.equals("movie")){
                                txtviw.setText(o.getString("title"));
                                img.setShowname(o.getString("title"));
                            }
                            else{
                                txtviw.setText(o.getString("name"));
                                img.setShowname(o.getString("name"));
                            }

                            img.setSrc(pic_head+o.getString("poster_path"));


                            final ImageView dwb=findViewById(R.id.details_watchlist_btn);
                            dwb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int index=findIndex(media,id);
                                    if(index==-1){
                                        try {
                                            addIndex(img);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        dwb.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
                                        Toast.makeText(DetailsActivity.this, img.getShowname()+" was added to Watchlist", Toast.LENGTH_LONG).show();

                                    }
                                    else{
                                        removeIndex(index);
                                        dwb.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
                                        Toast.makeText(DetailsActivity.this, img.getShowname()+" was removed from Watchlist", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                            ReadMoreTextView readMoreTextView=findViewById(R.id.details_overview_text);
                            readMoreTextView.setText(o.getString("overview"));

                            String genres_str="";
                            JSONArray ja=o.getJSONArray("genres");
                            for(int index=0;index<ja.length();index++){
                                if(index!=0) genres_str+=", ";
                                genres_str+=ja.getJSONObject(index).getString("name");
                            }
                            TextView genres_text=findViewById(R.id.details_genres_text);
                            genres_text.setText(genres_str);

                            TextView year_text=findViewById(R.id.details_year_text);
                            if(media.equals("movie")){
                                year_text.setText(o.getString("release_date").substring(0,4));
                            }
                            else{
                                year_text.setText(o.getString("first_air_date").substring(0,4));
                            }

                            start_cnt++;
                            if(start_cnt==5) display();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Error occurred ", error);
                    }
                });
        rq.add(stringRequest);
    }

    private void getVideo(RequestQueue rq){
        String url= url_head+"/videos/"+media+"/"+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject o = new JSONObject(response);
                            JSONArray values=o.getJSONArray("results");
                            for(int index=0;index<values.length();index++){
                                final JSONObject jo = values.getJSONObject(index);
                                if(jo.getString("type").equals("Trailer")){
                                    findViewById(R.id.details_default_picture).setVisibility(View.GONE);
                                    findViewById(R.id.youtube_player_view).setVisibility(View.VISIBLE);
                                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                        @Override
                                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                            String videoId = null;
                                            try {
                                                videoId = jo.getString("key");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            youTubePlayer.cueVideo(videoId, 0);
                                        }
                                    });
                                }
                            }
                            start_cnt++;
                            if(start_cnt==5) display();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Error occurred ", error);
                    }
                });
        rq.add(stringRequest);
    }

    private void getCast(RequestQueue rq){
        String url= url_head+"/casts/"+media+"/"+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject o = new JSONObject(response);
                            JSONArray values=o.getJSONArray("cast");
                            if(values.length()==0){
                                findViewById(R.id.details_cast_all).setVisibility(View.GONE);
                            }
                            else {
                                Log.d("getCast",String.valueOf(values.length()));
                                ArrayList<Cast> castslist = new ArrayList<>();
                                findViewById(R.id.details_cast_all).setVisibility(View.VISIBLE);
                                for (int index = 0; index < values.length(); index++) {
                                    if(castslist.size()==6) break;
                                    final JSONObject jo = values.getJSONObject(index);
                                    if(!jo.has("profile_path")||jo.getString("profile_path")=="null") continue;
                                    Cast cc=new Cast(jo.getString("name"),pic_head+jo.getString("profile_path"));
                                    castslist.add(cc);
                                }
                                RecyclerView castView = findViewById(R.id.details_cast_recyclerview);
                                CastAdapter adapter = new CastAdapter(DetailsActivity.this, castslist);
                                RecyclerView.LayoutManager layoutManager=new GridLayoutManager(DetailsActivity.this,3);
                                castView.setNestedScrollingEnabled(false);
                                castView.setLayoutManager(layoutManager);
                                castView.setItemAnimator(new DefaultItemAnimator());
                                castView.setAdapter(adapter);
                            }
                            start_cnt++;
                            if(start_cnt==5) display();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Error occurred ", error);
                    }
                });
        rq.add(stringRequest);
    }

    private void getReview(RequestQueue rq){
        String url= url_head+"/reviews/"+media+"/"+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject o = new JSONObject(response);
                            JSONArray values=o.getJSONArray("results");
                            if(values.length()==0){
                                findViewById(R.id.details_review_all).setVisibility(View.GONE);
                            }
                            else {
                                ArrayList<Review> reviewArrayList = new ArrayList<>();
                                findViewById(R.id.details_review_all).setVisibility(View.VISIBLE);
                                for (int index = 0; index < Math.min(3, values.length()); index++) {
                                    final JSONObject jo = values.getJSONObject(index);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    Date getDate = sdf.parse(jo.getString("created_at"));
                                    DateFormat df = new SimpleDateFormat("E, MMM dd yyyy");
                                    String str=df.format(getDate);
                                    String info="by "+jo.getString("author")+" on "+str;
                                    Log.d("getreview",jo.toString());
                                    int result=0;
                                    if(jo.getJSONObject("author_details").getString("rating")!="null"){
                                        result = (int) (jo.getJSONObject("author_details").getDouble("rating") / 2);
                                    }
                                    String rate_str = String.valueOf(result)+"/5";
                                    String content=jo.getString("content");
                                    Review rr=new Review(info,rate_str,content);
                                    reviewArrayList.add(rr);
                                }
                                RecyclerView reviewView = findViewById(R.id.details_review_recyclerview);
                                ReviewAdapter adapter = new ReviewAdapter(DetailsActivity.this, reviewArrayList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                                reviewView.setLayoutManager(layoutManager);
                                reviewView.setItemAnimator(new DefaultItemAnimator());
                                reviewView.setAdapter(adapter);
                            }
                            start_cnt++;
                            if(start_cnt==5) display();

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Error occurred ", error);
                    }
                });
        rq.add(stringRequest);
    }

    private void getRecommend(RequestQueue rq){
        String url= url_head+"/recommended/"+media+"/"+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject o = new JSONObject(response);
                            JSONArray values=o.getJSONArray("results");
                                ArrayList<Image> imageArrayList = new ArrayList<>();
                                for (int index = 0; index < Math.min(10,values.length()); index++) {
                                    final JSONObject jo = values.getJSONObject(index);
                                    if(!jo.has("poster_path")||jo.getString("poster_path")=="null") continue;
                                    Image ii=new Image();
                                    ii.setSrc(pic_head+jo.getString("poster_path"));
                                    ii.setMedia(media);
                                    ii.setId(jo.getString("id"));
                                    if(media.equals("tv")){
                                        ii.setShowname(jo.getString("name"));
                                    }
                                    else{
                                        ii.setShowname(jo.getString("title"));
                                    }
                                    imageArrayList.add(ii);
                                }
                                RecyclerView reviewView = findViewById(R.id.details_recommended_recyclerview);
                                RecommendAdapter adapter = new RecommendAdapter(DetailsActivity.this, imageArrayList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                reviewView.setLayoutManager(layoutManager);
                                reviewView.setItemAnimator(new DefaultItemAnimator());
                                reviewView.setAdapter(adapter);
                                if(imageArrayList.isEmpty()){
                                    findViewById(R.id.details_recommended_title).setVisibility(View.GONE);
                                }
                            start_cnt++;
                            if(start_cnt==5) display();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Error occurred ", error);
                    }
                });
        rq.add(stringRequest);
    }

}