package com.example.onlinecollegelibrary;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
    TextView name,sapId,mobileNo,email,studentClass,division,address,profileUsername,profileSapid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        String sapID = "14";
        name = findViewById(R.id.profile_name_value);
        sapId = findViewById(R.id.profile_sapid_value);
        mobileNo = findViewById(R.id.profile_mobile_number_value);
        email = findViewById(R.id.profile_email_value);
        studentClass = findViewById(R.id.profile_class_value);
        address = findViewById(R.id.profile_address_value);
        profileUsername = findViewById(R.id.ProfileUserName);
        profileSapid = findViewById(R.id.ProfileUsersap);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users/"+sapID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u = snapshot.getValue(User.class);
                updateUi(u.getName(),u.getSapId(),u.getMobNo(),u.getEmail(),u.getStudentClass(),u.getDiv(),u.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Failed","Failed to read value.",error.toException());
            }
        });
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.purple_200));
    }
    private void updateUi(String sName,String sSapId,String sMobNo,String sEmail,String sClass,String sDiv,String sAddr){
        String classAndDiv = sClass+" - "+sDiv;
        profileUsername.setText(sName);
        profileSapid.setText(sSapId);
        name.setText(sName);
        sapId.setText(sSapId);
        mobileNo.setText(sMobNo);
        email.setText(sEmail);
        studentClass.setText(classAndDiv);
        address.setText(sAddr);
    }
}