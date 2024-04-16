package com.example.pharmacy_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacy_app.R;
import com.example.pharmacy_app.models.MyCartModel;

import java.util.List;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder>
{
    Context context;
    List<MyCartModel> cartModelList;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    OnItemDeletedListener itemDeletedListener; // Interface for communication with fragment

    public interface OnItemDeletedListener
    {
        void onItemDeleted();
    }

    public void setOnItemDeletedListener(OnItemDeletedListener listener)
    {
        this.itemDeletedListener = listener;
    }

    public MyCartAdapter(Context context, List<MyCartModel> cartModelList)
    {
        this.context = context;
        this.cartModelList = cartModelList;

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.alternate_my_cart_item_2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.name.setText(cartModelList.get(position).getProductName());
        holder.price.setText(cartModelList.get(position).getProductPrice());
        holder.date.setText(cartModelList.get(position).getCurrentDate());
        holder.time.setText(cartModelList.get(position).getCurrentTime());
        holder.quantity.setText(cartModelList.get(position).getTotalQuantity());
        holder.totalPrice.setText(String.valueOf(cartModelList.get(position).getTotalPrice())); // as float

//        holder.deleteItem.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
//                        .collection("AddToCart")
//                        .document(cartModelList.get(position).getDocumentId())
//                        .delete()
//                        .addOnCompleteListener(new OnCompleteListener<Void>()
//                        {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task)
//                            {
//                                if(task.isSuccessful())
//                                {
//                                    cartModelList.remove(cartModelList.get(position));
//                                    notifyDataSetChanged();
//                                    // Notify the fragment that an item is deleted
//                                    if (itemDeletedListener != null)
//                                    {
//                                        itemDeletedListener.onItemDeleted();
//                                    }
//
//                                    Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();
//                                }
//                                else
//                                {
//                                    Toast.makeText(context, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });

        // use product name to delete
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productNameToDelete = cartModelList.get(position).getProductName();

                // Find the item by product name and delete it
                for (MyCartModel cartItem : cartModelList) {
                    if (cartItem.getProductName().equals(productNameToDelete)) {
                        // Delete the item from Firestore
                        firestore.collection("CurrentUser")
                                .document(auth.getCurrentUser().getUid())
                                .collection("AddToCart")
                                .document(cartItem.getDocumentId())
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Remove the item from the list
                                            cartModelList.remove(cartItem);
                                            notifyDataSetChanged();
                                            // Notify the fragment that an item is deleted
                                            if (itemDeletedListener != null) {
                                                itemDeletedListener.onItemDeleted();
                                            }
                                            Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        break; // Exit the loop after deleting the item
                    }
                }
            }
        });


        // pass total amount to MyCartFragment
//        totalPrice = totalPrice + cartModelList.get(position).getTotalPrice();  // add total price of each item in cart
//        Intent intent = new Intent("MyTotalAmount");
//        intent.putExtra("totalAmount", (double) totalPrice);
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, price, date, time, quantity, totalPrice;
        ImageView deleteItem;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            deleteItem = itemView.findViewById(R.id.delete);

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            date = itemView.findViewById(R.id.current_date);
            time = itemView.findViewById(R.id.current_time);
            quantity = itemView.findViewById(R.id.total_quantity);
            totalPrice = itemView.findViewById(R.id.total_price);
        }
    }

}
