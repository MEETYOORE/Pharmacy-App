package com.example.pharmacy_app.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacy_app.R;
import com.example.pharmacy_app.adapters.HomeAdapter;
import com.example.pharmacy_app.adapters.PopularAdapters;
import com.example.pharmacy_app.databinding.FragmentHomeBinding;
import com.example.pharmacy_app.models.HomeCategory;
import com.example.pharmacy_app.models.PopularModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment
{
    // firestore db acess
    FirebaseFirestore db;
    RecyclerView popularRec, homeCatRec;
    // popular items
    List<PopularModel> popularModelList;
    PopularAdapters popularAdapters;

    // home category
    List<HomeCategory> categoryList;
    HomeAdapter homeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
    {
       View root = inflater.inflate(R.layout.fragment_home, container, false);
       db = FirebaseFirestore.getInstance();

       popularRec = root.findViewById(R.id.pop_rec);    // popular recomendations
       homeCatRec = root.findViewById(R.id.explore_rec);    // explore recomnedations

       // popular items show
        {
            popularRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            popularModelList = new ArrayList<>();
            popularAdapters = new PopularAdapters(requireContext(), popularModelList);
            popularRec.setAdapter(popularAdapters);

            // read data firestore
            db.collection("PopularProducts")    // give reference id
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {

                            if (task.isSuccessful())
                            {
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    PopularModel popularModel = document.toObject(PopularModel.class);
                                    popularModelList.add(popularModel);
                                    popularAdapters.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
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

        // Home categories / explore items show
        {
            homeCatRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            categoryList = new ArrayList<>();
            homeAdapter = new HomeAdapter(getActivity(), categoryList);
            homeCatRec.setAdapter(homeAdapter);

            // read data firestore
            db.collection("HomeCategory")    // give reference id
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {

                            if (task.isSuccessful())
                            {
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    HomeCategory homeCategory = document.toObject(HomeCategory.class);
                                    categoryList.add(homeCategory);
                                    homeAdapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
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