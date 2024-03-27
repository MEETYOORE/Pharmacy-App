package com.example.pharmacy_app.ui.category;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacy_app.R;
import com.example.pharmacy_app.adapters.NavCategoryAdapter;
import com.example.pharmacy_app.adapters.PopularAdapters;
import com.example.pharmacy_app.models.NavCategoryModel;
import com.example.pharmacy_app.models.PopularModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryFragment extends Fragment
{
    FirebaseFirestore db;
    RecyclerView recyclerView;
    List<NavCategoryModel> categoryModelList;
    NavCategoryAdapter navCategoryAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_category, container, false);
        db = FirebaseFirestore.getInstance();

        recyclerView = root.findViewById(R.id.cat_rec);

        {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            categoryModelList = new ArrayList<>();
            navCategoryAdapter = new NavCategoryAdapter(requireContext(), categoryModelList);
            recyclerView.setAdapter(navCategoryAdapter);

            // read data firestore
            db.collection("NavCategory")    // give reference id
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {

                            if (task.isSuccessful())
                            {

//                                for (QueryDocumentSnapshot document : task.getResult())
//                                {
//                                    Toast.makeText(getActivity(), "entered", Toast.LENGTH_SHORT).show();
//
//                                    NavCategoryModel navCategoryModel = document.toObject(NavCategoryModel.class);
//                                    categoryModelList.add(navCategoryModel);
//                                    navCategoryAdapter.notifyDataSetChanged();
//
//                                    //Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
//                                }

                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    try
                                    {
                                        NavCategoryModel navCategoryModel = document.toObject(NavCategoryModel.class);
                                        categoryModelList.add(navCategoryModel);
                                        navCategoryAdapter.notifyDataSetChanged();
                                    }
                                    catch (Exception e)
                                    {
                                        Log.e("Exception", "Error processing document: " + e.getMessage());

                                        Toast.makeText(getActivity(), ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }

                            }
                            else
                            {
                                if (task.getException() != null)
                                {
                                    Toast.makeText(getActivity(), "ERROR " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Unknown error occurred", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }


        return root;
    }

}