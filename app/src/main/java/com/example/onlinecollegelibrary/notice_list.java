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

public class notice_list extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);

        recyclerView = findViewById(R.id.notice_view);
        FirebaseDatabase.getInstance().getReference(new DatabaseConstant().NOTICE_TABLE_TITLE).addValueEventListener(new ValueEventListener() {
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
        ArrayList<Notice> notices = new ArrayList<>();
        for (DataSnapshot ds : snapshot.getChildren()){
            DatabaseConstant dc = new DatabaseConstant();
            String title = ds.child(dc.NOTICE_TITLE).getValue(String.class);
            String date = ds.child(dc.NOTICE_DATE).getValue(String.class);
            notices.add(new Notice(ds.getKey(),title,date));
        }
        NoticeAdapter adapter = new NoticeAdapter(notice_list.this,notices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}