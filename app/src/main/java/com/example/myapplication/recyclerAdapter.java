package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private ArrayList<Image> imagesList;
    private Context context;

    public recyclerAdapter(Context context,ArrayList<Image> imagesList){
        this.context=context;
        this.imagesList=imagesList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView txt;
        private CardView card;
        public MyViewHolder(final View view){
            super(view);
            img=view.findViewById(R.id.image_view_background);
            txt=view.findViewById(R.id.image_view_text);
            card=view.findViewById(R.id.list_item_card);

        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(itemView);
    }

    private int findIndex(Image curImage){
        SharedPreferences prefs=context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE);
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
                String media="";
                String id="";
                try {
                    media=jsonArray.getJSONObject(jai).getString("media");
                    id=jsonArray.getJSONObject(jai).getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(media.equals(curImage.getMedia())&&id.equals(curImage.getId())){
                    return jai;
                }
            }
        }
        return -1;
    }

    private int removeIndex(int index){
        SharedPreferences prefs=context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE);
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
        SharedPreferences prefs=context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE);
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
    public void onBindViewHolder(@NonNull final recyclerAdapter.MyViewHolder holder, final int position) {
        final Image thisImage=imagesList.get(position);
        String src=thisImage.getSrc();

        Glide.with(holder.img)
                .load(src)
                .centerCrop()
                .into(holder.img);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details_intent=new Intent(context,DetailsActivity.class);
                details_intent.putExtra("media",thisImage.getMedia());
                details_intent.putExtra("id",thisImage.getId());
                context.startActivity(details_intent);
            }
        });

        holder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(holder.txt.getContext(), holder.txt);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                if(findIndex(thisImage)==-1){
                    popupMenu.getMenu().getItem(3).setTitle("Add to Watchlist");
                }
                else{
                    popupMenu.getMenu().getItem(3).setTitle("Remove from Watchlist");
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        Intent browserIntent;
                        switch (menuItem.getItemId()){
                            case R.id.menu_tmdb:
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/"+imagesList.get(position).getMedia()+"/"+imagesList.get(position).getId()));
                                context.startActivity(browserIntent);
                                break;
                            case R.id.menu_facebook:
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=https%3A//www.themoviedb.org/"+imagesList.get(position).getMedia()+"/"+imagesList.get(position).getId()));
                                context.startActivity(browserIntent);
                                break;
                            case R.id.menu_twitter:
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Check%20this%20out!%0ahttps%3A//www.themoviedb.org/"+imagesList.get(position).getMedia()+"/"+imagesList.get(position).getId()));
                                context.startActivity(browserIntent);
                                break;
                            case R.id.menu_watchlist:
                                int find_index=findIndex(thisImage);
                                if(find_index==-1){
                                    try {
                                        addIndex(thisImage);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    popupMenu.getMenu().getItem(3).setTitle("Remove from Watchlist");
                                    Toast.makeText(holder.txt.getContext(), imagesList.get(position).getShowname()+" was added to Watchlist", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    removeIndex(find_index);
                                    popupMenu.getMenu().getItem(3).setTitle("Add to Watchlist");
                                    Toast.makeText(holder.txt.getContext(), imagesList.get(position).getShowname()+" was removed from Watchlist", Toast.LENGTH_SHORT).show();

                                }
                                break;
                        }



                        return true;
                    }
                });
                popupMenu.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }
}
