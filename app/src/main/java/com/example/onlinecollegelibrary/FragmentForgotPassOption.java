package com.example.onlinecollegelibrary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentForgotPassOption#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentForgotPassOption extends Fragment {
    FirebaseAuth mAuth;
    DatabaseReference database, myRef;
    String sapID, emailId, phoneNumber;
    TextView smsDescription, smsNum, smsLayout,mailIdText;
    ImageView smsIcon;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentForgotPassOption() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentForgotPassOption newInstance(String param1, String param2) {
        FragmentForgotPassOption fragment = new FragmentForgotPassOption();
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
        View view = inflater.inflate(R.layout.fragment_forgot_pass_option, container, false);
        sapID = getArguments().getString("SapID");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        myRef = database.child("Users/" + sapID);
        smsDescription = view.findViewById(R.id.sms_title);
        smsNum = view.findViewById(R.id.sms_value);
        smsIcon = view.findViewById(R.id.sms_icon);
        smsLayout = view.findViewById(R.id.smsLayoutBg);
        mailIdText = view.findViewById(R.id.mail_value);
//        RelativeLayout smsLayout = view.findViewById(R.id.smsLayout);
        RelativeLayout mailLayout = view.findViewById(R.id.mailLayout);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User usr = snapshot.getValue(User.class);
                phoneNumber = usr.getMobNo();
                emailId = usr.getEmail();
                smsNum.setText("+91 "+phoneNumber);
                mailIdText.setText(emailId);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to read data", Toast.LENGTH_SHORT).show();
            }
        });


        smsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedOnSmsLayout(v);
            }
        });

        smsDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedOnSmsLayout(v);
            }
        });

        smsNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedOnSmsLayout(v);
            }
        });

        smsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedOnSmsLayout(v);
            }
        });

        mailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("EmailID", emailId);
                bundle.putString("SapID", sapID);
                Navigation.findNavController(v).navigate(R.id.action_fragmentForgotPassOption_to_fragmentEmailVerificationPage, bundle);
            }
        });
        return view;
    }

    public void clickedOnSmsLayout(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("PhoneNumber", "+91" + phoneNumber);
        bundle.putString("SapID", sapID);
        Navigation.findNavController(view).navigate(R.id.action_fragmentForgotPassOption_to_fragmentForgotPassotpVerification2, bundle);
    }
}