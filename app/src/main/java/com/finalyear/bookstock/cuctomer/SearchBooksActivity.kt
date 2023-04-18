package com.finalyear.bookstock.cuctomer

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.finalyear.bookstock.R
import com.finalyear.bookstock.extras.Book
import com.finalyear.bookstock.extras.SBRecyclerViewAdapter
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONException
import java.util.Objects

class SearchBooksActivity : AppCompatActivity() {

    lateinit var ll_dataFound : LinearLayout
    lateinit var ll_nodataFound : LinearLayout
    lateinit var et_search: EditText
    lateinit var search: ImageView
    lateinit var recyclerView : RecyclerView
    lateinit var adapter: SBRecyclerViewAdapter

    lateinit var bookList: ArrayList<Book>
    lateinit var firebaseFirestore: FirebaseFirestore
    private val BASE_URL = "https://www.googleapis.com/books/v1/volumes?q="

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_books)
        Objects.requireNonNull(supportActionBar)!!.hide()

        firebaseFirestore = FirebaseFirestore.getInstance()
        ll_dataFound = findViewById(R.id.ll_available)
        ll_nodataFound = findViewById(R.id.ll_not_available)
        et_search = findViewById(R.id.et_search)
        search = findViewById(R.id.iv_search)
        recyclerView = findViewById(R.id.recyler_search)
        bookList = ArrayList()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this,2)

        search.setOnClickListener {
            val name = et_search.text.toString()
            if (name == ""){
                ll_nodataFound.visibility= View.VISIBLE
                ll_dataFound.visibility= View.GONE

                Toast.makeText(this,"Please enter boook name",Toast.LENGTH_SHORT).show()
            }else{
                ll_nodataFound.visibility= View.GONE
                ll_dataFound.visibility= View.VISIBLE
                showBook(name)
            }
        }

    }

    private fun showBook(search_query: String) {
        Toast.makeText(this, " Searching books..", Toast.LENGTH_SHORT).show()
        val final_query = search_query.replace(" ", "+")
        val uri = Uri.parse(BASE_URL + final_query)
        val buider = uri.buildUpon()
        parseJson(buider.toString())
    }

    private fun parseJson(key: String) {
        val request = JsonObjectRequest(
            Request.Method.GET, key, null,
            { response ->
                var title = ""
                var author = ""
                var isbn = "empty"
                var publishedDate = "NoT Available"
                var description = "No Description"
                var pageCount = 1000
                var categories = "Non categorized "
                var buy = ""
                var price = "not given INR"
                try {
                    val items = response.getJSONArray("items")
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val volumeInfo = item.getJSONObject("volumeInfo")
                        try {
                            title = volumeInfo.getString("title")
                            val authors = volumeInfo.getJSONArray("authors")
                            author = if (authors.length() == 1) {
                                authors.getString(0)
                            } else {
                                authors.getString(0) + "|" + authors.getString(1)
                            }
                            publishedDate = volumeInfo.getString("publishedDate")
                            pageCount = volumeInfo.getInt("pageCount")
                            val saleInfo = item.getJSONObject("saleInfo")
                            val listPrice = saleInfo.getJSONObject("listPrice")
                            price =
                                listPrice.getString("amount") + " " + listPrice.getString("currencyCode")
                            description = volumeInfo.getString("description")
                            buy = saleInfo.getString("buyLink")
                            isbn = item.getString("id")
                            categories = volumeInfo.getJSONArray("categories").getString(0)
                        } catch (e: Exception) {
                            Log.d("TAG", e.toString())
                        }
                        val thumbnail =
                            volumeInfo.getJSONObject("imageLinks").getString("thumbnail")
                        val previewLink = volumeInfo.getString("previewLink")
                        val url = volumeInfo.getString("infoLink")
                        bookList.add(Book(title, author, publishedDate, description, categories, thumbnail,
                            buy, previewLink, price, pageCount, url, isbn))
                        adapter = SBRecyclerViewAdapter(this, bookList)
                        recyclerView.adapter = adapter
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.e("TAG", e.toString())
                }
            }) { error -> error.printStackTrace() }
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }
}