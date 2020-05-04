package com.example.simplechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Home extends AppCompatActivity {
    RecyclerView recycLv;
    RecyAdapter recyAdapter;
    TextView tvUsernameHome;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        recycLv = findViewById(R.id.recyclerLv);
        tvUsernameHome = findViewById(R.id.tvUsernameHome);
        Intent intent = getIntent();
        tvUsernameHome.setText("Xin chào "+intent.getStringExtra("1") +" "+intent.getStringExtra("2"));
        recyAdapter = new RecyAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recycLv.addItemDecoration(dividerItemDecoration);
        recycLv.setLayoutManager(new LinearLayoutManager(this));
        recycLv.setAdapter(recyAdapter);
    }
}
