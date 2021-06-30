package com.example.onlinecollegelibrary;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdminDashboardAdapter extends FragmentPagerAdapter {
    private Context context;
    public AdminDashboardAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new UserFragment();
        if(position == 0){
            Bundle bundle = new Bundle();
            bundle.putBoolean("is chat",false);
            fragment.setArguments(bundle);
            return fragment;
        }else if (position == 1){
            return new BookListFragment();
        }else if (position == 2){
            return new RequestedBooksFragment();
        }else {
            return new NoticeListFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "Users";
        }else if (position == 1){
            return "Books";
        }else if (position == 2){
            return "Requested Books";
        }else{
            return  "Notice";
        }
    }
}
