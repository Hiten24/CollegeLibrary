package com.example.onlinecollegelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatUserList extends AppCompatActivity {
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user_list);

        backButton = findViewById(R.id.chat_user_list_back_button);

        Fragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is chat",true);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.chat_user_list_view,fragment).commit();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*recyclerView = findViewById(R.id.chat_user_list_view);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };*/

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.tool_bar));

    }
    /*private void showData(DataSnapshot dataSnapshot){
        final ArrayList<String> users = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            String name = ds.child("name").getValue(String.class);
            String sapId = ds.child("sapId").getValue(String.class);
//            users.add(new User(name,sapId));
            users.add(sapId);
        }

        UserAdapter userAdapter = new UserAdapter(ChatUserList.this,users,true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(userAdapter);
    }*/
}