package com.example.videostreamingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GiftGridActivity extends AppCompatActivity {

    int[] giftModelImage1 = {
            R.drawable.gift_0_confused,
            R.drawable.gift_1_volleyball,
            R.drawable.gift_2_coffee,
            R.drawable.gift_0_confused,
            R.drawable.gift_1_volleyball,
            R.drawable.gift_2_coffee,
            R.drawable.gift_0_confused,
            R.drawable.gift_1_volleyball,
            R.drawable.gift_2_coffee,
    };

    int[] giftModelImage2 = {
            R.drawable.coin_icon_currency,
            R.drawable.coin_icon_currency,
            R.drawable.coin_icon_currency,
            R.drawable.coin_icon_currency,
            R.drawable.coin_icon_currency,
            R.drawable.coin_icon_currency,
            R.drawable.coin_icon_currency,
            R.drawable.coin_icon_currency,
            R.drawable.coin_icon_currency,
    };

    String[] giftModelText = {
            "59", "89", "99","59", "89", "99","59", "89", "99"
    };

    private BottomSheetBehavior<FrameLayout> bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_grid);

        FrameLayout bottomSheet = findViewById(R.id.frameBottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(200);
        GridLayout gridLayout = findViewById(R.id.gridEmoteLayout);

        for (int i = 0; i < giftModelImage1.length; i++) {
            View gridItemView = LayoutInflater.from(this).inflate(R.layout.grid_item_view, null);

            ImageView imageView1 = gridItemView.findViewById(R.id.imageEmoteView);
            ImageView imageView2 = gridItemView.findViewById(R.id.imageCurrencyEmoteView);
            TextView textView = gridItemView.findViewById(R.id.textCurrencyEmoteView);

            imageView1.setImageResource(giftModelImage1[i]);
            imageView2.setImageResource(giftModelImage2[i]);
            textView.setText(giftModelText[i]);

            gridLayout.addView(gridItemView);

            GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) gridItemView.getLayoutParams();
            layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            gridItemView.setLayoutParams(layoutParams);
        }

    }

}