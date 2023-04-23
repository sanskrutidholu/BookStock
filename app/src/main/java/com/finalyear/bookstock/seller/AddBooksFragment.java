package com.finalyear.bookstock.seller;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.finalyear.bookstock.R;
import com.finalyear.bookstock.extras.ABRecyclerViewAdapter;
import com.finalyear.bookstock.extras.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddBooksFragment extends Fragment {

    LinearLayout ll_dataFound, ll_noDataFound;
    private EditText search_book;
    private RecyclerView result_list;
    private TextView error_message;
    private ArrayList<Book> mBooks;
    private ABRecyclerViewAdapter mAdapter;
    private RequestQueue mRequestQueue;
    String user;
    ImageView search_book_btn;
    private static  final  String BASE_URL="https://www.googleapis.com/books/v1/volumes?q=";

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_books, container, false);

        ll_dataFound = root.findViewById(R.id.ll_available);
        ll_noDataFound = root.findViewById(R.id.ll_not_available);
        search_book_btn = root.findViewById(R.id.iv_search);
        search_book=root.findViewById(R.id.et_search);
        result_list=root.findViewById(R.id.recyler_search);
        mBooks = new ArrayList<>();
        result_list.setHasFixedSize(true);
        result_list.setLayoutManager(new GridLayoutManager(getActivity(),2));

        mBooks = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(requireActivity());

        search_book_btn.setOnClickListener(v -> {
            String name = search_book.getText().toString();
            if (name.equals(" ")){
                ll_noDataFound.setVisibility(View.VISIBLE);
                ll_dataFound.setVisibility(View.GONE);
                Toast.makeText(getActivity(),"Please enter boook name",Toast.LENGTH_SHORT).show();
            }else{
                ll_noDataFound.setVisibility(View.GONE);
                ll_dataFound.setVisibility(View.VISIBLE);
                mBooks.clear();
                search(name);
            }

        });

        return root;
    }


    private void search(String name) {
        Toast.makeText(getActivity(), " Searching books..", Toast.LENGTH_SHORT).show();
        String final_query=name.replace(" ","+");
        Uri uri=Uri.parse(BASE_URL+final_query);
        Uri.Builder buider = uri.buildUpon();
        parseJson(buider.toString());
    }

    private void parseJson(String key) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, key.toString(), null,
                response -> {
                    Log.e("","response " + response);
                    String title ="";
                    String author ="";
                    String isbn= "empty";
                    String publishedDate = "NoT Available";
                    String description = "No Description";
                    int pageCount = 1000;
                    String categories = "Non categorized ";
                    String buy ="";

                    String price = "000 INR";
                    try {
                        JSONArray items = response.getJSONArray("items");

                        for (int i = 0 ; i< items.length() ;i++){
                            JSONObject item = items.getJSONObject(i);
                            JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                            try{
                                title = volumeInfo.getString("title");

                                JSONArray authors = volumeInfo.getJSONArray("authors");
                                if(authors.length() == 1){
                                    author = authors.getString(0);
                                }else {
                                    author = authors.getString(0) + "|" +authors.getString(1);
                                }

                                publishedDate = volumeInfo.getString("publishedDate");
                                pageCount = volumeInfo.getInt("pageCount");
                                JSONObject saleInfo = item.getJSONObject("saleInfo");
                                JSONObject listPrice = saleInfo.getJSONObject("listPrice");
                                price = listPrice.getString("amount") + " " +listPrice.getString("currencyCode");
                                description = volumeInfo.getString("description");
                                buy = saleInfo.getString("buyLink");
                                isbn = item.getString("id");
                                categories = volumeInfo.getJSONArray("categories").getString(0);

                            }catch (Exception e){
                                Log.d("TAG", e.toString());
                            }



                            String thumbnail = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                            String previewLink = volumeInfo.getString("previewLink");
                            String url = volumeInfo.getString("infoLink");

                            mBooks.add(new Book(title , author , publishedDate , description ,categories
                                    ,thumbnail,buy,previewLink,price,pageCount,url,isbn));

                            mAdapter = new ABRecyclerViewAdapter(getActivity() , mBooks);
                            result_list.setAdapter(mAdapter);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TAG" , e.toString());

                    }

                }, error -> error.printStackTrace());
        mRequestQueue.add(request);
    }
}

