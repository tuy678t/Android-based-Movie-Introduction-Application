package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent details_intent = getIntent();
        String rate = details_intent.getStringExtra("rate");
        String info = details_intent.getStringExtra("info");
        String cont = details_intent.getStringExtra("cont");

        TextView rinfo=findViewById(R.id.activity_review_info);
        rinfo.setText(info);
        TextView rrate=findViewById(R.id.activity_review_rate);
        rrate.setText(rate);
        TextView rcont=findViewById(R.id.activity_review_cont);
        rcont.setText(cont);




    }
}