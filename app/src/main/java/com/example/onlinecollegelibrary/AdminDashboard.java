package com.example.onlinecollegelibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AdminDashboard extends AppCompatActivity {
    CardView addUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        addUser = findViewById(R.id.add_user_admin);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"add User",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminDashboard.this,RegisterForm.class);
                startActivity(intent);
            }
        });
    }
}