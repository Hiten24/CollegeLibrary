package com.example.onlinecollegelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.SnapshotHolder;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;

public class RequestedBooksAdmin extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<RequestedBook> requestedBooks;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_books_admin);
        recyclerView = findViewById(R.id.Requested_books_view);
        backButton = findViewById(R.id.request_book_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        FirebaseDatabase.getInstance().getReference("Requested Books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.tool_bar));
    }

    private void showData(DataSnapshot snapshot){
        requestedBooks = new ArrayList<>();
        for (DataSnapshot ds : snapshot.getChildren()){
            String userSapId = ds.getKey();
            String isbn = ds.child("Book ISBN").getValue(String.class);
            requestedBooks.add(new RequestedBook(userSapId,isbn));
        }

        RequestedBookAdapter adapter = new RequestedBookAdapter(RequestedBooksAdmin.this,requestedBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}