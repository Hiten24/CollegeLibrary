package com.example.onlinecollegelibrary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateCurrentPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateCurrentPassword extends Fragment {
    Button updateBtn;
    TextInputLayout oldPass,newPass,newConfirmPass;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateCurrentPassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateCurrentPassword.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateCurrentPassword newInstance(String param1, String param2) {
        UpdateCurrentPassword fragment = new UpdateCurrentPassword();
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
        View view =  inflater.inflate(R.layout.fragment_update_current_password, container, false);

        oldPass = view.findViewById(R.id.update_pass_old_pass);
        newPass = view.findViewById(R.id.update_pass_new_pass);
        newConfirmPass = view.findViewById(R.id.update_pass_confirm_pass);
        updateBtn = view.findViewById(R.id.update_pass_button);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strOldPassword = oldPass.getEditText().getText().toString().trim();
                String strNewPassword = newPass.getEditText().getText().toString().trim();
                String strConfirmPassword = newConfirmPass.getEditText().getText().toString().trim();
                HasingPassword hasingPassword = new HasingPassword();
                String hasedOldPassword =  hasingPassword.hasedPassword(strOldPassword);
                /*String databasePassword = new FetchDataFromDatabase()*/
            }
        });

        return view;
    }
}