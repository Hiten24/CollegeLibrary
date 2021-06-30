package com.example.onlinecollegelibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileFragment extends Fragment {
    TextView name,sapId,mobileNo,email,studentClass,division,address,profileUsername,profileSapid;
    Button changePasswordBtn,logOut;

    public UserProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
//        String sapID = getActivity().getIntent().getExtras().getString("sapId");
//        Toast.makeText(getContext(),sapID,Toast.LENGTH_SHORT).show();

        name = view.findViewById(R.id.profile_user_name);
        sapId = view.findViewById(R.id.profile_sapid);
        mobileNo = view.findViewById(R.id.profile_phone);
        email = view.findViewById(R.id.profile_email);
        studentClass = view.findViewById(R.id.profile_class_and_div);
        address = view.findViewById(R.id.profile_address);
        logOut = view.findViewById(R.id.user_profile_log_out_btn);

        profileUsername = view.findViewById(R.id.ProfileUserName);
        profileSapid = view.findViewById(R.id.ProfileUsersap);
        changePasswordBtn = view.findViewById(R.id.change_password_button);

        String sapID = "53003180070";

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sapId = new SessionManager(getContext()).getSapId();
                Toast.makeText(getContext(),sapId,Toast.LENGTH_SHORT).show();
            }
        });

        new DatabaseHelper(getContext()).getUserDetails(sapID, new DatabaseCallback() {
            @Override
            public void onDataReceived(User user) {
                updateUi(user);
            }
        });

//        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        getActivity().getWindow().setStatusBarColor(this.getResources().getColor(R.color.purple_200));

        return view;
    }

    private void updateUi(User u){
        String classAndDiv = u.getStudentClass()+" - "+u.getDiv();
        profileUsername.setText(u.getName());
        profileSapid.setText(u.getSapId());
        name.setText(u.getName());
        sapId.setText(u.getSapId());
        mobileNo.setText(u.getMobNo());
        email.setText(u.getEmail());
        studentClass.setText(classAndDiv);
        address.setText(u.getAddress());
    }

}