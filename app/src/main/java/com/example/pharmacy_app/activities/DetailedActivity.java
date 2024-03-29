package com.example.pharmacy_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar; // Correct Toolbar import


import com.bumptech.glide.Glide;
import com.example.pharmacy_app.R;
import com.example.pharmacy_app.models.ViewAllModel;

public class DetailedActivity extends AppCompatActivity
{
    ImageView detailedImg;
    TextView price, rating, description;
    Button addToCart;
    ImageView addItem, removeItem;
    Toolbar toolbar;

    ViewAllModel viewAllModel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // used for passing object between activities ( from ViewAllAdapter)
        final Object object = getIntent().getSerializableExtra("detail");

        if(object instanceof ViewAllModel)
        {
            viewAllModel = (ViewAllModel) object;
        }

//        final Object object = getIntent().getSerializableExtra("detail");
//
//        if (object != null)
//        {
//            Log.d("DetailedActivity", "Received object: " + object.toString());
//            if (object instanceof ViewAllModel) {
//                Toast.makeText(this, "Object is an instance of ViewAllModel", Toast.LENGTH_SHORT).show();
//                viewAllModel = (ViewAllModel) object;
//                // Debugging: Print viewAllModel fields to check if they are properly initialized
//                Log.d("DetailedActivity", "Name: " + viewAllModel.getName());
//                Log.d("DetailedActivity", "Description: " + viewAllModel.getDescription());
//                // Print other fields as needed
//            } else {
//                Toast.makeText(this, "Object is not an instance of ViewAllModel", Toast.LENGTH_SHORT).show();
//                Log.e("DetailedActivity", "Object type: " + object.getClass().getName());
//            }
//        } else {
//            Toast.makeText(this, "Object is null", Toast.LENGTH_SHORT).show();
//        }


        detailedImg = findViewById(R.id.detailed_img);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);
        price = findViewById(R.id.detailed_price);
        rating = findViewById(R.id.detailed_rating);
        description = findViewById(R.id.detailed_description);

        if(viewAllModel != null)
        {
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detailedImg);
            rating.setText(viewAllModel.getRating());
            description.setText(viewAllModel.getDescription());
            price.setText("Price : ₹ " + viewAllModel.getPrice());
        }

        else
        {
            Toast.makeText(this, "null error: "+object, Toast.LENGTH_SHORT).show();
        }
        addToCart = findViewById(R.id.add_to_cart);
    }
}