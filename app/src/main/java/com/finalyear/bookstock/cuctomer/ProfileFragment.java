package com.finalyear.bookstock.cuctomer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import com.finalyear.bookstock.R;
import com.finalyear.bookstock.common.CustomerLogin;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private LinearLayout linearl2;
    private TextView profileName, tvemail;
    AppCompatButton btnEdit;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = root.findViewById(R.id.profileName);
        tvemail = root.findViewById(R.id.email);
        btnEdit = root.findViewById(R.id.btnEditProfile);
        LinearLayout linearl3 = root.findViewById(R.id.linearl3);
        LinearLayout linearl5 = root.findViewById(R.id.linearl5);
        LinearLayout linearl6 = root.findViewById(R.id.linearl6);
        LinearLayout linearl7 = root.findViewById(R.id.linearl7);
        LinearLayout linearl8 = root.findViewById(R.id.linearl8);
        AppCompatButton logout = root.findViewById(R.id.logout);

        //Checked signed
        //Firebase
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        DocumentReference typeref = db.collection("Users").document(userid);
        typeref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String fullName = documentSnapshot.getString("firstname")+" "+documentSnapshot.getString("lastname");
                    String email = documentSnapshot.getString("email");
                    profileName.setText(fullName);
                    tvemail.setText(email);
                }
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //My profile
                Intent i = new Intent(getActivity(), ProfileCustomer.class);
                startActivity(i);
            }
        });

        linearl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Order history
                Intent i = new Intent(getActivity(), OrderHistoryC.class);
                startActivity(i);
            }
        });

        linearl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Currently Not Available", Toast.LENGTH_SHORT).show();
            }
        });

        linearl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Currently Not Available", Toast.LENGTH_SHORT).show();

            }
        });

        linearl7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Currently Not Available", Toast.LENGTH_SHORT).show();

            }
        });

        linearl8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Currently Not Available", Toast.LENGTH_SHORT).show();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log out
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getActivity(), CustomerLogin.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        return root;
    }

}
