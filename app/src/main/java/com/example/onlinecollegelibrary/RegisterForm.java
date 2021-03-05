package com.example.onlinecollegelibrary;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.security.NoSuchAlgorithmException;

public class RegisterForm extends AppCompatActivity {
    TextInputLayout studentClassLayout,studentDivLayout;
    AutoCompleteTextView dropDownClass,dropDownDiv;
    AutoCompleteTextView studentClass,div;
    TextInputLayout fullName,email,sapId,address,mobNo,pass;
    Button signUpBtn;
    FirebaseDatabase database;  //real time database
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);

        fullName = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        sapId = findViewById(R.id.sapid);
        address = findViewById(R.id.address);
        mobNo = findViewById(R.id.mobileno);
        pass = findViewById(R.id.login_password);
        signUpBtn = findViewById(R.id.RegisterBtn);
        studentClass =findViewById(R.id.dropdown_class);
        div = findViewById(R.id.dropdown_div);

        studentClassLayout = findViewById(R.id.studentclass);
        dropDownClass = findViewById(R.id.dropdown_class);
        studentDivLayout = findViewById(R.id.div);
        dropDownDiv = findViewById(R.id.dropdown_div);
        //firebase authentication
        FirebaseAuth auth = FirebaseAuth.getInstance();  //Firebase Auth
        database = FirebaseDatabase.getInstance(); //for realtime database
        String[] items = new String[]{
                "BMM",
                "BMS",
                "Bsc IT",
                "BA"
        };
        String[] divItems = new String[]{
                "A",
                "B"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterForm.this,R.layout.dropdown_item,items);
        dropDownClass.setAdapter(adapter);
        ArrayAdapter<String> divAdapter = new ArrayAdapter<>(RegisterForm.this,R.layout.dropdown_div,divItems);
        dropDownDiv.setAdapter(divAdapter);

        // Signup process
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userClass = studentClass.getText().toString().trim();
                String userDiv = div.getText().toString().trim();
                String userName = fullName.getEditText().getText().toString().trim();
                String userMail = email.getEditText().getText().toString().trim();
                String userSapId = sapId.getEditText().getText().toString().trim();
                String useraddress = address.getEditText().getText().toString().trim();
                String userMobNo = mobNo.getEditText().getText().toString().trim();
                String userPassword = pass.getEditText().getText().toString().trim();
                myRef = database.getReference().child("Users");
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userMail)){
                    Toast.makeText(getApplicationContext(),"Enter Email Address",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userSapId)){
                    Toast.makeText(getApplicationContext(),"Enter Sap ID",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userClass)){
                    Toast.makeText(getApplicationContext(),"Enter yout Class",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userDiv)){
                    Toast.makeText(getApplicationContext(),"Enter yout Division",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(useraddress)){
                    Toast.makeText(getApplicationContext(),"Enter Address",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userMobNo)){
                    Toast.makeText(getApplicationContext(),"Enter Mobile Number",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userPassword)){
                    Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                //Create User or Register users
                String hasedPass = null;
                PasswordEncryptionHashGenerator passHash = new PasswordEncryptionHashGenerator();
                try {
                    hasedPass = passHash.generateSHA256(userPassword);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                String finalHasedPass = hasedPass;
                //Checking if user already exists or not if not then proceed
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(userSapId).exists()){
                            Toast.makeText(getApplicationContext(),"User already exists!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            auth.createUserWithEmailAndPassword(userMail,userPassword)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Toast.makeText(RegisterForm.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            if(!task.isSuccessful()){
                                                Toast.makeText(RegisterForm.this,"Registration Failed"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                generateUser(userName,userMail,userSapId,userClass,userDiv,useraddress,userMobNo, finalHasedPass);

                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }
    public void generateUser(String name,String mail,String sapid,String usrClass,String div,String address,String mobNo,String pass){
        DatabaseReference usersRef = database.getReference("Users");
        User usr = new User(name,mail,sapid,usrClass,div,address,mobNo,pass);
        usersRef.child(sapid).setValue(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"User Added Successfully",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Error "+ task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}