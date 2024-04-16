package com.example.pharmacy_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pharmacy_app.MainActivity;
import com.example.pharmacy_app.R;
import com.example.pharmacy_app.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacedOrderActivity extends AppCompatActivity
{
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        button = findViewById(R.id.backToHomeButton);

        List<MyCartModel> list = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList");

        if(list != null && list.size() > 0)
        {
            for(MyCartModel model : list)
            {
                final HashMap<String, Object> cartMap = new HashMap<>();

                // map keys to the values and store the date and time as well
                cartMap.put("productName", model.getProductName());
                cartMap.put("productPrice", model.getProductPrice());
                cartMap.put("currentDate", model.getCurrentDate());
                cartMap.put("currentTime", model.getCurrentTime());
                cartMap.put("totalQuantity", model.getTotalQuantity());
                cartMap.put("totalPrice", model.getTotalPrice());

                // add Cart Items to MyOrder db and clear cart
                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("MyOrder").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task)
                            {
                                Toast.makeText(PlacedOrderActivity.this, "Your Order Has Been Placed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            // Clear the cart after placing the order
            clearCart();
        }

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(PlacedOrderActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clearCart()
    {
        // Delete all items from the "AddToCart" collection
        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("AddToCart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot document : task.getResult().getDocuments())
                            {
                                document.getReference().delete();
                            }
//                            Toast.makeText(PlacedOrderActivity.this, "Cart cleared", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(PlacedOrderActivity.this, "Failed to clear cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
