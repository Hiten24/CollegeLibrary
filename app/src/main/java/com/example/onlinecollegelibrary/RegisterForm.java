package com.example.onlinecollegelibrary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public class RegisterForm extends AppCompatActivity{
    TextInputLayout studentClassLayout,studentDivLayout;
    AutoCompleteTextView dropDownClass,dropDownDiv;
    AutoCompleteTextView studentClass,div;
    TextInputLayout fullName,email,sapId,address,mobNo,pass;
    Button signUpBtn;
    SwitchMaterial switchMaterial;
    TextView addUserFromExcel;
    ImageView backButton;
    private static final int PICKFILE_REQUEST_CODE = 8778;
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
        addUserFromExcel = findViewById(R.id.add_user_from_Excel);

        backButton = findViewById(R.id.admin_register_usre_back_button);

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

        addUserFromExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterForm.this,AddBooksFromExcel.class);
                intent.putExtra("data type","users");
                startActivity(intent);
            }
        });

        switchMaterial = findViewById(R.id.is_admin_switch);

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    studentClassLayout.setVisibility(View.GONE);
                    studentDivLayout.setVisibility(View.GONE);
                    pass.setVisibility(View.VISIBLE);
                    /*div.setVisibility(View.GONE);
                    pass.setVisibility(View.VISIBLE);*/
                }else {
                    studentClassLayout.setVisibility(View.VISIBLE);
                    studentDivLayout.setVisibility(View.VISIBLE);
                    pass.setVisibility(View.GONE);
                    /*div.setVisibility(View.VISIBLE);
                    pass.setVisibility(View.GONE);*/
                }
            }
        });

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
                String userPassword;

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
                if(TextUtils.isEmpty(userClass) && switchMaterial.isChecked() != true){
                    Toast.makeText(getApplicationContext(),"Enter yout Class",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userDiv) && switchMaterial.isChecked() != true){
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
                if (switchMaterial.isChecked() == true){
                    userPassword = pass.getEditText().getText().toString().trim();
                    if(TextUtils.isEmpty(userPassword) && switchMaterial.isChecked() != false ){
                        Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else {
                    userPassword = userSapId+"@123";
                }
                signUpUsingEmail(userName,userMail,userSapId,userClass,userDiv,useraddress,userMobNo,userPassword,switchMaterial.isChecked(),false);
                //Create User or Register users
                //Checking if user already exists or not if not then proceed
                /*myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                            String finalHasedPass = new HasingPassword().hasedPassword(userPassword);
                                            Toast.makeText(RegisterForm.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            if(!task.isSuccessful()){
                                                Toast.makeText(RegisterForm.this,"Registration Failed"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                generateUser(userName,userMail,userSapId,userClass,userDiv,useraddress,userMobNo,finalHasedPass,switchMaterial.isChecked(),false);

                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/


            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.tool_bar));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        readExcel(uri);
    }

    private void readExcel(Uri uri){
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(inputStream);
            HSSFWorkbook workbook = new HSSFWorkbook(poifsFileSystem);
            HSSFSheet sheet = workbook.getSheetAt(0);
            for (int i=1;i<sheet.getPhysicalNumberOfRows();i++){
                for (int j=0;j<sheet.getRow(0).getPhysicalNumberOfCells();j++){

                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signUpUsingEmail(String userName,String userMail,String userSapId,String userClass,String userDiv,String useraddress,String userMobNo,String userPassword,Boolean isAdmin,Boolean fromExcel){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userSapId).exists()){
                    if(!fromExcel){
                        Toast.makeText(getApplicationContext(),"User already exists!",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    String finalHasedPass = new HasingPassword().hasedPassword(userPassword);
                    auth.createUserWithEmailAndPassword(userMail,finalHasedPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!fromExcel){
                                        Toast.makeText(RegisterForm.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    }
                                    if(!task.isSuccessful()){
                                        if(!fromExcel){
                                            Toast.makeText(RegisterForm.this,"Registration Failed"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        generateUser(userName,userMail,userSapId,userClass,userDiv,useraddress,userMobNo,finalHasedPass,isAdmin,fromExcel);
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

    public void generateUser(String name, String mail, String sapid, String usrClass, String div, String address, String mobNo, String pass, Boolean isAdmin,Boolean fromExcel){
        String path = "Users";
        if(isAdmin){
            path = "Admin";
        }
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference(path);
        User usr = new User(name,mail,sapid,usrClass,div,address,mobNo,pass);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child(sapid).exists()){
                    usersRef.child(sapid).setValue(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                if(!fromExcel){
                                    Toast.makeText(getApplicationContext(),"User Added Successfully",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                if(!fromExcel){
                                    Toast.makeText(getApplicationContext(),"Error "+ task.getException(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }else {
                    if(!fromExcel){
                        Toast.makeText(getApplicationContext(),"User is already Exists",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}