package com.example.collegelibrary;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddBooks extends AppCompatActivity {
    TextInputLayout inputBookISBN,inputBooksQnt;
    Button addBookBtn;
    TextView tmp;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);
        inputBookISBN = findViewById(R.id.isbn_number_input);
        inputBooksQnt = findViewById(R.id.book_quantity_input);
        addBookBtn = findViewById(R.id.add_book_btn);
        tmp = findViewById(R.id.tmptitle);

        myRef = FirebaseDatabase.getInstance().getReference().child("Books");

        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddBooks.this,"You clicked btn",Toast.LENGTH_SHORT).show();
                String isbnNumber = inputBookISBN.getEditText().getText().toString().trim();
                if(TextUtils.isEmpty(isbnNumber) || isbnNumber.length() <13){
                    inputBookISBN.setError("Enter Valid ISBN Number");
                }else {
                    String stringUrl = "https://openlibrary.org/api/books?bibkeys=ISBN:"+isbnNumber+"&jscmd=data&format=json";
                    fetchDataFromApi(stringUrl,isbnNumber);
                }
            }
        });
    }
    private void fetchDataFromApi(String strUrl,String isbnNumber){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, strUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(),"I am Trying",Toast.LENGTH_SHORT).show();
                    JSONObject book = response.getJSONObject("ISBN:"+isbnNumber);
                    String strTitle = book.getString("title");
                    String pages = String.valueOf(book.getInt("number_of_pages"));
                    JSONArray arrayAuthor = book.getJSONArray("authors");
                    JSONObject auther1 = arrayAuthor.getJSONObject(0);
                    String sAuther = auther1.getString("name");

                    JSONArray arrSub = book.getJSONArray("subjects");
                    JSONObject sub1 = arrSub.getJSONObject(0);
                    String sSubject = sub1.getString("name");

                    JSONArray arrPublishers = book.getJSONArray("publishers");
                    JSONObject pub1 = arrPublishers.getJSONObject(0);
                    String spublisher = pub1.getString("name");

                    JSONObject objCover = book.getJSONObject("cover");
                    String sCover = objCover.getString("medium");
                    /*title.setText(jtitle);
                    noofpage.setText(pages);
                    auther.setText(sAuther);
                    publisher.setText(spublisher);
                    sub.setText(sSubject);
                    cvr.setText(sCover);*/
                    addBookToDatabase(strTitle,pages,sAuther,spublisher,sSubject,sCover,isbnNumber);
                    Toast.makeText(getApplicationContext(),strTitle,Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getApplicationContext(),title,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error "+error,Toast.LENGTH_SHORT).show();
                Log.d("myapp","Error "+error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    private void addBookToDatabase(String title,String pages,String auther,String publisher,String subject,String cover,String isbn){
        Book book = new Book(title,pages,auther,publisher,subject,cover,isbn);
        myRef.child(isbn).setValue(book);
    }
}