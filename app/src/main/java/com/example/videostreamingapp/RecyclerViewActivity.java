package com.example.videostreamingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<ItemModel> itemModels = new ArrayList<>();
    int[] itemModelsImage = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
            R.drawable.f,
            R.drawable.g,
            R.drawable.h
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_activity);



        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        setUpItemModels();
        MyAdapter adapter = new MyAdapter(this, itemModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setUpItemModels() {
        String[] itemModelsName = getResources().getStringArray(R.array.item_Models_Name);
        String[] itemModelsEmail = getResources().getStringArray(R.array.item_Models_Email);
        String[] itemModelsDescription = getResources().getStringArray(R.array.item_Models_Description);

        for (int i = 0; i<itemModelsName.length; i++){
            itemModels.add(new ItemModel(itemModelsName[i],
                                         itemModelsEmail[i],
                                         itemModelsImage[i],
                                         itemModelsDescription[i]));
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(RecyclerViewActivity.this, ProfileDetailActivity.class);
        intent.putExtra("ITEM", itemModels.get(position));
        startActivity(intent);
    }
}