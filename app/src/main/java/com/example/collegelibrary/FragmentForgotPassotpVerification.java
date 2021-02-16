package com.example.collegelibrary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.core.utilities.Utilities;

import java.util.concurrent.TimeUnit;

public class FragmentForgotPassotpVerification extends Fragment {
    TextView descriptionNo,resentOTP;
    String userNumber,mVerificationId,sapId;
    EditText manualCodeByUser;
    FirebaseAuth mAuth;
    PhoneAuthProvider.ForceResendingToken mResendToken; // for resendOTP function
    public FragmentForgotPassotpVerification() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userNumber = getArguments().getString("PhoneNumber");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_passotp_verification, container, false);
        Button otpbtn = view.findViewById(R.id.verifyBtn);
        mAuth = FirebaseAuth.getInstance();
        descriptionNo = view.findViewById(R.id.verification_discription3);
        descriptionNo.setText(userNumber);
        resentOTP = view.findViewById(R.id.didntGetOTP2);
        manualCodeByUser = view.findViewById(R.id.otpEditText);
        sapId = getArguments().getString("SapID");

        sendVerificationCodeToUser(userNumber); // sending OTP to user by calling sendVerificationToUser function

        otpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String manulCode = manualCodeByUser.getText().toString().trim();
                if(TextUtils.isEmpty(manulCode)){
                    Toast.makeText(getActivity(),"Enter OTP",Toast.LENGTH_SHORT).show();
                    return;
                }
                verifyCode(manulCode); // verifing otp by calling userdefined verifyCode function
            }
        });

        //when user clicks Resend otp Button
        resentOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOPTTOUser(userNumber,mResendToken);
            }
        });

        return view;
    }
        private void sendVerificationCodeToUser(String phoneNo){
            //Sending OTP to User
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                    .setPhoneNumber(phoneNo)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(getActivity())
                    .setCallbacks(mCallbacks)
                    .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }

        private void resendOPTTOUser(String phoneNo,PhoneAuthProvider.ForceResendingToken token){
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                    .setPhoneNumber(phoneNo)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(getActivity())
                    .setCallbacks(mCallbacks)
                    .setForceResendingToken(token)
                    .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }

        private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code =phoneAuthCredential.getSmsCode();
                if(code!=null){
                    manualCodeByUser.setText(code);
                    verifyCode(code);
                }
            }
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getActivity(),"Failed "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                mResendToken = forceResendingToken;
            }
        };

        private void verifyCode(String codeByUser){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,codeByUser);
            signInTheUserWithCredential(credential);
        }

        private void signInTheUserWithCredential(PhoneAuthCredential credential){
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(),"Verified",Toast.LENGTH_SHORT).show();
                                Bundle b = new Bundle();
                                b.putString("SapId",sapId);
                                Navigation.findNavController(getView()).navigate(R.id.action_fragmentForgotPassotpVerification2_to_fragmentNewPassword,b);
                            }
                            else {
                                Toast.makeText(getActivity(),"Failed"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

}