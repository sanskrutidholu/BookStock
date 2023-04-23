package com.finalyear.bookstock.seller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyear.bookstock.R;
import com.finalyear.bookstock.extras.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class OrderReceivedAdapter extends RecyclerView.Adapter<OrderReceivedAdapter.UnacceptedsViewHolder> {

    Context context;
    ArrayList<Order> orderArrayList;
    public String dateTime;
    private FirebaseFirestore firebaseFirestore;

    public OrderReceivedAdapter(Context context,ArrayList<Order> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public OrderReceivedAdapter.UnacceptedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_approve_list, parent, false);

        return new UnacceptedsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderReceivedAdapter.UnacceptedsViewHolder viewHolder, int position) {
        Order model = orderArrayList.get(position);
        String orderid = model.getOrderid();
        String address = model.getAddress();

        viewHolder.row_orderedon.setText(model.getUpdatedat());
        viewHolder.row_add.setText(address);

        Calendar calender= Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(" EEEE, dd-MM-yyyy hh:mm:ss a");
        dateTime = simpleDateFormat.format(calender.getTime());

        //For book details
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("SellingList").whereEqualTo("bookid",model.getBookid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                       String bookname = document.getString("title");
                       viewHolder.row_bookname.setText(bookname);
                    }
                }
            }
        });


        viewHolder.acc_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Orders").document(orderid).update("updatedon",dateTime,"accepted",1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(context, "Order accepted!", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        viewHolder.dec_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Orders").document(orderid).update("updatedon",dateTime,"accepted",2).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(context, "Order declined!", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class UnacceptedsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        LinearLayout container; //row_approve_list
        TextView row_bookname,row_orderedon,row_add;
        Button acc_order,dec_order;

        UnacceptedsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            container=mView.findViewById(R.id.container);
            row_bookname=mView.findViewById(R.id.row_bookname);
            row_orderedon=mView.findViewById(R.id.row_orderedon);
            row_add=mView.findViewById(R.id.row_add);
            acc_order=mView.findViewById(R.id.acc_order);
            dec_order=mView.findViewById(R.id.dec_order);
        }
    }

}
