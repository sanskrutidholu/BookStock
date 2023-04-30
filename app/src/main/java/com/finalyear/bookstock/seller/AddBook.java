package com.finalyear.bookstock.seller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.finalyear.bookstock.R;
import com.finalyear.bookstock.common.HomeSeller;
import com.finalyear.bookstock.extras.PreviewBook;
import com.finalyear.bookstock.extras.SellingBook;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Objects;
import java.util.UUID;

public class AddBook extends AppCompatActivity implements PaymentResultListener {

    TextView title, author;
    ImageView thumbnail;
    Button previewBtn;
    private EditText uquantity,uprice,urprice,udprice;
    private Button updateBtn;
    private FirebaseAuth fAuth;
    private String userid;
    final String uniqueid= UUID.randomUUID().toString();
    String up1,urp1,udp1,uq1, book_id,book_title,book_author,book_desc,book_cat,image,preview;

    int uq, up, urp, udp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        Checkout.preload(getApplicationContext());

        final Intent intent=getIntent();
       book_title =intent.getStringExtra("book_title");
       image =intent.getStringExtra("book_thumbnail");
        book_id =intent.getStringExtra("book_isbn");
        preview = intent.getStringExtra("book_preview");
        book_author = intent.getStringExtra("book_author");
        book_desc = intent.getStringExtra("book_desc");
        book_cat = intent.getStringExtra("book_categories");

        title=findViewById(R.id.title);
        author=findViewById(R.id.author);
        uquantity=findViewById(R.id.uquantity);
        uprice=findViewById(R.id.uprice);
        urprice=findViewById(R.id.urprice);
        udprice=findViewById(R.id.udprice);
        updateBtn=findViewById(R.id.updateBtn);
        previewBtn=findViewById(R.id.previewBtn);
        thumbnail=findViewById(R.id.thumbnail);

        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(preview);
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent1);
            }
        });


        title.setText(book_title);
        author.setText(book_author);
        Glide.with(AddBook.this).load(image).placeholder(R.drawable.loading_shape).dontAnimate().into(thumbnail);

        updateBtn.setOnClickListener(v -> makepayment());

    }

    private void makepayment() {
        uq1=uquantity.getText().toString().trim();
        up1=uprice.getText().toString().trim();
        urp1=urprice.getText().toString().trim();
        udp1=udprice.getText().toString().trim();
        uq=Integer.parseInt(uq1);
        up=Integer.parseInt(up1);
        urp=Integer.parseInt(urp1);
        udp=Integer.parseInt(udp1);

        if(TextUtils.isEmpty(uq1)){
            Toast.makeText(getApplicationContext(),"Please enter the quantity",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(up1)){
            Toast.makeText(getApplicationContext(),"Please enter the selling price",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(urp1)){
            Toast.makeText(getApplicationContext(),"Please enter the renting price",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(udp1)){
            Toast.makeText(getApplicationContext(),"Please enter the delivery charges",Toast.LENGTH_SHORT).show();
            return;
        }else{
            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_AFKbVtwVh1ZlX5");
            checkout.setImage(R.drawable.logo);
            final Activity activity = this;

            try {
                JSONObject options = new JSONObject();

//            options.put("name", fullname);
                options.put("description", "Reference No. #123456");
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
                options.put("theme.color", "#3399cc");
                options.put("currency", "INR");
                options.put("amount", up*100);//pass amount in currency subunits
//                options.put("prefill.email", mailid);
//                options.put("prefill.contact",phone);
                JSONObject retryObj = new JSONObject();
                retryObj.put("enabled", true);
                retryObj.put("max_count", 4);
                options.put("retry", retryObj);

                checkout.open(activity, options);

            } catch(Exception e) {
                Log.e("TAG", "Error in starting Razorpay Checkout", e);
            }
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        fAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        userid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        updateBtn.setText("Adding Your Book...");

        SellingBook sellingBook=new SellingBook(book_id,uniqueid,userid,uq,up,urp,udp,book_title,book_author,book_desc,book_cat,image,preview);

        db.collection("SellingList").document(uniqueid).set(sellingBook).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateBtn.setText("Add book to selling list");
                if(task.isSuccessful()) {
                    Toast.makeText(AddBook.this,"Book added successfully", Toast.LENGTH_LONG).show();
                    Intent i =new Intent(AddBook.this, HomeSeller.class);
                    startActivity(i);
                    finish();
                } else{
                    String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(AddBook.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onPaymentError(int i, String s) {

    }


}
