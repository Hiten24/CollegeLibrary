package com.example.onlinecollegelibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserHome extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment selectedFragment = new UserDashboardFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.user_home_container,selectedFragment).commit();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.home:
                    selectedFragment = new UserDashboardFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.user_home_container,selectedFragment).commit();
                    return true;
                case R.id.library:
                    selectedFragment = new BookListFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.user_home_container,selectedFragment).commit();
                    return true;
                case R.id.profile:
                    selectedFragment = new UserProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.user_home_container,selectedFragment).commit();
                    return true;
                case R.id.dashboard:
//                    selectedFragment = new ChatWithAdmin();
                    selectedFragment = new UserFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("is chat",true);
                    selectedFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.user_home_container,selectedFragment).commit();
                    return true;
                case R.id.notice:
                    selectedFragment = new NoticeListFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.user_home_container,selectedFragment).commit();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.user_home_container,selectedFragment).commit();
            return false;
        }
    };
}