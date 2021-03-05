package com.example.onlinecollegelibrary;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FetchDataFromDatabase {
    private String name,email,sapId,studentClass,div,address,mobNo,pass;
    private DatabaseReference myRef;
    public FetchDataFromDatabase(String sapId) {
        this.sapId = sapId;
    }
    private void fetchdata(){
        myRef = FirebaseDatabase.getInstance().getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child(sapId).getValue(User.class);
                name = user.getName();
                email = user.getEmail();
                studentClass = user.getStudentClass();
                div = user.getDiv();
                address = user.getAddress();
                mobNo = user.getMobNo();
                pass = user.getPass();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getName() {
        fetchdata();
        return name;
    }

    public String getEmail() {
        fetchdata();
        return email;
    }

    public String getStudentClass() {
        fetchdata();
        return studentClass;
    }

    public String getDiv() {
        fetchdata();
        return div;
    }

    public String getAddress() {
        fetchdata();
        return address;
    }

    public String getMobNo() {
        fetchdata();
        return mobNo;
    }

    public String getPass() {
        fetchdata();
        return pass;
    }
}
