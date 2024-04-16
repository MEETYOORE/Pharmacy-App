package com.example.pharmacy_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacy_app.adapters.MyCartAdapter;
import com.example.pharmacy_app.adapters.MyOrderAdapter;
import com.example.pharmacy_app.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment {
    FirebaseFirestore db;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    MyOrderAdapter cartAdapter;
    List<MyCartModel> cartModelList;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_my_orders, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.GONE);  // initially invisible while data loads
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartModelList = new ArrayList<>();
        cartAdapter = new MyOrderAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(cartAdapter);

        loadOrders(); // Fetch and display orders from Firestore

        return root;
    }

    private void loadOrders() {
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("MyOrder").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            cartModelList.clear(); // Clear existing data

                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
                                if (cartModel != null) {
                                    cartModelList.add(cartModel); // Add each order (cart item) to the list
                                }
                            }

                            recyclerView.setVisibility(View.VISIBLE);

                            cartAdapter.notifyDataSetChanged(); // Notify adapter about data changes
                        } else {
                            Toast.makeText(getActivity(), "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
