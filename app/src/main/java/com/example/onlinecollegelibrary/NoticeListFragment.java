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

public class NoticeListFragment extends Fragment {
    RecyclerView recyclerView;

    public NoticeListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_notice_list, container, false);

        recyclerView = view.findViewById(R.id.notice_view);
        FirebaseDatabase.getInstance().getReference(new DatabaseConstant().NOTICE_TABLE_TITLE).addValueEventListener(new ValueEventListener() {
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
        ArrayList<Notice> notices = new ArrayList<>();
        for (DataSnapshot ds : snapshot.getChildren()){
            DatabaseConstant dc = new DatabaseConstant();
            String title = ds.child(dc.NOTICE_TITLE).getValue(String.class);
            String date = ds.child(dc.NOTICE_DATE).getValue(String.class);
            notices.add(new Notice(ds.getKey(),title,date));
        }
        NoticeAdapter adapter = new NoticeAdapter(getActivity(),notices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}