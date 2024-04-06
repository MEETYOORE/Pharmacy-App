package com.example.pharmacy_app.activities;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;

public class DetailedActivity extends AppCompatActivity
{
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    TextView quantity;
    int totalQuantity = 1;
    float totalPrice = 0;

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

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

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


        quantity = findViewById(R.id.quantity);
        detailedImg = findViewById(R.id.detailed_img);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);
        price = findViewById(R.id.detailed_price);
        rating = findViewById(R.id.detailed_rating);
        description = findViewById(R.id.detailed_description);
        addToCart = findViewById(R.id.add_to_cart);

        if(viewAllModel != null)
        {
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detailedImg);

            rating.setText(viewAllModel.getRating());
            description.setText(viewAllModel.getDescription());
            price.setText("Price : ₹ " + viewAllModel.getPrice());

            totalPrice = viewAllModel.getPrice() * totalQuantity;
        }

        else
        {
            Toast.makeText(this, "null error: "+object, Toast.LENGTH_SHORT).show();
        }

        addToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addedToCart();
            }
        });        addItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(totalQuantity < 10)
                {
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice = viewAllModel.getPrice() * totalQuantity;
                }
            }
        });
        removeItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(totalQuantity > 1)
                {
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice = viewAllModel.getPrice() * totalQuantity;
                }
            }
        });


    }

    private void addedToCart()
    {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();

        // map keys to the values and store the date and time as well
        cartMap.put("productName", viewAllModel.getName());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task)
                    {
                        Toast.makeText(DetailedActivity.this, "Added To A Cart : " + viewAllModel.getName(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}