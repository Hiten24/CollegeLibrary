package com.example.onlinecollegelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminManageBooks extends AppCompatActivity {
    RecyclerView listView;
    ArrayList<Book> books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_books);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Books");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myRef.addValueEventListener(valueEventListener);
//        listView = findViewById(R.id.)
        /*listView = findViewById(R.id.admin_book_manage_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book b = books.get(position);
//                Toast.makeText(getApplicationContext(),b.getIsbn(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminManageBooks.this,BookDetails.class);
                intent.putExtra("isbn",b.getIsbn());
                startActivity(intent);
            }
        });*/
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    private void showData(DataSnapshot snapshot){
        books = new ArrayList<>();
//        final ArrayList<Book> books = new ArrayList<>();
        for (DataSnapshot ds : snapshot.getChildren()){
            String bookTitle = ds.child("title").getValue(String.class);
            String bookAuthorName = ds.child("author").getValue(String.class);
            String bookCover = ds.child("cover").getValue(String.class);
            String bookIsbn = ds.getKey();

            books.add(new Book(bookTitle,bookAuthorName,bookCover,bookIsbn,4,4));
        }
        /*BookAdapter bookAdapter = new BookAdapter(AdminManageBooks.this,books);
        listView.setAdapter(bookAdapter);*/
    }
}