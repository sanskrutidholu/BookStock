package com.finalyear.bookstock.seller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalyear.bookstock.R;
import com.finalyear.bookstock.cuctomer.AvailableBooksAdapter;
import com.finalyear.bookstock.extras.SellingBook;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BooklistFragment extends Fragment {

    private ArrayList<SellingBook> sellingBookArrayList;
    private SellerBookListAdapter availableBooksAdapter;
    LinearLayout ll_dataFound, ll_nodataFound;
    AppCompatButton btnAddBooks;

    private RecyclerView recyclerView;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_booklist, container, false);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        ll_dataFound = root.findViewById(R.id.ll_available);
        ll_nodataFound = root.findViewById(R.id.ll_not_available);
        btnAddBooks = root.findViewById(R.id.btn_addBooks);
        recyclerView=root.findViewById(R.id.seller_book_list);

        sellingBookArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        availableBooksAdapter = new SellerBookListAdapter(getContext(), sellingBookArrayList);
        recyclerView.setAdapter(availableBooksAdapter);


        LoadList(userid);
        return root;
    }

    private void LoadList(String userid) {
        db.collection("SellingList").whereEqualTo("sellerid", userid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
