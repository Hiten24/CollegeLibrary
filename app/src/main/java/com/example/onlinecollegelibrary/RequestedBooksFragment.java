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

public class RequestedBooksFragment extends Fragment {
    private RecyclerView recyclerView;

    public RequestedBooksFragment() {
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
        View view = inflater.inflate(R.layout.fragment_requested_books, container, false);

        recyclerView = view.findViewById(R.id.Requested_books_view);

        FirebaseDatabase.getInstance().getReference("Requested Books").addValueEventListener(new ValueEventListener() {
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
        ArrayList<RequestedBook> requestedBooks = new ArrayList<>();
        for (DataSnapshot ds : snapshot.getChildren()){
            String userSapId = ds.getKey();
            String isbn = ds.child("Book ISBN").getValue(String.class);
            requestedBooks.add(new RequestedBook(userSapId,isbn));
        }

        RequestedBookAdapter adapter = new RequestedBookAdapter(getActivity(),requestedBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}