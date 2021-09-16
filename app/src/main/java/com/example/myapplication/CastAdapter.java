package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {
    private ArrayList<Cast> castslist;
    private Context context;

    public CastAdapter(Context context,ArrayList<Cast> castslist){
        this.context=context;
        this.castslist=castslist;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView img;
        private TextView txt;
        public MyViewHolder(final View view){
            super(view);
            img=view.findViewById(R.id.cast_image);
            txt=view.findViewById(R.id.cast_name);
        }
    }

    @NonNull
    @Override
    public CastAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_cast,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CastAdapter.MyViewHolder holder, final int position) {
        Cast cc=castslist.get(position);
        Glide.with(holder.img)
                .load(cc.getSrc())
                .centerCrop()
                .into(holder.img);
        holder.txt.setText(cc.getName());
    }

    @Override
    public int getItemCount() {
        return castslist.size();
    }
}