package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.MyViewHolder> {
    private ArrayList<Image> imageslist;
    private Context context;

    public RecommendAdapter(Context context,ArrayList<Image> imageslist){
        this.context=context;
        this.imageslist=imageslist;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        public MyViewHolder(final View view){
            super(view);
            img=view.findViewById(R.id.recommend_image);
        }
    }

    @NonNull
    @Override
    public RecommendAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecommendAdapter.MyViewHolder holder, final int position) {
        final Image ii=imageslist.get(position);
        Glide.with(holder.img)
                .load(ii.getSrc())
                .centerCrop()
                .into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details_intent=new Intent(context,DetailsActivity.class);
                details_intent.putExtra("media",ii.getMedia());
                details_intent.putExtra("id",ii.getId());
                context.startActivity(details_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageslist.size();
    }
}