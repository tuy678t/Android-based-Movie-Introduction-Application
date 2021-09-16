package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private ArrayList<Review> reviewslist;
    private Context context;

    public ReviewAdapter(Context context,ArrayList<Review> reviewslist){
        this.context=context;
        this.reviewslist=reviewslist;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView info;
        private TextView rate;
        private TextView cont;
        private CardView card;
        public MyViewHolder(final View view){
            super(view);
            info=view.findViewById(R.id.review_info);
            rate=view.findViewById(R.id.review_rate);
            cont=view.findViewById(R.id.review_cont);
            card=view.findViewById(R.id.review_card);
        }
    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewAdapter.MyViewHolder holder, final int position) {
        final Review rr=reviewslist.get(position);
        holder.info.setText(rr.getInfo());
        holder.rate.setText(rr.getRating());
        holder.cont.setText(rr.getContent());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent review_intent=new Intent(context,ReviewActivity.class);
                review_intent.putExtra("info",rr.getInfo());
                review_intent.putExtra("rate",rr.getRating());
                review_intent.putExtra("cont",rr.getContent());
                context.startActivity(review_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewslist.size();
    }
}