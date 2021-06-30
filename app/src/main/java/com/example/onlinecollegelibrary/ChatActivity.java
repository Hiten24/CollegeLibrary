package com.example.onlinecollegelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {
    EditText message;
    ImageView sendButton,backButton;
    RecyclerView chatView;
    String sapId;
    TextView chatUserName,chatSapId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backButton = findViewById(R.id.activity_chat_back_button);
        chatView = findViewById(R.id.chat_with_admin_recycler_view);
        chatUserName = findViewById(R.id.chat_user_name);
        chatSapId = findViewById(R.id.chat_user_sap_id);

        String userSapId = getIntent().getExtras().getString("sapId");

        Boolean isFromAdmin = getIntent().getExtras().getBoolean("isFromAdmin");

        // to store and display user name and sap id
        if(isFromAdmin){
//            sapId = "53003180070";
            sapId = userSapId;
            new DatabaseHelper(getApplicationContext()).getUserDetails(userSapId, new DatabaseCallback() {
                @Override
                public void onDataReceived(User user) {
                    chatSapId.setText(user.getSapId());
                    chatUserName.setText(user.getName());
                }
            });
            userSapId = new SessionManager(getApplicationContext()).getSapId();
        }else {
            sapId = new SessionManager(getApplicationContext()).getSapId();
            String finalSapId = userSapId;
            FirebaseDatabase.getInstance().getReference("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatUserName.setText(snapshot.child(finalSapId).child("name").getValue(String.class));
                    chatSapId.setText(finalSapId);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        message = findViewById(R.id.chat_with_admin_message);
        sendButton = findViewById(R.id.chat_with_admin_send_btn);

        // to send message and storing msg in database
        String finalSapId = userSapId;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageStr = message.getText().toString().trim();
                DateAndTime dt = new DateAndTime();
                String date = dt.getDate() + " " + new SimpleDateFormat("MMMM").format(dt.getMonth()) + ", " + dt.getYear();
                Message messageObj = new Message(messageStr,new SessionManager(getApplicationContext()).getSapId(),date);
                new DatabaseHelper(getApplicationContext()).addMessage(messageObj,sapId,finalSapId);
                message.setText("");
            }
        });

        // back button action
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // display messages
        FirebaseDatabase.getInstance().getReference(new DatabaseConstant().MESSAGE_TITLE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                showData(snapshot.child(sapId).child(new DatabaseContract().MESSAGE));
                showData(snapshot.child(sapId).child(finalSapId));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.tool_bar));
    }
    private void showData(DataSnapshot snapshot){
        ArrayList<Message> messages = new ArrayList<>();
        for (DataSnapshot ds : snapshot.getChildren()){
//            String msg = ds.child(new DatabaseContract().MESSAGE).getValue(String.class);
            Message message = ds.getValue(Message.class);
//            int msgId;
            /*if(TextUtils.equals(message.getSapId(),new SessionManager(getApplicationContext()).getSapId())){
                msgId = 0;
            }else {
                msgId = 1;
            }*/
            messages.add(message);
        }
        ChatWithAdminAdapter chatAdapter = new ChatWithAdminAdapter(getApplicationContext(),messages);
        chatView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chatView.setAdapter(chatAdapter);
    }
}