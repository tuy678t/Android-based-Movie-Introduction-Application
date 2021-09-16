package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    private final List<Image> mSliderItems;
    private Context context;

    // Constructor
    public SliderAdapter(Context context, ArrayList<Image> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
        this.context=context;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final Image sliderItem = mSliderItems.get(position);

        // Glide is use to load image
        // from url in your imageview.

        Glide.with(viewHolder.itemView)
                .load(sliderItem.getSrc())
                .transform(new BlurTransformation(viewHolder.itemView.getContext()))
                .into(viewHolder.imageViewBackground);
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getSrc())
                .fitCenter()
                .into(viewHolder.imageViewBackground2);
        viewHolder.slider_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details_intent=new Intent(context,DetailsActivity.class);
                details_intent.putExtra("media",sliderItem.getMedia());
                details_intent.putExtra("id",sliderItem.getId());
                context.startActivity(details_intent);
            }
        });
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground;
        ImageView imageViewBackground2;
        RelativeLayout slider_layout;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            imageViewBackground2 = itemView.findViewById(R.id.myimage2);
            slider_layout=itemView.findViewById(R.id.slider_layout);
            this.itemView = itemView;
        }
    }
}
