package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class WatchlistFragment extends Fragment {



    private JSONArray getLocalWatchList(){
        SharedPreferences prefs=getContext().getSharedPreferences("LocalStorage", Context.MODE_PRIVATE);
        String MyWatchListString = prefs.getString("MyWatchList", "{}");
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(MyWatchListString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonArray==null){
            return new JSONArray();
        }
        else return jsonArray;
    }

    private ArrayList<Image> GenerateImageList(JSONArray ja) throws JSONException {
        ArrayList<Image> watchListArrayList = new ArrayList<>();
        for(int jai=0;jai<ja.length();jai++){
            Image curimg=new Image();
            curimg.setMedia(ja.getJSONObject(jai).getString("media"));
            curimg.setId(ja.getJSONObject(jai).getString("id"));
            curimg.setShowname(ja.getJSONObject(jai).getString("showname"));
            curimg.setSrc(ja.getJSONObject(jai).getString("src"));
            watchListArrayList.add(curimg);
        }
        return watchListArrayList;
    }

    private int updateLocalWatchList(ArrayList<Image> ali) throws JSONException {
        SharedPreferences.Editor prefsedit=getContext().getSharedPreferences("LocalStorage", Context.MODE_PRIVATE).edit();
        JSONArray jsonArray=new JSONArray();
        for(int alii=0;alii<ali.size();alii++){
            JSONObject jojo=new JSONObject();
            jojo.put("media",ali.get(alii).getMedia());
            jojo.put("id",ali.get(alii).getId());
            jojo.put("showname",ali.get(alii).getShowname());
            jojo.put("src",ali.get(alii).getSrc());
            jsonArray.put(jojo);
        }
        prefsedit.putString("MyWatchList",jsonArray.toString());
        prefsedit.commit();
        return 0;
    }

    private ArrayList<Image> wl;
    private WatchListAdapter adapter;
    ItemTouchHelper itemTouchHelper;

    private View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_watchlist,container,false);
        JSONArray ja = getLocalWatchList();
        wl=new ArrayList<>();
        try {
            wl=GenerateImageList(ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView nothingView=rootView.findViewById(R.id.watchlist_nothing);
        RecyclerView watchlistView = rootView.findViewById(R.id.watchlist_recyclerview);
        if(wl.size()==0){
            nothingView.setVisibility(View.VISIBLE);
            watchlistView.setVisibility(View.GONE);
        }
        else{
            nothingView.setVisibility(View.GONE);
            watchlistView.setVisibility(View.VISIBLE);
            adapter = new WatchListAdapter(getContext(), wl,rootView);
            //RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
            RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),3);
            watchlistView.setLayoutManager(layoutManager);
            watchlistView.setItemAnimator(new DefaultItemAnimator());
            watchlistView.setAdapter(adapter);
            itemTouchHelper=new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(watchlistView);
        }


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        JSONArray ja = getLocalWatchList();
        wl=new ArrayList<>();
        try {
            wl=GenerateImageList(ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView nothingView=rootView.findViewById(R.id.watchlist_nothing);
        RecyclerView watchlistView = rootView.findViewById(R.id.watchlist_recyclerview);
        if(wl.size()==0){
            nothingView.setVisibility(View.VISIBLE);
            watchlistView.setVisibility(View.GONE);
        }
        else{
            nothingView.setVisibility(View.GONE);
            watchlistView.setVisibility(View.VISIBLE);
            adapter = new WatchListAdapter(getContext(), wl,rootView);
            //RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
            RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),3);
            watchlistView.setLayoutManager(layoutManager);
            watchlistView.setItemAnimator(new DefaultItemAnimator());
            watchlistView.setAdapter(adapter);
            itemTouchHelper.attachToRecyclerView(null);
            itemTouchHelper=new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(watchlistView);
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Log.d("onMove",String.valueOf(fromPosition)+" => "+String.valueOf(toPosition));

            Collections.swap(adapter.getImagesList(),fromPosition,toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);
            recyclerView.getAdapter().notifyDataSetChanged();
            adapter.display();
            try {
                updateLocalWatchList(adapter.getImagesList());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}
