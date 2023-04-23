package com.finalyear.bookstock.seller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.finalyear.bookstock.R;
import com.finalyear.bookstock.cuctomer.AvailableBooksAdapter;
import com.finalyear.bookstock.extras.Order;
import com.finalyear.bookstock.extras.SellingBook;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class OrderReceivedFragment extends Fragment {


    private FirebaseFirestore firebaseFirestore;
    private ArrayList<Order> orderArrayList;
    private OrderReceivedAdapter orderReceivedAdapter;
    private RecyclerView recyler_approve_s;
    public String dateTime;
    LinearLayout ll_dataFound, ll_nodata;
    private Query query;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_order_received, container, false);

        Intent intent= requireActivity().getIntent();
        final String userid =intent.getStringExtra("user_id");

        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefreshApprove);
        recyler_approve_s = root.findViewById(R.id.recyler_approve_s);
        ll_nodata = root.findViewById(R.id.ll_nodata);
        ll_dataFound = root.findViewById(R.id.ll_dataFound);

        firebaseFirestore=FirebaseFirestore.getInstance();
        Calendar calender= Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(" EEEE, dd-MM-yyyy hh:mm:ss a");
        dateTime = simpleDateFormat.format(calender.getTime());





//        firebaseFirestore.collection("Orders").whereEqualTo("sellerid",userid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @SuppressLint("DefaultLocale")
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                long sum=0;
//                int count=0;
//                long accepted_count=0;
//                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
//                    if(documentSnapshot.getLong("price")!=null) {
//                        long sp = documentSnapshot.getLong("price");
//                        sum = sum + sp;
//                    }
//                    count++;
//                    if((documentSnapshot.getLong("accepted"))==0)
//                    {
//                        long sp = documentSnapshot.getLong("price");
//                        accepted_count = accepted_count + sp;
//                    }
//                }
//
//            }
//        });

        orderArrayList = new ArrayList<>();
        recyler_approve_s.setHasFixedSize(true);
        recyler_approve_s.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderReceivedAdapter = new OrderReceivedAdapter(getContext(),orderArrayList);
        recyler_approve_s.setAdapter(orderReceivedAdapter);

        showUnacceptedOrders(userid);

        //End of adapter code

        pullToRefresh.setOnRefreshListener(() -> {
            showUnacceptedOrders(userid);
            pullToRefresh.setRefreshing(false);
        });

        return root;
    }


    private void showUnacceptedOrders(String userid) {
        firebaseFirestore.collection("Orders").whereEqualTo("sellerid",userid).whereEqualTo("accepted",0).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    ll_nodata.setVisibility(View.GONE);
                    ll_dataFound.setVisibility(View.VISIBLE);
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        Order c = d.toObject(Order.class);
                        orderArrayList.add(c);
                    }
                } else {
                    ll_nodata.setVisibility(View.VISIBLE);
                    ll_dataFound.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(e -> {
            ll_nodata.setVisibility(View.VISIBLE);
            ll_dataFound.setVisibility(View.GONE);
        });
    }

}
