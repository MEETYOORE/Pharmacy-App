package com.example.pharmacy_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.pharmacy_app.R;
import com.example.pharmacy_app.adapters.HomeAdapter;
import com.example.pharmacy_app.adapters.NavCategoryDetailedAdapter;
import com.example.pharmacy_app.models.HomeCategory;
import com.example.pharmacy_app.models.NavCategoryDetailedModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NavCategoryActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    List<NavCategoryDetailedModel> list;
    NavCategoryDetailedAdapter adapter;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_category);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.nav_cat_det_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new NavCategoryDetailedAdapter(this, list);
        recyclerView.setAdapter(adapter);

//        // AllProducts
//        {
//            // read data firestore
//            db.collection("AllProducts")    // give reference id
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
//                    {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task)
//                        {
//
//                            if (task.isSuccessful())
//                            {
//                                for (QueryDocumentSnapshot document : task.getResult())
//                                {
//                                    NavCategoryDetailedModel navCategoryDetailedModel = document.toObject(NavCategoryDetailedModel.class);
//                                    list.add(navCategoryDetailedModel);
//                                    adapter.notifyDataSetChanged();
//                                    //Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            else
//                            {
//                                if (task.getException() != null)
//                                {
//                                    Toast.makeText(NavCategoryActivity.this, "ERROR " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                                else
//                                {
//                                    Toast.makeText(NavCategoryActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    });
//        }

        // Products of 'type'
        {
            String type = getIntent().getStringExtra("type");

            db.collection("AllProducts")
                    .whereEqualTo("type", type) // Filter by type
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            if (task.isSuccessful())
                            {
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    NavCategoryDetailedModel navCategoryDetailedModel = document.toObject(NavCategoryDetailedModel.class);
                                    list.add(navCategoryDetailedModel);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            else
                            {
                                if (task.getException() != null)
                                {
                                    Toast.makeText(NavCategoryActivity.this, "ERROR " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(NavCategoryActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

        }
    }
}