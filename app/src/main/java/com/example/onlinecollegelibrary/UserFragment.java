package com.example.onlinecollegelibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserFragment extends Fragment {
    RecyclerView recyclerView;
    Boolean isChat;
    DatabaseReference myRef;
    FloatingActionButton addUserBtn;
    LinearLayout toolbar;
    public UserFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        addUserBtn = view.findViewById(R.id.add_user_floating_button);
        toolbar = view.findViewById(R.id.contact_with_admin_toolbar);

        isChat = this.getArguments().getBoolean("is chat");
        String userType = new SessionManager(getContext()).getUserType();
        if(TextUtils.equals(userType,new DatabaseConstant().ADMIN)){
            myRef = FirebaseDatabase.getInstance().getReference("Users");
            toolbar.setVisibility(View.GONE);
            if(!isChat){
                addUserBtn.setVisibility(View.VISIBLE);
            }
        }else {
            myRef = FirebaseDatabase.getInstance().getReference("Admin");
            toolbar.setVisibility(View.VISIBLE);
        }

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

        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RegisterForm.class);
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.users_view);

        return view;
    }
    private void showData(DataSnapshot dataSnapshot){
        final ArrayList<User> users = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            String name = ds.child("name").getValue(String.class);
            String sapId = ds.child("sapId").getValue(String.class);
            users.add(new User(name,sapId));
//            users.add(sapId);
        }

        UserAdapter userAdapter = new UserAdapter(getActivity(),users,isChat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userAdapter);
    }

}