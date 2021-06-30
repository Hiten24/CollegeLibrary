package com.example.onlinecollegelibrary;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "MySession";
    //    public static final String KEY_USERID = "userId";
    private static final String KEY_USERTYPE = "userType";
    private static final String KEY_SAPID = "sapId";
    private static final String KEY_USER_NAME = "userName";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String userName,String userType, String sapId) {
//        editor.putString(KEY_USERID,userId)
        editor.putString(KEY_USER_NAME,userName);
        editor.putString(KEY_USERTYPE, userType);
        editor.putString(KEY_SAPID, sapId);
        editor.commit();
    }

    public String getUserType() {
        return sharedPreferences.getString(KEY_USERTYPE, null);
//        return userType;
    }

    public String getSapId() {
        return sharedPreferences.getString(KEY_SAPID, null);
//        return sapId;
    }

    public String getUserName(){
        return sharedPreferences.getString(KEY_USER_NAME,null);
    }

}
