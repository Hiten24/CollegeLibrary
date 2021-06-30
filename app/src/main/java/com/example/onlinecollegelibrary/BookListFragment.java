package com.example.onlinecollegelibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
interface BookCallback{
    Void onBookItemClick(String isbn);
}
public class BookListFragment extends Fragment implements BookCallback{
    RecyclerView recyclerView;
    List<Book> books;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BookListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookListFragment newInstance(String param1, String param2) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

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
        recyclerView = view.findViewById(R.id.book_list_view);

        /*listView = view.findViewById(R.id.admin_book_manage_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book b = books.get(position);
//                Toast.makeText(getApplicationContext(),b.getIsbn(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),BookDetails.class);
                intent.putExtra("isbn",b.getIsbn());
                startActivity(intent);
            }
        });*/
        return view;
    }
    private void showData(DataSnapshot snapshot){
        books = new ArrayList<>();
//        final ArrayList<Book> books = new ArrayList<>();
        for (DataSnapshot ds : snapshot.getChildren()){
            String bookTitle = ds.child("title").getValue(String.class);
            String bookAuthorName = ds.child("author").getValue(String.class);
            String bookCover = ds.child("cover").getValue(String.class);
            int noOfBooks = ds.child("noOfBooks").getValue(int.class);
            int noOfAvailableBooks = ds.child("number_of_available_books").getValue(int.class);
            String bookIsbn = ds.getKey();

            books.add(new Book(bookTitle,bookAuthorName,bookCover,bookIsbn,noOfBooks,noOfAvailableBooks));
        }
        BookAdapter bookAdapter = new BookAdapter(books,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(bookAdapter);
    }


    @Override
    public Void onBookItemClick(String isbn) {
//        Toast.makeText(getContext(),isbn,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(),BookDetails.class);
        intent.putExtra("isbn",isbn);
        startActivity(intent);
        return null;
    }
}