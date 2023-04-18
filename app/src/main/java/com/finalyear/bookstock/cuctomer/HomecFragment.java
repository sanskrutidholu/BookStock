package com.finalyear.bookstock.cuctomer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.finalyear.bookstock.R;
import com.finalyear.bookstock.cuctomer.AvailableBooksAdapter;
import com.finalyear.bookstock.cuctomer.BookInfoOrder;
import com.finalyear.bookstock.cuctomer.SearchBooksActivity;
import com.finalyear.bookstock.extras.SellingBook;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomecFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private ArrayList<SellingBook> sellingBookArrayList;
    private AvailableBooksAdapter availableBooksAdapter;
    RecyclerView recyler_home_page_books;
    LinearLayout ll_dataFound, ll_nodataFound;
    TextView tvExplore;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_homec, container, false);

        recyler_home_page_books = root.findViewById(R.id.recyler_home_page_books);
        ll_dataFound = root.findViewById(R.id.ll_available);
        ll_nodataFound = root.findViewById(R.id.ll_not_available);
        tvExplore = root.findViewById(R.id.tv_explore);
        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh);


        firebaseFirestore=FirebaseFirestore.getInstance();

        sellingBookArrayList = new ArrayList<>();
        recyler_home_page_books.setHasFixedSize(true);
        recyler_home_page_books.setLayoutManager(new LinearLayoutManager(getActivity()));
        availableBooksAdapter = new AvailableBooksAdapter(getContext(), sellingBookArrayList);
        recyler_home_page_books.setAdapter(availableBooksAdapter);

        tvExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), SearchBooksActivity.class);
                startActivity(i);
            }
        });

        showNewAvailables();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showNewAvailables();
                pullToRefresh.setRefreshing(false);
            }
        });
        return root;

    }

    private void showNewAvailables() {
        firebaseFirestore.collection("SellingList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    ll_nodataFound.setVisibility(View.GONE);
                    ll_dataFound.setVisibility(View.VISIBLE);
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        SellingBook c = d.toObject(SellingBook.class);
                        sellingBookArrayList.add(c);
                    }
                } else {
                    ll_nodataFound.setVisibility(View.VISIBLE);
                    ll_dataFound.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ll_nodataFound.setVisibility(View.VISIBLE);
                ll_dataFound.setVisibility(View.GONE);
            }
        });
    }

}
