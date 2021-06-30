package com.example.onlinecollegelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.sl.usermodel.Background;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BookDetails extends AppCompatActivity implements View.OnClickListener/*,DatabaseCallback*/ {
    TextView bookTitle,author,pages,noOfBooks,description,aboutBook,bookAvailability,descTitle;
    ImageView bookCover,close,selectorView1,selectorView2;
    LinearLayout aboutBookBox,availableDetails;
    String isbn;
    Button issueBookBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        isbn = getIntent().getExtras().getString("isbn");
        getBookDescription(isbn);
        bookTitle = findViewById(R.id.book_title_details);
        author = findViewById(R.id.book_author_name_details);
        /*pages = findViewById(R.id.book_page_no_details);
        noOfBooks = findViewById(R.id.no_of_book_details);*/
        pages = findViewById(R.id.tv_page_v);
        noOfBooks = findViewById(R.id.tv_book_v);
        description = findViewById(R.id.book_description_details);
        bookCover = findViewById(R.id.book_detail_cover);
        close = findViewById(R.id.book_detail_close);

        aboutBook = findViewById(R.id.about_book_details);
        bookAvailability = findViewById(R.id.availability_book_details);
        selectorView1 = findViewById(R.id.selector_underline);
        selectorView2 = findViewById(R.id.selector_underline2);

        aboutBookBox = findViewById(R.id.about_book_box);
        descTitle = findViewById(R.id.textView6);
        availableDetails = findViewById(R.id.available_book_list);

        issueBookBtn = findViewById(R.id.issue_book_details);

        bookAvailability.setOnClickListener(this);
        aboutBook.setOnClickListener(this);
        close.setOnClickListener(this);
        issueBookBtn.setOnClickListener(this);
        getBooksDetails(isbn);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.book_details));
    }
    private void getBooksDetails(String isbn){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Books");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Book book = snapshot.child(isbn).getValue(Book.class);
                if(book.getNumber_of_available_books() == 0){
                    issueBookBtn.setEnabled(false);
                }
//                Toast.makeText(getApplicationContext(),""+book.getNoOfBooks(),Toast.LENGTH_SHORT).show();
                updateUi(book);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUi(Book book){

        bookTitle.setText(book.getTitle());
        author.setText(book.getAuthor());
        pages.setText(book.getPage());
        noOfBooks.setText(""+book.getNoOfBooks());
        Glide.with(this)
                .load(book.getCover())
                .placeholder(R.drawable.loadingimage)
                .apply(new RequestOptions().transform(new RoundedCorners(16)))
                .into(bookCover);
    }

    private void getBookDescription(String isbnNumber){
        String Url = "https://openlibrary.org/api/books?bibkeys=ISBN:"+isbnNumber+"&jscmd=details&format=json";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONObject book = jsonObject.getJSONObject("ISBN:"+isbnNumber);
                    JSONObject details = book.getJSONObject("details");
                    String strDescription = details.getString("description");
//                    Toast.makeText(getApplicationContext(),description,Toast.LENGTH_SHORT).show();
                    description.setText(strDescription);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.availability_book_details:
                selectorView1.setVisibility(View.GONE);
                selectorView2.setVisibility(View.VISIBLE);
                bookAvailability.setTextColor(Color.parseColor("#001D1B"));
                aboutBook.setTextColor(Color.parseColor("#9C9A9A"));

                aboutBookBox.setVisibility(View.GONE);
                descTitle.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
                availableDetails.setVisibility(View.VISIBLE);
                break;
            case R.id.about_book_details:
                selectorView1.setVisibility(View.VISIBLE);
                selectorView2.setVisibility(View.GONE);
                aboutBook.setTextColor(Color.parseColor("#001D1B"));
                bookAvailability.setTextColor(Color.parseColor("#9C9A9A"));
                aboutBookBox.setVisibility(View.VISIBLE);
                descTitle.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                availableDetails.setVisibility(View.GONE);
                break;
            case R.id.issue_book_details:
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                dbHelper.getBookDetails(isbn, new BookDetailsCallback() {
                    @Override
                    public void onBookReceived(Book book) {
//                        Toast.makeText(getApplicationContext(),book.getTitle(),Toast.LENGTH_SHORT).show();
                        int available = book.getNumber_of_available_books()-1;
                        if(book.getNumber_of_available_books() > 0){
                            Toast.makeText(getApplicationContext(),"you book is requested",Toast.LENGTH_SHORT).show();
                            new DatabaseHelper(getApplicationContext()).updateNoOfAvailableBooks(isbn,book,available);
                            Date dateObj = Calendar.getInstance().getTime();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                            String date = dateFormat.format(dateObj);
                            new DatabaseHelper(getApplicationContext()).addRequestedBooks(isbn,new SessionManager(getApplicationContext()).getSapId(),date);
                        }else {
                            Toast.makeText(getApplicationContext(),"sorry! Books is not available currently.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.book_detail_close:
                onBackPressed();
                break;
        }
    }

    /*@Override
    public void onDataReceived(User user) {
        Toast.makeText(getApplicationContext(),user.getSapId(),Toast.LENGTH_SHORT).show();
    }*/
}