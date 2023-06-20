package com.example.videostreamingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_activity);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        List<Item> items = new ArrayList<Item>();
        items.add(new Item("Shaunaq", "shaunaqpaulofficial@gmail.com", R.drawable.a));
        items.add(new Item("Milee", "mileeofficial@gmail.com", R.drawable.b));
        items.add(new Item("Emma", "emmaofficial@gmail.com", R.drawable.c));
        items.add(new Item("Martin", "martinofficial@gmail.com", R.drawable.d));
        items.add(new Item("Elise", "eliseofficial@gmail.com", R.drawable.e));
        items.add(new Item("John", "johnofficial@gmail.com", R.drawable.f));
        items.add(new Item("Buck", "buckofficial@gmail.com", R.drawable.g));
        items.add(new Item("Uriel", "urielofficial@gmail.com", R.drawable.h));
        items.add(new Item("Shaunaq", "shaunaqpaulofficial@gmail.com", R.drawable.a));
        items.add(new Item("Milee", "mileeofficial@gmail.com", R.drawable.b));
        items.add(new Item("Emma", "emmaofficial@gmail.com", R.drawable.c));
        items.add(new Item("Martin", "martinofficial@gmail.com", R.drawable.d));
        items.add(new Item("Elise", "eliseofficial@gmail.com", R.drawable.e));
        items.add(new Item("John", "johnofficial@gmail.com", R.drawable.f));
        items.add(new Item("Buck", "buckofficial@gmail.com", R.drawable.g));
        items.add(new Item("Uriel", "urielofficial@gmail.com", R.drawable.h));
        items.add(new Item("Shaunaq", "shaunaqpaulofficial@gmail.com", R.drawable.a));
        items.add(new Item("Milee", "mileeofficial@gmail.com", R.drawable.b));
        items.add(new Item("Emma", "emmaofficial@gmail.com", R.drawable.c));
        items.add(new Item("Martin", "martinofficial@gmail.com", R.drawable.d));
        items.add(new Item("Elise", "eliseofficial@gmail.com", R.drawable.e));
        items.add(new Item("John", "johnofficial@gmail.com", R.drawable.f));
        items.add(new Item("Buck", "buckofficial@gmail.com", R.drawable.g));
        items.add(new Item("Uriel", "urielofficial@gmail.com", R.drawable.h));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), items));
    }
}