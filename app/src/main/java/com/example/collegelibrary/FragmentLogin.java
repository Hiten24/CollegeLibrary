package com.example.collegelibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;


public class FragmentLogin extends Fragment {
    Button loginBtn;
    TextInputLayout loginSapId,loginPass;
    FirebaseAuth auth;
    DatabaseReference database,myRef;
    TextView regBtn;
    public FragmentLogin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        TextView forgotPassBtn =view.findViewById(R.id.forgotpass);
        auth = FirebaseAuth.getInstance();
        loginBtn = view.findViewById(R.id.loginBtn);
        loginSapId = view.findViewById(R.id.login_sapid);
        loginPass = view.findViewById(R.id.login_password);
        database = FirebaseDatabase.getInstance().getReference();
        regBtn = view.findViewById(R.id.loginWelcomeMsg);

        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_fragmentLogin_to_fragmentForgotPassDetail);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrSapId = loginSapId.getEditText().getText().toString().trim();
                String usrPassword = loginPass.getEditText().getText().toString().trim();
                myRef = database.child("Users/"+usrSapId);
                if(TextUtils.isEmpty(usrSapId)){
//                    Toast.makeText(getActivity().getApplicationContext(),"Enter Sap ID",Toast.LENGTH_SHORT).show();
                    loginSapId.setError("Enter Valid Email");
                }
                if(TextUtils.isEmpty(usrPassword)){
//                    Toast.makeText(getActivity().getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
                    loginPass.setError("Enter Valid Password");
                }
                String HasedPassword = null;
                PasswordEncryptionHashGenerator passHash = new PasswordEncryptionHashGenerator();
                try {
                    HasedPassword = passHash.generateSHA256(usrPassword);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                String finalHasedPassword = HasedPassword;
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User usr = snapshot.getValue(User.class);
                        if (TextUtils.equals(finalHasedPassword,usr.getPass())){
                            Toast.makeText(getActivity().getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(),ActivityHome.class);
                            startActivity(intent);
                        }
                        else{
//                            Toast.makeText(getActivity().getApplicationContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
                            loginPass.setError("Wrong Password");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RegisterForm.class);
                startActivity(intent);
            }
        });

        return view;
    }
}