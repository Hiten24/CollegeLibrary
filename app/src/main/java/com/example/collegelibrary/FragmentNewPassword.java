package com.example.collegelibrary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;

public class FragmentNewPassword extends Fragment {
    TextInputLayout password,confirmPassword;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String sapId;
    public FragmentNewPassword() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_password, container, false);
        database = FirebaseDatabase.getInstance();
        password = view.findViewById(R.id.newPass);
        confirmPassword = view.findViewById(R.id.newConfirmPass);
        sapId = getArguments().getString("SapId");
        myRef = database.getReference("Users").child(sapId).child("pass");
        Button updatepassbtn = view.findViewById(R.id.updatePassBtn);
        updatepassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrPassword = password.getEditText().getText().toString().trim();
                String usrConfirmPassword = confirmPassword.getEditText().getText().toString().trim();

                if(TextUtils.isEmpty(usrPassword)){
                    password.setError("Enter Password");
                    return;
                }
                if(TextUtils.isEmpty(usrConfirmPassword)){
                    confirmPassword.setError("Enter Confirm Password");
                    return;
                }
                if(!TextUtils.equals(usrPassword,usrConfirmPassword)){
                    confirmPassword.setError("Password not match");
                    return;
                }
                String HasedPassword = null;
                PasswordEncryptionHashGenerator passHash = new PasswordEncryptionHashGenerator();
                try {
                    HasedPassword = passHash.generateSHA256(usrPassword);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                myRef.setValue(HasedPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(),"Password Updated",Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.action_fragmentNewPassword_to_fragmentPasswordUpdated);
                        }else {
                            Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });
        return view;
    }
}