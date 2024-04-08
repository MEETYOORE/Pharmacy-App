package com.example.pharmacy_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pharmacy_app.adapters.MyCartAdapter;
import com.example.pharmacy_app.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment
{
    FirebaseFirestore db;
    FirebaseAuth auth;

    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;

    ConstraintLayout constraint1, constraint2;

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

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(cartAdapter);

        db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments())
                            {
                                MyCartModel cartModel = documentSnapshot.toObject((MyCartModel.class));
                                cartModelList.add(cartModel);
                                cartAdapter.notifyDataSetChanged();
                            }

                            if (cartModelList.isEmpty()) {
                                // Show constraint1 if cart is empty
                                constraint1.setVisibility(View.VISIBLE);
                                constraint2.setVisibility(View.GONE);
                            } else {
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

        return root;
    }
}