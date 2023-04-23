package com.finalyear.bookstock.seller;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyear.bookstock.R;
import com.finalyear.bookstock.extras.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.UnacceptedsViewHolder> {

    Context context;
    ArrayList<Order> orderArrayList;
    String bookname;
    private FirebaseFirestore firebaseFirestore;

    public OrderHistoryAdapter(Context context,ArrayList<Order> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.UnacceptedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_filter_s_list, parent, false);

        return new UnacceptedsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.UnacceptedsViewHolder viewHolder, int position) {
        Order model = orderArrayList.get(position);
        String address = model.getAddress();
        String updatedate = model.getUpdatedat();
        int accepted = model.getAccepted();

        if (accepted == 1){
            viewHolder.row_ostatus.getResources().getColor(R.color.green);
            viewHolder.row_ostatus.setText("Accepted");
        } else if (accepted == 2) {
            viewHolder.row_ostatus.setTextColor(Color.RED);
            viewHolder.row_ostatus.setText("Rejected");
        }


        //For book details
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("SellingList").whereEqualTo("bookid",model.getBookid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        String bookname = document.getString("title");
                        viewHolder.row_bb.setText(bookname);
                    }
                }
            }
        });

        viewHolder.row_add.setText(address);
        viewHolder.row_orderupdate.setText(updatedate);
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class UnacceptedsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        LinearLayout container; //row_filter_s_list
        TextView row_ostatus,row_bb,row_orderupdate,row_add;
        Button row_details;

        UnacceptedsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            container=mView.findViewById(R.id.container);

            row_ostatus=mView.findViewById(R.id.row_ostatus);
            row_bb=mView.findViewById(R.id.row_bb);
            row_orderupdate=mView.findViewById(R.id.row_orderupdate);
            row_add=mView.findViewById(R.id.row_add);
//            row_details=mView.findViewById(R.id.row_details);
        }
    }

}
