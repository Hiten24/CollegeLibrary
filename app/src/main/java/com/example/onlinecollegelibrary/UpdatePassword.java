package com.example.onlinecollegelibrary;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatePassword {
    Context context;
    UpdatePassword(Context context){
        this.context = context;
    }

    public void updatePassword(String sapId,String password){
//        String finaPassword = new HasingPassword().hasedPassword(password);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference("Users").child(sapId).child("pass").setValue(password);
                }else {
                    Toast.makeText(context,task.getException().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
