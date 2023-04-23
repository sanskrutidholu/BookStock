package com.finalyear.bookstock.seller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.finalyear.bookstock.R
import com.finalyear.bookstock.cuctomer.BuyBook
import com.finalyear.bookstock.extras.PreviewBook
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Objects

class BookDescriptionActivity : AppCompatActivity() {

   lateinit var bthumbnail: ImageView
    lateinit var btitle: TextView
    lateinit  var bcategory:TextView
    lateinit  var bprice:TextView
    lateinit  var bauthor:TextView
    lateinit  var bdesc:TextView
    lateinit  var bshow: Button
    lateinit  var buybook:android.widget.Button
    val db = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_description)
        Objects.requireNonNull(supportActionBar)!!.hide()

        bthumbnail = findViewById(R.id.bthumbnail)
        btitle = findViewById(R.id.btitle)
        bcategory = findViewById(R.id.bcategory)
        bprice = findViewById(R.id.bprice)
        bauthor = findViewById(R.id.bauthor)
        bdesc = findViewById(R.id.bdesc)
        bshow = findViewById(R.id.bshow)
        buybook = findViewById(R.id.buybook)

        val intent = intent
        val book_title = intent.getStringExtra("book_title")
        val image = intent.getStringExtra("book_thumbnail")
        val book_id = intent.getStringExtra("book_isbn")
        val preview = intent.getStringExtra("book_preview")
        val book_author = intent.getStringExtra("book_author")
        val book_desc = intent.getStringExtra("book_desc")
        val book_cat = intent.getStringExtra("book_categories")

        bshow.setOnClickListener(View.OnClickListener {
            val i = Intent(this, PreviewBook::class.java)
            i.putExtra("book_preview", preview)
            startActivity(i)
        })

        btitle.text = book_title
        bauthor.text = book_author
        bcategory.text = book_cat
        bdesc.text = book_desc
        Glide.with(this).load(image).placeholder(R.drawable.loading_shape)
            .dontAnimate().into(bthumbnail)

        buybook.setOnClickListener {
            val i = Intent(this, AddBook::class.java)
            i.putExtra("book_title", book_title)
            i.putExtra("book_author", book_author)
            i.putExtra("book_desc", book_desc)
            i.putExtra("book_categories", book_cat)
            i.putExtra("book_isbn", book_id)
            i.putExtra("book_thumbnail", image)
            i.putExtra("book_preview", preview)
            startActivity(i)
        }


    }
}