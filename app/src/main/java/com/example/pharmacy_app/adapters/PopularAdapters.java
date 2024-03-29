package com.example.pharmacy_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pharmacy_app.R;
import com.example.pharmacy_app.activities.ViewAllActivity;
import com.example.pharmacy_app.models.PopularModel;

import java.util.Collections;
import java.util.List;

public class PopularAdapters extends RecyclerView.Adapter<PopularAdapters.ViewHolder>
{
    private Context context;
    private List<PopularModel> popularModelList;

    public PopularAdapters(Context context, List<PopularModel> popularModelList)
    {
        this.context = context;
        this.popularModelList = popularModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        Glide.with(context).load(popularModelList.get(position).getImg_url()).into(holder.img_url);

        holder.name.setText(popularModelList.get(position).getName());
        holder.rating.setText(popularModelList.get(position).getRating());
        holder.description.setText(popularModelList.get(position).getDescription());
        holder.discount.setText(popularModelList.get(position).getDiscount());
        holder.type.setText(popularModelList.get(position).getType());

        // on click go to the product page
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, ViewAllActivity.class);
                intent.putExtra("type", popularModelList.get(position).getType());
                context.startActivity(intent);
            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("ViewHolder", "Item clicked at position: " + position);
//                String itemType = popularModelList.get(position).getType();
//                Log.d("ViewHolder", "Item type: " + itemType);
//                Intent intent = new Intent(context, ViewAllActivity.class);
//                intent.putExtra("type", itemType);
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount()
    {
        return popularModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_url;
        TextView name, description, rating, discount, type;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            img_url = itemView.findViewById(R.id.pop_img);
            name = itemView.findViewById(R.id.pop_name);
            description = itemView.findViewById(R.id.pop_des);
            discount = itemView.findViewById(R.id.pop_discount);
            rating = itemView.findViewById(R.id.pop_rating);
            type = itemView.findViewById(R.id.pop_type);
        }
    }
}
