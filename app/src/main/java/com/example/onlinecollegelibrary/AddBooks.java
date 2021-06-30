package com.example.onlinecollegelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddBooks extends AppCompatActivity {
    TextInputLayout inputBookISBN,inputBooksQnt;
    TextInputEditText isbnValue;
    DatabaseReference myRef;
    BottomSheetBehavior bottomSheetBehavior;
    ConstraintLayout constraintLayout;
    View bagroundBlur;
    ImageView backButton;
//    TextView bookTitle,bookPublisher,bookAuthor,bookPageNo,bookSubject,scanActivity;
    TextView scanActivity,addFromExcel;
    ImageView bookCover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);

        inputBookISBN = findViewById(R.id.isbn_number_input);
        inputBooksQnt = findViewById(R.id.book_quantity_input);
        isbnValue = findViewById(R.id.isbn_value);

        myRef = FirebaseDatabase.getInstance().getReference().child("Books");


        constraintLayout = findViewById(R.id.add_book_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);

        isbnValue.addTextChangedListener(textWatcher);

        scanActivity = findViewById(R.id.scan_barcod_button);
        addFromExcel = findViewById(R.id.read_data_from_excel);

        backButton = findViewById(R.id.admin_add_books_back_button);

        addFromExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddBooksFromExcel.class);
                intent.putExtra("data type","books");
                startActivity(intent);
            }
        });
        scanActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),BarcodeScanner.class);
                startActivity(intent);
            }
        });

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d("slide value",""+slideOffset);
                    transitionBottomSheetBackgroundColor(slideOffset);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.tool_bar));

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() == 13){
                Toast.makeText(getApplicationContext(),"Searching Book...",Toast.LENGTH_SHORT).show();
                loadData(s.toString(),true,0);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void transitionBottomSheetBackgroundColor(float slideOffset) {
        int colorFrom = getResources().getColor(R.color.bottom_sheet_bg_alpha0);
        int colorTo = getResources().getColor(R.color.bottom_sheet_bg_alpha60);
        bagroundBlur = findViewById(R.id.background_blur);
        bagroundBlur.setBackgroundColor(interpolateColor(slideOffset,
                colorFrom, colorTo));
    }

    private int interpolateColor(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;
        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;
        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }

    public void loadData(String isbnNumber,Boolean showBottomSheet,int noOfBooks){

        String Url = "https://openlibrary.org/api/books?bibkeys=ISBN:"+isbnNumber+"&jscmd=data&format=json";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response,isbnNumber,showBottomSheet,noOfBooks);
                Toast.makeText(getApplicationContext(),"load data",Toast.LENGTH_SHORT).show();
                Log.d("checking 1","load Data ");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void parseJSON(String data,String isbnNumber,Boolean showBottomSheet,int noOfBooks){
        try {
            Toast.makeText(getApplicationContext(),"parse JSON",Toast.LENGTH_SHORT).show();
            Log.d("checking 2","Parse Data ");
            JSONObject jsonObject = new JSONObject(data);
            JSONObject book = jsonObject.getJSONObject("ISBN:"+isbnNumber);
            String bookTitle = book.getString("title");
            String bookPages = String.valueOf(book.getInt("number_of_pages"));

            JSONArray arrayAuthor = book.getJSONArray("authors");
            JSONObject auther1 = arrayAuthor.getJSONObject(0);
            String bookAuther = auther1.getString("name");

            JSONArray arrSub = book.getJSONArray("subjects");
            JSONObject sub1 = arrSub.getJSONObject(0);
            String bookSubject = sub1.getString("name");

            JSONArray arrPublishers = book.getJSONArray("publishers");
            JSONObject pub1 = arrPublishers.getJSONObject(0);
            String bookPublisher = pub1.getString("name");

            JSONObject objCover = book.getJSONObject("cover");
            String bookCover = objCover.getString("medium");

            if(showBottomSheet){
                updateBottomSheet(bookTitle,bookPublisher,bookAuther,bookPages,bookSubject,bookCover,isbnNumber);
                Toast.makeText(getApplicationContext(),bookTitle,Toast.LENGTH_SHORT).show();
            }else {
                addBookToDatabase(bookTitle,bookPages,bookAuther,bookPublisher,bookSubject,bookCover,isbnNumber,noOfBooks);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateBottomSheet(String strTitle,String strPublisher,String strAuthor,String strPages,String strSubject,String strCover,String strISBN){
        Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
        TextView bookTitle = findViewById(R.id.book_title_bs);
        TextView bookPublisher = findViewById(R.id.book_publisher_value);
        TextView bookAuthor = findViewById(R.id.book_author_value);
        TextView bookPageNo = findViewById(R.id.book_page_number_value);
        TextView bookSubject = findViewById(R.id.book_subject_value);
        ImageView bookCover = findViewById(R.id.book_cover);

        Glide.with(this)
                .load(strCover)
                .placeholder(R.drawable.loadingimage)
                .into(bookCover);

        bookTitle.setText(strTitle);
        bookPublisher.setText(strPublisher);
        bookAuthor.setText(strAuthor);
        bookPageNo.setText(strPages);
        bookSubject.setText(strSubject);

        ConstraintLayout constraintLayout = findViewById(R.id.add_book_bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        Button addBookToDB = findViewById(R.id.add_book_btn_bottomsheet);

        TextInputLayout bookQuntity = findViewById(R.id.book_quantity_input);
        addBookToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int noOfBooks = Integer.parseInt(bookQuntity.getEditText().getText().toString().trim());
                addBookToDatabase(strTitle,strPages,strAuthor,strPublisher,strSubject,strCover,strISBN,noOfBooks);
            }
        });
    }

    private void addBookToDatabase(String title,String pages,String auther,String publisher,String subject,String cover,String isbn,int noOfBooks){
        Book book = new Book(title,pages,auther,publisher,subject,cover,noOfBooks,noOfBooks);
        myRef.child(isbn).setValue(book);
    }
}