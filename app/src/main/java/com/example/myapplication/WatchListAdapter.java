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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.MyViewHolder> {
    private ArrayList<Image> imagesList;
    private Context context;
    private View rootView;


    public WatchListAdapter(Context context,ArrayList<Image> imagesList,View rootView){
        this.context=context;
        this.imagesList=imagesList;
        this.rootView=rootView;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView txt;
        private TextView btn;
        private CardView watchlist_card;
        public MyViewHolder(final View view){
            super(view);
            img=view.findViewById(R.id.watchlist_image_background);
            txt=view.findViewById(R.id.watchlist_text);
            btn=view.findViewById(R.id.watchlist_button);
            watchlist_card=view.findViewById(R.id.watchlist_card);
        }
    }

    public ArrayList<Image> getImagesList(){
        return imagesList;
    }

    public void display(){
        for(int i=0;i<imagesList.size();i++){
            Log.d(String.valueOf(i),imagesList.get(i).getShowname());
        }
    }

    public void onItemMove(int fromPosition, int toPosition) throws JSONException {

        notifyItemMoved(fromPosition, toPosition);
        Collections.swap(imagesList, fromPosition, toPosition);
        //notifyItemChanged(fromPosition);
        //Log.d("change",String.valueOf(fromPosition));
        //notifyItemChanged(toPosition);
        //Log.d("change",String.valueOf(toPosition));
        //display();
        //Image correctItem=imagesList.get(toPosition);
        //imagesList.remove(toPosition);
        //imagesList.add(fromPosition, correctItem);
        //notifyItemMoved(toPosition, fromPosition);
        notifyDataSetChanged();
        Log.d("onItemMove",String.valueOf(fromPosition)+" to "+String.valueOf(toPosition));
        updateLocalWatchList(imagesList);

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

    private int updateLocalWatchList(ArrayList<Image> ali) throws JSONException {
        SharedPreferences.Editor prefsedit=context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE).edit();
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

    @NonNull
    @Override
    public WatchListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.watchlist_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final WatchListAdapter.MyViewHolder holder, final int position) {
        final Image thisImage=imagesList.get(position);
        String src=thisImage.getSrc();

        Glide.with(holder.img)
                .load(src)
                .centerCrop()
                .into(holder.img);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.txt.getContext(), "\""+thisImage.getShowname()+"\" was removed from Watchlist", Toast.LENGTH_SHORT).show();
                int remove_position=findIndex(thisImage);
                removeIndex(remove_position);
                imagesList.remove(remove_position);
                notifyItemRemoved(remove_position);
                notifyItemRangeChanged(remove_position, imagesList.size());
                display();
                if(imagesList.size()==0){
                    rootView.findViewById(R.id.watchlist_recyclerview).setVisibility(View.GONE);
                    rootView.findViewById(R.id.watchlist_nothing).setVisibility(View.VISIBLE);
                }
            }
        });
        if(thisImage.getMedia().equals("tv")){
            holder.txt.setText("TV");
        }
        else{
            holder.txt.setText("Movie");
        }
        holder.watchlist_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details_intent=new Intent(context,DetailsActivity.class);
                details_intent.putExtra("media",thisImage.getMedia());
                details_intent.putExtra("id",thisImage.getId());
                context.startActivity(details_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }
}
