package com.finalyear.bookstock.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.finalyear.bookstock.R;
import java.util.Objects;

public class MainScreenActivity extends AppCompatActivity {

    RelativeLayout seller, customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        seller = findViewById(R.id.rl_seller);
        customer = findViewById(R.id.rl_customer);

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainScreenActivity.this, CreatAccountSeller.class);
                startActivity(i);
                finish();
            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainScreenActivity.this, CreateAccountCustomer.class);
                startActivity(i);
                finish();
            }
        });


    }
}