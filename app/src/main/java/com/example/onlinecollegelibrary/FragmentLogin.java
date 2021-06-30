package com.example.onlinecollegelibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FragmentLogin extends Fragment {
    Button loginBtn;
    TextInputLayout loginSapId, loginPass;
    FirebaseAuth auth;
    DatabaseReference database, myRef;
    TextView regBtn;
    Boolean isAdmin = false;
    SessionManager sessionManager;

    public FragmentLogin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        TextView forgotPassBtn = view.findViewById(R.id.forgotpass);
        auth = FirebaseAuth.getInstance();
        loginBtn = view.findViewById(R.id.loginBtn);
        loginSapId = view.findViewById(R.id.login_sapid);
        loginPass = view.findViewById(R.id.login_password);
        database = FirebaseDatabase.getInstance().getReference();
        regBtn = view.findViewById(R.id.loginWelcomeMsg);
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
//            String usrMail = user.getEmail();
            String accountType = new SessionManager(getContext()).getUserType();
            Toast.makeText(getContext(), accountType, Toast.LENGTH_SHORT).show();

            if (TextUtils.equals(accountType, "Admin")) {
                Toast.makeText(getContext(), "Admin", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), AdminDashboard.class);
                getActivity().finish();
                startActivity(intent);

            } else {
                Toast.makeText(getContext(), "User", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), UserHome.class);
                getActivity().finish();
                startActivity(intent);
            }

        } else {
            forgotPassBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_fragmentLogin_to_fragmentForgotPassDetail);
                }
            });
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Login in...", Toast.LENGTH_SHORT).show();
                String usrSapId = loginSapId.getEditText().getText().toString().trim();
                String usrPassword = loginPass.getEditText().getText().toString().trim();
//                myRef = database.child("Users/"+usrSapId);
                if (TextUtils.isEmpty(usrSapId)) {
//                    Toast.makeText(getActivity().getApplicationContext(),"Enter Sap ID",Toast.LENGTH_SHORT).show();
                    loginSapId.setError("Enter Valid Sap ID");
                }
                if (TextUtils.isEmpty(usrPassword)) {
//                    Toast.makeText(getActivity().getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
                    loginPass.setError("Enter Valid Password");
                }
                String finalHasedPassword = new HasingPassword().hasedPassword(usrPassword);
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isAdmin = false;
                        User usr = snapshot.child("Users").child(usrSapId).getValue(User.class);
                        if (snapshot.child("Admin").child(usrSapId).exists()) {
                            usr = snapshot.child("Admin").child(usrSapId).getValue(User.class);
                            isAdmin = true;
                        } else if (!snapshot.child("Users").child(usrSapId).exists()) {
                            loginSapId.setError("User Not found");
                            return;
                        } else {
                            usr = snapshot.child("Users").child(usrSapId).getValue(User.class);
                        }
                        if (TextUtils.equals(finalHasedPassword, usr.getPass())) {
                            User finalUsr = usr;
                            auth.signInWithEmailAndPassword(usr.getEmail(), finalHasedPassword)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
//                                                Toast.makeText(getActivity().getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = null;
                                                sessionManager = new SessionManager(getContext());
                                                if (isAdmin) {
                                                    sessionManager.createLoginSession(finalUsr.getName(),new DatabaseConstant().ADMIN, usrSapId);
                                                    intent = new Intent(getActivity(), AdminDashboard.class);
                                                    getActivity().finish();
                                                    startActivity(intent);
                                                } else {
                                                    sessionManager.createLoginSession(finalUsr.getName(),new DatabaseConstant().USER, usrSapId);
                                                    if (TextUtils.equals(finalHasedPassword, new HasingPassword().hasedPassword(finalUsr.getSapId() + "@123"))) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("EmailID", finalUsr.getEmail());
                                                        Navigation.findNavController(view).navigate(R.id.action_fragmentLogin_to_fragmentEmailVerificationPage, bundle);
                                                    } else {
                                                        intent = new Intent(getActivity(), UserHome.class);
                                                        getActivity().finish();
                                                        startActivity(intent);
                                                    }
                                                }
                                                /*Bundle bundle = new Bundle();
                                                bundle.putString("email",usr.getEmail());
                                                Navigation.findNavController(v).navigate(R.id.action_fragmentLogin_to_emailVerificationWhileSignin,bundle);*/
                                            } else {
                                                loginPass.setError("Wrong Password db");
                                                Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                        }
                                    });

                        } else {
//                            Toast.makeText(getActivity().getApplicationContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
                            loginPass.setError("Wrong Password");
                            return;
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
                Intent intent = new Intent(getActivity(), RegisterForm.class);
                startActivity(intent);
            }
        });

        return view;
    }

}