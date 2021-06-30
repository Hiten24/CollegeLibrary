package com.example.onlinecollegelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersManageInAdmin extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_manage_in_admin);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");

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
        backButton = findViewById(R.id.user_list_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView = findViewById(R.id.users_view);
    }
    private void showData(DataSnapshot dataSnapshot){
        final ArrayList<User> users = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            String name = ds.child("name").getValue(String.class);
            String sapId = ds.child("sapId").getValue(String.class);
            users.add(new User(name,sapId));
//            users.add(sapId);
        }

        UserAdapter userAdapter = new UserAdapter(UsersManageInAdmin.this,users,false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);
    }
}