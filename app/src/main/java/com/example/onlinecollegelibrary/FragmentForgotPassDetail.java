package com.example.onlinecollegelibrary;

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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentForgotPassDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentForgotPassDetail extends Fragment {
    TextInputLayout sapId;
    Button forgotBtn;
    private DatabaseReference myRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentForgotPassDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentForgotPassDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentForgotPassDetail newInstance(String param1, String param2) {
        FragmentForgotPassDetail fragment = new FragmentForgotPassDetail();
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
        View view = inflater.inflate(R.layout.fragment_forgot_pass_detail, container, false);
        Button detailBtn = view.findViewById(R.id.forgotUsrinfoBtn);
        sapId = view.findViewById(R.id.forget_pass_sapid);
        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sapIdStr = sapId.getEditText().getText().toString().trim();
                if(TextUtils.isEmpty(sapIdStr)){
                    sapId.setError("Enter Valid SapId");
                }
                else {
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child(sapIdStr).exists()){
                                Bundle bundle = new Bundle();
                                bundle.putString("SapID",sapIdStr);
                                Navigation.findNavController(v).navigate(R.id.action_fragmentForgotPassDetail_to_fragmentForgotPassOption,bundle);
                            }else {
                                Toast.makeText(getActivity(),"Sap ID does not exist",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        return view;
    }
}