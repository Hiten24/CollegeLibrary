package com.example.onlinecollegelibrary;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
    TextView name,sapId,mobileNo,email,studentClass,division,address,profileUsername,profileSapid;
    Button changePasswordBtn,logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        String sapID = getIntent().getExtras().getString("sapId");
        Toast.makeText(getApplicationContext(),sapID,Toast.LENGTH_SHORT).show();

        name = findViewById(R.id.profile_user_name);
        sapId = findViewById(R.id.profile_sapid);
        mobileNo = findViewById(R.id.profile_phone);
        email = findViewById(R.id.profile_email);
        studentClass = findViewById(R.id.profile_class_and_div);
        address = findViewById(R.id.profile_address);

        profileUsername = findViewById(R.id.ProfileUserName);
        profileSapid = findViewById(R.id.ProfileUsersap);
        changePasswordBtn = findViewById(R.id.change_password_button);
        logOut = findViewById(R.id.user_profile_log_out_button);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Changing password...",Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();

            }
        });

        new DatabaseHelper(this).getUserDetails(sapID, new DatabaseCallback() {
            @Override
            public void onDataReceived(User user) {
                updateUi(user);
            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.purple_200));
    }
    private void updateUi(User u){
        String classAndDiv = u.getStudentClass()+" - "+u.getDiv();
        profileUsername.setText(u.getName());
        profileSapid.setText(u.getSapId());
        name.setText(u.getName());
        sapId.setText(u.getSapId());
        mobileNo.setText(u.getMobNo());
        email.setText(u.getEmail());
        studentClass.setText(classAndDiv);
        address.setText(u.getAddress());
    }
}