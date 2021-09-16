package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private ArrayList<SeachResult> searchresultsList;
    private Context context;

    public SearchAdapter(Context context,ArrayList<SeachResult> searchresultsList){
        this.context=context;
        this.searchresultsList=searchresultsList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView lttext;
        private TextView rttext;
        private TextView lbtext;
        private RelativeLayout search_layout;
        public MyViewHolder(final View view){
            super(view);
            img=view.findViewById(R.id.seach_item_image);
            lttext=view.findViewById(R.id.search_item_left_top_text);
            rttext=view.findViewById(R.id.search_item_right_top_text);
            lbtext=view.findViewById(R.id.search_item_left_bottom_text);
            search_layout=view.findViewById(R.id.search_layout);
        }
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.MyViewHolder holder, final int position) {
        final SeachResult sr=searchresultsList.get(position);
        Glide.with(holder.img)
                .load(sr.getBackdrop())
                .centerCrop()
                .into(holder.img);
        holder.lttext.setText(sr.getMedia());
        holder.rttext.setText(sr.getRate());
        holder.lbtext.setText(sr.getShowname());
        holder.search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details_intent=new Intent(context,DetailsActivity.class);
                if(sr.getMedia().charAt(0)=='T') details_intent.putExtra("media","tv");
                else details_intent.putExtra("media","movie");
                details_intent.putExtra("id",sr.getId());
                context.startActivity(details_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchresultsList.size();
    }
}
