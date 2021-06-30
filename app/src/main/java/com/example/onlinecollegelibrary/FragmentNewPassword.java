package com.example.onlinecollegelibrary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentNewPassword extends Fragment {
    TextInputLayout password, confirmPassword;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String sapId;
    Boolean isForgot;

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
        isForgot = getArguments().getBoolean("isForgot");
//        myRef = FirebaseDatabase.getInstance().getReference("Users").child(sapId).child("pass");
        Button updatepassbtn = view.findViewById(R.id.updatePassBtn);
        updatepassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrPassword = password.getEditText().getText().toString().trim();
                String usrConfirmPassword = confirmPassword.getEditText().getText().toString().trim();

                if (TextUtils.isEmpty(usrPassword)) {
                    password.setError("Enter Password");
                    return;
                }
                if (TextUtils.isEmpty(usrConfirmPassword)) {
                    confirmPassword.setError("Enter Confirm Password");
                    return;
                }
                if (!TextUtils.equals(usrPassword, usrConfirmPassword)) {
                    confirmPassword.setError("Password not match");
                    return;
                }
                /*String HasedPassword = null;
                PasswordEncryptionHashGenerator passHash = new PasswordEncryptionHashGenerator();
                try {
                    HasedPassword = passHash.generateSHA256(usrPassword);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }*/

                String password = new HasingPassword().hasedPassword(usrPassword);
                updatePassword(password, sapId, isForgot);
                /*myRef.setValue(HasedPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(),"Password Updated",Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.action_fragmentNewPassword_to_fragmentPasswordUpdated);
                        }else {
                            Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/


            }
        });
        return view;
    }

    public void updatePassword(String password, String sapId, Boolean isForgot) {
//        String sapId = new SessionManager(getContext()).getSapId();
        if (isForgot) {
            updateForgotPassword(sapId, password);
        } else {
            UpdatePassword updatePassword = new UpdatePassword(getContext());
            updatePassword.updatePassword(sapId, password);
        }
        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(),"Password updated",Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference("Users").child(sapId).child("pass").setValue(new HasingPassword().hasedPassword(password));
                }else {
                    Toast.makeText(getContext(),"Error password not updated",Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }

    private void updateForgotPassword(String sapId, String password) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child(sapId).getValue(User.class);
                FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getEmail(), user.getPass()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /*FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            currentUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        myRef.child("pass").setValue(password);
                                        Toast.makeText(getContext(),"Password Changed.",Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
//                                        Navigation.findNavController(getView()).navigate(R.id.action_fragmentPasswordUpdated_to_fragmentLogin);

                                    }
                                }
                            });*/
                            UpdatePassword updatePassword = new UpdatePassword(getContext());
                            updatePassword.updatePassword(sapId, password);
//                            if(isPassUpdated){
                            changedPassword();
//                            }
                        } else {
                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void changedPassword() {
        Toast.makeText(getContext(), "Password Changed.", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
    }

}