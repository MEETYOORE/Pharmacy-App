package com.example.pharmacy_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.pharmacy_app.R;
import com.example.pharmacy_app.adapters.ViewAllAdapter;
import com.example.pharmacy_app.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;
import com.google.firestore.v1.FirestoreGrpc;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity
{
    FirebaseFirestore firestore;
    List<ViewAllModel> viewAllModelList;
    ViewAllAdapter viewAllAdapter;
    RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        firestore = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the typr using key 'type' from popular model
        String type = getIntent().getStringExtra("type");
        recyclerView = findViewById(R.id.view_all_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(this, viewAllModelList);
        recyclerView.setAdapter(viewAllAdapter);


        // check if belongs to category "antibiotics"
        if(type != null && type.equalsIgnoreCase("antibiotics"))
        {
            firestore.collection("AllProducts").whereEqualTo("type", "antibiotics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
//                    Toast.makeText(ViewAllActivity.this, "type: " + type, Toast.LENGTH_SHORT).show();

                    if (task.isSuccessful())
                    {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                            viewAllModelList.add(viewAllModel);
                            viewAllAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        if (task.getException() != null)
                        {
                            Toast.makeText(ViewAllActivity.this, "ERROR " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ViewAllActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        // check if belongs to category "antibiotics"
        if(type != null && type.equalsIgnoreCase("syrup")) {
            firestore.collection("AllProducts").whereEqualTo("type", "syrup").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();

                    }
                }
            });
        }

        // check if belongs to category "drops"
        if(type != null && type.equalsIgnoreCase("drops")) {
            firestore.collection("AllProducts").whereEqualTo("type", "drops").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();

                    }
                }
            });
        }

        // check if belongs to category "tablets"
        if(type != null && type.equalsIgnoreCase("tablets")) {
            firestore.collection("AllProducts").whereEqualTo("type", "tablets").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();

                    }
                }
            });
        }

        // check if belongs to category "ointment"
        if(type != null && type.equalsIgnoreCase("ointment")) {
            firestore.collection("AllProducts").whereEqualTo("type", "ointment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();

                    }
                }
            });
        }


    }
}