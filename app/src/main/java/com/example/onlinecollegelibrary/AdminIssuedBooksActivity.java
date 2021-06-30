package com.example.onlinecollegelibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class AdminIssuedBooksActivity extends AppCompatActivity {
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_issued_books);
        Fragment selectedFragment = new IssuedBooksFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.admin_issued_books_container,selectedFragment).commit();

        backBtn = findViewById(R.id.admin_issued_books_back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.tool_bar));
    }
}