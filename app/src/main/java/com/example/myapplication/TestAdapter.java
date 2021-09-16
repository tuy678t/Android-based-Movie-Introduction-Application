package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TestAdapter extends RecyclerView.Adapter<com.example.myapplication.TestAdapter.MyViewHolder> {
        private ArrayList<String> als;
        private Context context;

        public TestAdapter(Context context,ArrayList<String> als){
            this.context=context;
            this.als=als;
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder{
            private TextView tv;
            public MyViewHolder(final View view){
                super(view);
                tv=view.findViewById(R.id.watchlist_text);
            }
        }

        @NonNull
        @Override
        public com.example.myapplication.TestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.watchlist_item,parent,false);
            return new com.example.myapplication.TestAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final com.example.myapplication.TestAdapter.MyViewHolder holder, final int position) {
            final String str=als.get(position);
            holder.tv.setText(str);
        }

        @Override
        public int getItemCount() {
            return als.size();
        }
    }
