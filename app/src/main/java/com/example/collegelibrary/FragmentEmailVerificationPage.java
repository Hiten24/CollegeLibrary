package com.example.collegelibrary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class FragmentEmailVerificationPage extends Fragment {
    FirebaseAuth mAuth;
    Button emailVerifiedBtn;
    String emailID;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentEmailVerificationPage() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentEmailVerificationPage newInstance(String param1, String param2) {
        FragmentEmailVerificationPage fragment = new FragmentEmailVerificationPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_email_verification_page, container, false);
        mAuth = FirebaseAuth.getInstance();
        emailID = getArguments().getString("EmailID");
        emailVerifiedBtn = view.findViewById(R.id.reset_pass_mail_sent_verifiedMail);
        emailVerifiedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser().isEmailVerified()){
                    Navigation.findNavController(view).navigate(R.id.action_fragmentEmailVerificationPage_to_fragmentNewPassword);
                }
                else {
                    Toast.makeText(getActivity(),"Please verify your email id",Toast.LENGTH_SHORT).show();
                }
            }
        });
        sendEmailVerification();

        return view;
    }
    private void sendEmailVerification(){
        mAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(),"Please Check your email",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}