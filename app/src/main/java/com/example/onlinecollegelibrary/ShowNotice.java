package com.example.onlinecollegelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowNotice extends AppCompatActivity {
    TextView title,content,date;
    ImageView backBtn,editButton;
    String noticeNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notice);

        noticeNumber = getIntent().getExtras().getString("Notice Number");

        title = findViewById(R.id.notice_title_value);
        content = findViewById(R.id.notice_body_value);
        backBtn = findViewById(R.id.show_notice_back_button);
        date = findViewById(R.id.notice_date_value);
        editButton = findViewById(R.id.notice_edit_button);

        String isAdmin = new SessionManager(this).getUserType();
        if(TextUtils.equals(new DatabaseConstant().ADMIN,isAdmin)){
            editButton.setVisibility(View.VISIBLE);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowNotice.this,AddNoticeAdmin.class).putExtra(new DatabaseConstant().NOTICE_ID,noticeNumber));
            }
        });



        FirebaseDatabase.getInstance().getReference("Notice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                /*DatabaseContract dc = new DatabaseContract();
                String title = snapshot.child(noticeNumber).child(dc.NOTICE_TITLE).getValue(String.class);
                String body = snapshot.child(noticeNumber).child(dc.NOTICE_BODY).getValue(String.class);
                String date = snapshot.child(noticeNumber).child(dc.NOTICE_DATE).getValue(String.class);*/
                new DatabaseHelper(getApplicationContext()).getNotice(noticeNumber, new NoticeCallback() {
                    @Override
                    public void onNoticeReceived(String title, String body, String date) {
                        updateUi(title,body,date);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void updateUi(String sTitle,String body,String dateStr){
        date.setText(dateStr);
        title.setText(sTitle);
        content.setText(body);
    }
}