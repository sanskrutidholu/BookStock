package com.finalyear.bookstock.cuctomer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.finalyear.bookstock.R;import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class RentBook extends AppCompatActivity implements PaymentResultListener {

    public TextView oname,obname,obauthorname,oprice,oitemprice,odeliprice,ototalprice,famount,onumber;
    public EditText oaddress,opin;
    public Button order;
    public ImageView bthumbnail;
    private String uniqueid, fullname, phone;
    FirebaseFirestore db;
    private int total;
    private String userid,bookid,bookauthor,booktitle,image,rprice,genre,mailid,sid,dateTime,sbid;
    private int sprice,dprice,available,newsp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_book);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Checkout.preload(getApplicationContext());

        db = FirebaseFirestore.getInstance();
        uniqueid= UUID.randomUUID().toString();
        Calendar calender= Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(" EEEE, dd-MM-yyyy hh:mm:ss a");
        dateTime = simpleDateFormat.format(calender.getTime());

        oname=findViewById(R.id.oname);
        onumber=findViewById(R.id.onumber);
        oaddress=findViewById(R.id.oaddress);
        obname=findViewById(R.id.obname);
        obauthorname=findViewById(R.id.obauthorname);
        oprice=findViewById(R.id.oprice);
        opin=findViewById(R.id.opin);
        oitemprice=findViewById(R.id.oitemprice);
        odeliprice=findViewById(R.id.odeliprice);
        ototalprice=findViewById(R.id.ototalprice);
        famount=findViewById(R.id.famount);
        order=findViewById(R.id.placeorder);
        bthumbnail=findViewById(R.id.bthumbnail);

        //get the order information
        bookid = getIntent().getStringExtra("book_id");
        sbid= getIntent().getStringExtra("sellerbookid");
        bookauthor = getIntent().getStringExtra("book_author");
        booktitle= getIntent().getStringExtra("book_title");
        image= getIntent().getStringExtra("book_thumbnail");
        genre= getIntent().getStringExtra("book_cat");
        sprice= getIntent().getIntExtra("sp",0);
        dprice= getIntent().getIntExtra("dc",0);
        available = getIntent().getIntExtra("qu",0);

        //set text
        Glide.with(RentBook.this).load(image).placeholder(R.drawable.loading_shape).dontAnimate().into(bthumbnail);
        obname.setText(booktitle);
        obauthorname.setText(bookauthor);
        oprice.setText(String.format("%s ₹", sprice));
        oitemprice.setText(String.format("%s ₹", sprice));
        odeliprice.setText(String.format("%s ₹", dprice));
        total=sprice+dprice;
        ototalprice.setText(String.format("%s ₹", total));
        famount.setText(String.format("₹ %s", total));

        //get all the data
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        userid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        //For user/customer
        DocumentReference docRef = db.collection("Users").document(userid);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                fullname = documentSnapshot.getString("firstname")+" "+documentSnapshot.getString("lastname");
                phone = documentSnapshot.getString("phno");
                String pc = documentSnapshot.getString("pincode");
                String ad = documentSnapshot.getString("address");
                mailid = documentSnapshot.getString("email");
                oname.setText(fullname);
                onumber.setText(phone);
                oaddress.setText(ad);
                opin.setText(pc);
            }
        });

        //For book details
        DocumentReference docRef1 = db.collection("SellingList").document(sbid);
        docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) { sid = documentSnapshot.getString("sellerid"); }
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makePayment();
            }
        });
    }

    private void makePayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_AFKbVtwVh1ZlX5");
        checkout.setImage(R.drawable.logo);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", fullname);
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", total*100);//pass amount in currency subunits
            options.put("prefill.email", mailid);
            options.put("prefill.contact",phone);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        if(opin.getText().toString().trim().length()<6) {
            Toast.makeText(RentBook.this, "Pincode should be of 6 digits", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(oaddress.getText().toString().trim())){
            Toast.makeText(getApplicationContext(),"Please enter your address",Toast.LENGTH_LONG).show();
            return;
        }
        storeToDatabase();
    }

    private void storeToDatabase() {

        Map<String, Object> order = new HashMap<>();
        //unique ids
        order.put("orderid", uniqueid);
        order.put("bookid", bookid);
        order.put("sellerid", sid);
        order.put("customerid", userid);
        //book info can be taken from SellingList
        //customer info
        order.put("address", oaddress.getText().toString().trim());
        order.put("pincode", opin.getText().toString().trim());
        order.put("phno", onumber.getText().toString().trim());
        order.put("email", mailid);
        //order info
        order.put("type", "Rent");
        order.put("price", sprice);
        order.put("deliverycharge", dprice);
        order.put("totalamount", total);
        order.put("orderedat", dateTime);
        order.put("status", "Order placed");
        order.put("accepted", 0);
        order.put("updatedat", dateTime);

        order.put("docverified",0);
        int otp = (int) (Math.random() * 9000) + 1000;
        order.put("otp", String.valueOf(otp));

        db.collection("Orders").document(uniqueid).set(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RentBook.this, "Order placed \uD83C\uDF89", Toast.LENGTH_LONG).show();

                    db.collection("SellingList").document(sbid).update("quantities", FieldValue.increment(-1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RentBook.this, "Payment Done Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RentBook.this, OrderHistoryC.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(RentBook.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}
