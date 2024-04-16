package com.example.pharmacy_app.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar; // Correct Toolbar import


import com.bumptech.glide.Glide;
import com.example.pharmacy_app.MainActivity;
import com.example.pharmacy_app.R;
import com.example.pharmacy_app.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;
import android.view.MenuItem;
import android.content.Intent;

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

    RatingBar ratingBar;

    ViewAllModel viewAllModel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        startAnimationWithDelay(findViewById(android.R.id.content), R.anim.scale_up_animation, 25);

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

            ratingBar = findViewById(R.id.rating_bar);  // set the rating bar

            // Get the rating text from the TextView
            String ratingText = ((TextView) findViewById(R.id.detailed_rating)).getText().toString();

            // Convert the rating text to float and set it to the RatingBar
            try
            {
                float rating = Float.parseFloat(ratingText);
                ratingBar.setRating(rating);
            }
            catch (NumberFormatException e)
            {
                Toast.makeText(this, "Rating Bar Error: " + e, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                // Handle parsing error if needed
            }
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
        });

        addItem.setOnClickListener(new View.OnClickListener()
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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            onBackPressed(); // Call onBackPressed() when home button is clicked
            return true;
        }
        if (id == R.id.action_settings) {
            // Handle sign out action
            // For example, navigate to the login activity
            Intent intent = new Intent(DetailedActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Optionally, finish this activity to prevent going back
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private void addedToCart()
//    {
//        String saveCurrentDate, saveCurrentTime;
//        Calendar calForDate = Calendar.getInstance();
//
//        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
//        saveCurrentDate = currentDate.format(calForDate.getTime());
//
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
//        saveCurrentTime = currentTime.format(calForDate.getTime());
//
//        final HashMap<String, Object> cartMap = new HashMap<>();
//
//        // map keys to the values and store the date and time as well
//        cartMap.put("productName", viewAllModel.getName());
//        cartMap.put("productPrice", price.getText().toString());
//        cartMap.put("currentDate", saveCurrentDate);
//        cartMap.put("currentTime", saveCurrentTime);
//        cartMap.put("totalQuantity", quantity.getText().toString());
//        cartMap.put("totalPrice", totalPrice);
//
//        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
//                .collection("CurrentUser").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task)
//                    {
//                        Toast.makeText(DetailedActivity.this, "Added To Cart : " + viewAllModel.getName(), Toast.LENGTH_SHORT).show();
////                        showCustomToast(DetailedActivity.this, R.drawable.ic_baseline_shopping_cart_24, "Added to Cart: " + viewAllModel.getName());
////                        showToast(DetailedActivity.this, "Added To Cart : " + viewAllModel.getName());
////                        showCustomToast("Added To Cart : " + viewAllModel.getName());
//                    }
//                });
//    }

//    // check if user logged in
//    private void addedToCart()
//    {
//        if (auth.getCurrentUser() != null)
//        {
//            String saveCurrentDate, saveCurrentTime;
//            Calendar calForDate = Calendar.getInstance();
//
//            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
//            saveCurrentDate = currentDate.format(calForDate.getTime());
//
//            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
//            saveCurrentTime = currentTime.format(calForDate.getTime());
//
//            final HashMap<String, Object> cartMap = new HashMap<>();
//
//            // map keys to the values and store the date and time as well
//            cartMap.put("productName", viewAllModel.getName());
//            cartMap.put("productPrice", price.getText().toString());
//            cartMap.put("currentDate", saveCurrentDate);
//            cartMap.put("currentTime", saveCurrentTime);
//            cartMap.put("totalQuantity", quantity.getText().toString());
//            cartMap.put("totalPrice", totalPrice);
//
//            firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
//                    .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>()
//                    {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentReference> task)
//                        {
//                            Toast.makeText(DetailedActivity.this, "Added To Cart : " + viewAllModel.getName(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//        else
//        {
//            // User is not logged in, show toast to login
//            Toast.makeText(DetailedActivity.this, "Please login to add items to cart", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void addedToCart() {
        if (auth.getCurrentUser() != null) {
            // Check if the item already exists in the cart based on the product name
            firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                    .collection("AddToCart")
                    .whereEqualTo("productName", viewAllModel.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // Check if the query result contains any documents
                                if (!task.getResult().isEmpty()) {
                                    // Item already exists, update its quantity in the database
                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    String documentId = documentSnapshot.getId();

                                    int existingQuantity = Integer.parseInt(documentSnapshot.getString("totalQuantity"));
                                    int newQuantity = existingQuantity + totalQuantity; // Update quantity

                                    // Update the quantity and total price in the cart
                                    HashMap<String, Object> updateMap = new HashMap<>();
                                    updateMap.put("totalQuantity", String.valueOf(newQuantity));
                                    updateMap.put("totalPrice", viewAllModel.getPrice() * newQuantity);

                                    firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                                            .collection("AddToCart").document(documentId)
                                            .update(updateMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> updateTask) {
                                                    if (updateTask.isSuccessful()) {
                                                        // Quantity updated successfully
                                                        Toast.makeText(DetailedActivity.this, "Item quantity updated in cart", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(DetailedActivity.this, "Failed to update item quantity", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // Item doesn't exist, add it as a new item to the cart
                                    addToCartAsNew();
                                }
                            } else {
                                Toast.makeText(DetailedActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // User is not logged in, show toast to login
            Toast.makeText(DetailedActivity.this, "Please login to add items to cart", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to add the item as a new entry in the cart
    private void addToCartAsNew() {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productName", viewAllModel.getName());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DetailedActivity.this, "Added To Cart: " + viewAllModel.getName(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailedActivity.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    /**
     * ShowToast
     */

    public void showToast(Context context, String info) {
        Toast toast = Toast.makeText(context, Html.fromHtml("<font color='#000000' ><b>" + info + "</b></font>"), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    private void showCustomToast(String message)
    {
        // Inflate the custom layout for the Toast
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, findViewById(R.id.custom_toast_layout));

        // Set the message text in the TextView
        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText(message);

//        ImageView imageView = layout.findViewById(R.id.image_cart);

        // Create the custom Toast and set its duration
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        // Set the custom layout as the Toast view
        toast.setView(layout);

        // Show the custom Toast
        toast.show();
    }

//    public static void showCustomToast(Context context, int imageResource, String message) {
//        // Inflate the custom toast layout
//        View toastView = LayoutInflater.from(context).inflate(R.layout.custom_toast_layout, null);
//
//        // Get references to the ImageView and TextView in the custom layout
//        ImageView toastImage = toastView.findViewById(R.id.toast_image);
//        TextView toastText = toastView.findViewById(R.id.toast_text);
//
//        // Set the custom image and text for the toast
//        toastImage.setImageResource(imageResource);
//        toastText.setText(message);
//
//        // Create a Toast object and set the custom view
//        Toast toast = new Toast(context);
//        toast.setView(toastView);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.show();
//    }

    private void startAnimationWithDelay(final View view, final int animationResId, final long delayMillis) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(view.getContext(), animationResId);
                view.startAnimation(animation);
            }
        }, delayMillis);
    }

}