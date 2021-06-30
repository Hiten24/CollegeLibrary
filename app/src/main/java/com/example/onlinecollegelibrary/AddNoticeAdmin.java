package com.example.onlinecollegelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoticeAdmin extends AppCompatActivity implements View.OnClickListener {
    EditText body;
    ImageButton bold,italic,underline,clear;
    EditText noticeTitle,noticeContent;
    Button addNoticeBnt;
    Boolean isEditClicked;
    String NoticeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice_admin);

        isEditClicked = getIntent().getExtras().getBoolean("isEditClick");
        if(isEditClicked){
            NoticeId = getIntent().getExtras().getString(new DatabaseConstant().NOTICE_ID);
            getData(NoticeId);
        }


        body = findViewById(R.id.notice_body_view);
        bold = findViewById(R.id.bold_syle);
        bold.setOnClickListener(this);

        italic = findViewById(R.id.italic_style);
        italic.setOnClickListener(this);

        underline = findViewById(R.id.underline_style);
        underline.setOnClickListener(this);

        clear = findViewById(R.id.format_clear_style);
        clear.setOnClickListener(this);

        noticeTitle = findViewById(R.id.notice_title_view);
        noticeContent = findViewById(R.id.notice_body_view);
        addNoticeBnt = findViewById(R.id.add_notice_button);
        addNoticeBnt.setOnClickListener(this);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    public void onClick(View v) {
        int endSelection = body.getSelectionEnd();
        switch (v.getId()){
            case R.id.bold_syle:
                formatText(new StyleSpan(Typeface.BOLD),false);
                break;
            case R.id.italic_style:
                formatText(new StyleSpan(Typeface.ITALIC),false);
                break;
            case R.id.underline_style:
//                btnUnderline();
                break;
            case R.id.format_clear_style:
                formatText(new StyleSpan(Typeface.NORMAL),true);
                break;
            case R.id.add_notice_button:
                addToDB();
                break;
        }
        body.setSelection(endSelection);
    }
    private void addToDB(){
        DatabaseConstant dc = new DatabaseConstant();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(dc.NOTICE_TABLE_TITLE);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String title = noticeTitle.getText().toString().trim();
                String content = noticeContent.getText().toString().trim();
                String dateStr = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(new Date());
                DatabaseReference noticeRef = null;
                if(isEditClicked){
                    noticeRef = myRef.child(NoticeId);
                }else {
                    noticeRef = myRef.child(String.valueOf(snapshot.getChildrenCount() + 1));
                }
                noticeRef.child(dc.NOTICE_TITLE).setValue(title);
                noticeRef.child(dc.NOTICE_BODY).setValue(content);
                noticeRef.child(dc.NOTICE_DATE).setValue(dateStr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void formatText(StyleSpan format,Boolean isClear){
        SpannableStringBuilder spannableString = new SpannableStringBuilder(body.getText());
        spannableString.setSpan(format,body.getSelectionStart(),body.getSelectionEnd(),0);

        if(isClear){
            StyleSpan[] span = spannableString.getSpans(body.getSelectionStart(),body.getSelectionEnd(),StyleSpan.class);
            for (int i = 0; i < span.length; i++) {
                spannableString.removeSpan(span[i]);
            }
//            spannableString.removeSpan(new StyleSpan(Typeface.BOLD));
        }
        body.setText(spannableString);
    }

    private void getData(String noticeNumber){
        new DatabaseHelper(getApplicationContext()).getNotice(noticeNumber, new NoticeCallback() {
            @Override
            public void onNoticeReceived(String title, String body, String date) {
                noticeTitle.setText(title);
                noticeContent.setText(body);
            }
        });
    }
}