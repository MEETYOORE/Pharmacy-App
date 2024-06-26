package com.example.pharmacy_app.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import com.example.pharmacy_app.adapters.RecommendedAdapter;
import com.example.pharmacy_app.adapters.ViewAllAdapter;
import com.example.pharmacy_app.databinding.FragmentHomeBinding;
import com.example.pharmacy_app.models.HomeCategory;
import com.example.pharmacy_app.models.PopularModel;
import com.example.pharmacy_app.models.RecommendedModel;
import com.example.pharmacy_app.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment
{
    ScrollView scrollView;
    ProgressBar progressBar;


    // firestore db acess
    FirebaseFirestore db;
    RecyclerView popularRec, homeCatRec, recommendedRec;

    // Popular items
    List<PopularModel> popularModelList;
    PopularAdapters popularAdapters;

    // Search box
    EditText search_box;
    List<ViewAllModel> viewAllModelList;
    RecyclerView recyclerViewSearch;    // Search results
    ViewAllAdapter viewAllAdapter;

    // Home items
    List<HomeCategory> categoryList;
    HomeAdapter homeAdapter;

    // Recommended items
//    List<RecommendedModel> recommendedModelList;
    List<ViewAllModel> recommendedModelList;
//    RecommendedAdapter recommendedAdapter;
    ViewAllAdapter recommendedAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
    {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();

        popularRec = root.findViewById(R.id.pop_rec);    // popular recommendations
        homeCatRec = root.findViewById(R.id.explore_rec);    // explore categories
        recommendedRec = root.findViewById(R.id.recommended_rec);   // recommended recommendations
        scrollView = root.findViewById(R.id.scroll_view);   // scrollbar
        progressBar = root.findViewById(R.id.progressbar);   // scrollbar

        progressBar.setVisibility(View.VISIBLE);    // progress bar show
        scrollView.setVisibility(View.GONE);    // initally scrollbar hidden

//       // popular items show
//        {
//            popularRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
//            popularModelList = new ArrayList<>();
//            popularAdapters = new PopularAdapters(requireContext(), popularModelList);
//            popularRec.setAdapter(popularAdapters);
//
//            // read data firestore
//            db.collection("PopularProducts")    // give reference id
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
//                    {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task)
//                        {
//
//                            if (task.isSuccessful())
//                            {
////                                for (QueryDocumentSnapshot document : task.getResult())
////                                {
////                                    PopularModel popularModel = document.toObject(PopularModel.class);
////                                    popularModelList.add(popularModel);
////                                    popularAdapters.notifyDataSetChanged();
////                                    progressBar.setVisibility(View.VISIBLE);
//
////                                    //Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
////                                }
//                                for (QueryDocumentSnapshot document : task.getResult())
//                                {
//                                    PopularModel popularModel = document.toObject(PopularModel.class);
//                                    popularModelList.add(popularModel);
//                                }
//
//                                // Shuffle the list after populating it
//                                Collections.shuffle(popularModelList);
//
//                                // Update the RecyclerView adapter
//                                popularAdapters.notifyDataSetChanged();
//
//                                progressBar.setVisibility(View.GONE);    // progress bar hide once db connected
//                                scrollView.setVisibility(View.VISIBLE);    //  scrollbar show once db connect
//
//                            }
//                            else
//                            {
//                                if (task.getException() != null)
//                                {
//                                    Toast.makeText(getActivity(), "ERROR " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                                else
//                                {
//                                    Toast.makeText(getActivity(), "Unknown error occurred", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    });
//        }

        // popular items show for now random all items
        {
            popularRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            popularModelList = new ArrayList<>();
            popularAdapters = new PopularAdapters(requireContext(), popularModelList);
            popularRec.setAdapter(popularAdapters);

            // read data firestore
            db.collection("AllProducts")    // give reference id
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
//                                    PopularModel popularModel = document.toObject(PopularModel.class);
//                                    popularModelList.add(popularModel);
//                                    popularAdapters.notifyDataSetChanged();
//                                    progressBar.setVisibility(View.VISIBLE);

//                                    //Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
//                                }
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    PopularModel popularModel = document.toObject(PopularModel.class);
                                    popularModelList.add(popularModel);
                                }

                                // Shuffle the list after populating it
                                Collections.shuffle(popularModelList);

                                // Update the RecyclerView adapter
                                popularAdapters.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);    // progress bar hide once db connected
                                scrollView.setVisibility(View.VISIBLE);    //  scrollbar show once db connect

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

        // Home categories ie: explore items by category
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
                                    //Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
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

//        // Recommended Recommendations
//        {
//            recommendedRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
//            recommendedModelList = new ArrayList<>();
//            recommendedAdapter = new RecommendedAdapter(getActivity(), recommendedModelList);
//            recommendedRec.setAdapter(recommendedAdapter);
//
//            // read data firestore
//            db.collection("Recommended")    // give reference id
//            .get()
//            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
//            {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task)
//                {
//
//                    if (task.isSuccessful())
//                    {
//                        for (QueryDocumentSnapshot document : task.getResult())
//                        {
//                            RecommendedModel recommendedModel = document.toObject(RecommendedModel.class);
//                            recommendedModelList.add(recommendedModel);
//                            recommendedAdapter.notifyDataSetChanged();
//                            //Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else
//                    {
//                        if (task.getException() != null)
//                        {
//                            Toast.makeText(getActivity(), "ERROR " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
//                            Toast.makeText(getActivity(), "Unknown error occurred", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            });
//        }

        // Recommended Recommendations only for demo
        {
//            recommendedRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
//            recommendedModelList = new ArrayList<>();
//            recommendedAdapter = new RecommendedAdapter(getActivity(), recommendedModelList);
//            recommendedRec.setAdapter(recommendedAdapter);
            recommendedRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            recommendedModelList = new ArrayList<>();
            recommendedAdapter = new ViewAllAdapter(getActivity(), recommendedModelList);
            recommendedRec.setAdapter(recommendedAdapter);

            // read data firestore
            db.collection("AllProducts")    // give reference id
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {

                    if (task.isSuccessful())
                    {
//                        for (QueryDocumentSnapshot document : task.getResult())
//                        {
//                            RecommendedModel recommendedModel = document.toObject(RecommendedModel.class);
//                            recommendedModelList.add(recommendedModel);
//                            recommendedAdapter.notifyDataSetChanged();
//                            //Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
//                        }

                        for (QueryDocumentSnapshot document : task.getResult())
                        {
//                            RecommendedModel recommendedModel = document.toObject(RecommendedModel.class);
                            ViewAllModel recommendedModel = document.toObject(ViewAllModel.class);
                            recommendedModelList.add(recommendedModel);
                        }

                        // Shuffle the list after populating it
                        Collections.shuffle(recommendedModelList);

                        int count = 10; // Number of products to display
                        if (recommendedModelList.size() < count) {
                            count = recommendedModelList.size(); // If there are less than 10 products, display all
                        }

                        // Take the first 10 products from the shuffled list
//                        List<RecommendedModel> randomProductsList = recommendedModelList.subList(0, count);
                        List<ViewAllModel> randomProductsList = recommendedModelList.subList(0, count);

                        // Update the RecyclerView adapter
                        recommendedAdapter.notifyDataSetChanged();
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

        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(getContext(), viewAllModelList);

        search_box = root.findViewById(R.id.search_box);
        recyclerViewSearch = root.findViewById(R.id.search_rec);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(viewAllAdapter);
        recyclerViewSearch.setHasFixedSize(true);
        search_box.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(s.toString().isEmpty())
                {
                    viewAllModelList.clear();
                    viewAllAdapter.notifyDataSetChanged();
                }
                else
                {
                    searchProduct(s.toString());
                }
            }
        });

       return root;
    }

    private void searchProduct(String type)
    {
        if(!type.isEmpty())
        {
            db.collection("AllProducts").whereEqualTo("type", type).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            if(task.isSuccessful() && task.getResult() != null)
                            {
                                viewAllModelList.clear();
                                viewAllAdapter.notifyDataSetChanged();

                                for(DocumentSnapshot doc : task.getResult().getDocuments())
                                {
                                    ViewAllModel viewAllModel = doc.toObject(ViewAllModel.class);
                                    viewAllModelList.add(viewAllModel);
                                    viewAllAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
    }


}