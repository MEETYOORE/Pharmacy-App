package com.example.pharmacy_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacy_app.activities.PlacedOrderActivity;
import com.example.pharmacy_app.adapters.MyCartAdapter;
import com.example.pharmacy_app.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyCartFragment extends Fragment
{
    ProgressBar progressBar;

    FirebaseFirestore db;
    FirebaseAuth auth;

    TextView overTotalAmount;
    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;

    ConstraintLayout constraint1, constraint2;

    Button buyNow;

    public MyCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_cart, container, false);

        constraint1 = root.findViewById(R.id.constraint1);
        constraint2 = root.findViewById(R.id.constraint2);

        progressBar = root.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        buyNow = root.findViewById(R.id.buy_now);

        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.GONE);  // intially invisible while firestore loads
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        overTotalAmount = root.findViewById(R.id.total_cart_id);

//        LocalBroadcastManager.getInstance(getActivity())
//                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(cartAdapter);

//        if (cartModelList.isEmpty())
//        {
//            // Show constraint1 if cart is empty
//            constraint1.setVisibility(View.VISIBLE);
//            constraint2.setVisibility(View.GONE);
//        }
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            double totalAmount = 0.0; // Initialize total amount

                            for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments())
                            {
                                MyCartModel cartModel = documentSnapshot.toObject((MyCartModel.class));
                                cartModelList.add(cartModel);
//                                cartAdapter.notifyDataSetChanged();

                                // Calculate total amount directly from Firestore data
                                totalAmount += cartModel.getTotalPrice();

                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }

                            // Set the total amount to the TextView
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            String formattedTotalAmount = decimalFormat.format(totalAmount);
                            overTotalAmount.setText("Total Cart: ₹ " + formattedTotalAmount);

                            cartAdapter.notifyDataSetChanged(); // Notify adapter about data changes

                            if (cartModelList.isEmpty())
                            {
                                // Show constraint1 if cart is empty
                                constraint1.setVisibility(View.VISIBLE);
                                constraint2.setVisibility(View.GONE);
                            }
                            else {
                                // Hide constraint1 if cart is not empty
                                constraint1.setVisibility(View.GONE);
                                constraint2.setVisibility(View.VISIBLE);
                            }

                        }
                        else
                        {
                            Toast.makeText(getActivity(), "not successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        buyNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), PlacedOrderActivity.class);
                intent.putExtra("itemList", (Serializable) cartModelList);
                startActivity(intent);

            }
        });

        return root;
    }

//    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent)
//        {
//            double totalBill = intent.getDoubleExtra("totalAmount", 0.0);
//
//            // Format the totalBill to display as currency with two decimal places
//            DecimalFormat decimalFormat = new DecimalFormat("0.00");
//            String formattedTotalBill = decimalFormat.format(totalBill);
//
//            overTotalAmount.setText("Total Cart: ₹ " + formattedTotalBill);
//        }
//    };


}