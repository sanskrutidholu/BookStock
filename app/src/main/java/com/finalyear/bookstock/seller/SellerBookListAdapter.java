package com.finalyear.bookstock.seller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalyear.bookstock.R;
import com.finalyear.bookstock.extras.SellingBook;

import java.util.ArrayList;

public class SellerBookListAdapter extends RecyclerView.Adapter<SellerBookListAdapter.SellingBooksViewHolder> {
    Context context;
    ArrayList<SellingBook> sellingBookArrayList;

    public SellerBookListAdapter(Context context, ArrayList<SellingBook> sellingBookArrayList) {
        this.context = context;
        this.sellingBookArrayList = sellingBookArrayList;
    }

    @NonNull
    @Override
    public SellingBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_row_book, parent, false);
        return new SellingBooksViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull SellingBooksViewHolder viewHolder, int position) {
        SellingBook model = sellingBookArrayList.get(position);
        viewHolder.row_price.setText(String.format("%d INR", model.getSellingprice()));
        viewHolder.row_quantity.setText(String.format("%d pcs", model.getQuantities()));
        final String final_query=model.getBookid();
        viewHolder.row_title.setText(model.getTitle());
        viewHolder.row_author.setText(model.getAuthor());
        //load image from internet and set it into imageView using Glide
        Glide.with(context).load(model.getThumbnail()).placeholder(R.drawable.loading_shape).dontAnimate().into(viewHolder.row_thumbnail);

    }


    @Override
    public int getItemCount() {

        return sellingBookArrayList.size();
    }

    public static class SellingBooksViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView row_thumbnail;
        LinearLayout container;
        TextView row_title,row_author,row_price,row_quantity;

        SellingBooksViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            container=mView.findViewById(R.id.container);
            row_thumbnail= mView.findViewById(R.id.row_thumbnail);
            row_title=mView.findViewById(R.id.row_title);
            row_author=mView.findViewById(R.id.row_author);
            row_price=mView.findViewById(R.id.row_price);
            row_quantity=mView.findViewById(R.id.row_quantity);

        }
    }
}
