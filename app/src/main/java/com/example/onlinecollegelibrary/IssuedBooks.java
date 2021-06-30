package com.example.onlinecollegelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class IssuedBooks extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_books);

        recyclerView = findViewById(R.id.issued_book_list_view);
        FirebaseDatabase.getInstance().getReference(new DatabaseConstant().ISSUED_BOOK).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showData(DataSnapshot snapshot){

        ArrayList<String> issuedBooks = new ArrayList<>();
        for (DataSnapshot ds : snapshot.getChildren()){
            DatabaseConstant dc = new DatabaseConstant();
            String sapId = ds.getKey();
//            issuedBooks.add(sapId);
        }
//        IssuedBooksAdapter adapter = new IssuedBooksAdapter(IssuedBooks.this,issuedBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
    }

    }
        /*ArrayList<IssuedBook> issuedBooks = new ArrayList<>();
        for (DataSnapshot ds : snapshot.getChildren()){
            DatabaseConstant dc = new DatabaseConstant();
            String sapId = ds.getKey();
            String isbn = ds.child(dc.BOOK_ISBN).getValue(String.class);
//            DateAndTime dt = ds.child(dc.BOOK_ISSUED_DATE).getValue(DateAndTime.class);
            Date date = ds.child(dc.BOOK_ISSUED_DATE).getValue(Date.class);
//            issuedBooks.add(new IssuedBook(sapId,isbn,date));
        }
        IssuedBooksAdapter adapter = new IssuedBooksAdapter(IssuedBooks.this,issuedBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }*/
//}