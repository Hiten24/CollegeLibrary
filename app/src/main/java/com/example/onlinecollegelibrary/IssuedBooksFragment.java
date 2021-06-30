package com.example.onlinecollegelibrary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class IssuedBooksFragment extends Fragment {
    RecyclerView recyclerView;

    public IssuedBooksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_issued_books, container, false);

        recyclerView = view.findViewById(R.id.issued_book_list_view);
        FirebaseDatabase.getInstance().getReference(new DatabaseConstant().ISSUED_BOOK).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
    private void showData(DataSnapshot snapshot){
        ArrayList<IssuedBook> issuedBooks = new ArrayList<>();
        for (DataSnapshot ds : snapshot.getChildren()){
            DatabaseConstant dc = new DatabaseConstant();
            String sapId = ds.getKey();
            String isbn = ds.child(dc.BOOK_ISBN).getValue(String.class);
            DateAndTime issueDate = ds.child(dc.BOOK_ISSUED_DATE).getValue(DateAndTime.class);
            DateAndTime expDate = ds.child(dc.BOOK_EXPIRE_DATE).getValue(DateAndTime.class);
//            int date = ds.child(dc.BOOK_ISBN).child(dc.BOOK_ISSUED_DATE).child("date")

            issuedBooks.add(new IssuedBook(sapId,isbn,issueDate,expDate));
        }
        IssuedBooksAdapter adapter = new IssuedBooksAdapter(getActivity(),issuedBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}