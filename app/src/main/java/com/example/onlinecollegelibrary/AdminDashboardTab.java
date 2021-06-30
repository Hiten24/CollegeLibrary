package com.example.onlinecollegelibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class AdminDashboardTab extends AppCompatActivity {
    TextView title;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_tab);

        title = findViewById(R.id.admin_dashboard_title);
        backButton = findViewById(R.id.admin_dashboard_back_button);

        ViewPager viewPager =findViewById(R.id.viewpager);
        AdminDashboardAdapter adapter = new AdminDashboardAdapter(this,getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        title.setText("Users");
                        return;
                    case 1:
                        title.setText("Books");
                        return;
                    case 2:
                        title.setText("Requested Books");
                        return;
                    case 3:
                        title.setText("Notice");
                        return;
                    default:
                        throw new IllegalStateException("Unexpected value: " + tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.tool_bar));

    }
}