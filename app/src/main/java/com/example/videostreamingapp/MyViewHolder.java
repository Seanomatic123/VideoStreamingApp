package com.example.videostreamingapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView recyclerImageView;
    TextView nameTextView;
    TextView emailTextView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recyclerImageView = itemView.findViewById(R.id.recyclerImageView);
        nameTextView = itemView.findViewById(R.id.nameTextView);
        emailTextView = itemView.findViewById(R.id.emailTextView);
    }
}
