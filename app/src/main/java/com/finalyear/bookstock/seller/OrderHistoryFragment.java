package com.finalyear.bookstock.seller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.finalyear.bookstock.R;
import com.finalyear.bookstock.extras.Order;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderHistoryFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private ArrayList<Order> orderArrayList;
    private RecyclerView recyler_order_filter_s;
    private OrderHistoryAdapter adapter;
    LinearLayout ll_dataFound, ll_nodata;
    private String filter,bookname;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order_history, container, false);

        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefreshfilter);
        recyler_order_filter_s = root.findViewById(R.id.recyler_order_filter_s);
        ll_nodata = root.findViewById(R.id.ll_nodata);
        ll_dataFound = root.findViewById(R.id.ll_dataFound);

        firebaseFirestore=FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String current_user_id = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        //spinner

        orderArrayList = new ArrayList<>();
        recyler_order_filter_s.setHasFixedSize(true);
        recyler_order_filter_s.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new OrderHistoryAdapter(getContext(),orderArrayList);
        recyler_order_filter_s.setAdapter(adapter);
        //End of adapter code

        showcurrentorder(current_user_id);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showcurrentorder(current_user_id);
                pullToRefresh.setRefreshing(false);
            }
        });
        return root;
    }

    private void showcurrentorder(String userid)
    {
        firebaseFirestore.collection("Orders").whereEqualTo("sellerid",userid).get().addOnSuccessListener(queryDocumentSnapshots -> {
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
        }).addOnFailureListener(e -> {
            ll_nodata.setVisibility(View.VISIBLE);
            ll_dataFound.setVisibility(View.GONE);
        });

    }
}
