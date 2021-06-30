package com.example.onlinecollegelibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;

public class AddBooksFromExcel extends AddBooks{
    TextView data;
    Button addBookFromExcel;
    private static final int PICKFILE_REQUEST_CODE = 8778;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books_from_excel);

        data = findViewById(R.id.data_of_excel);
        addBookFromExcel = findViewById(R.id.add_book_from_excel_btn);

        addBookFromExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent = Intent.createChooser(intent,"choose a file");
                startActivityForResult(intent,PICKFILE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        readExcel(uri);

    }
    private void readExcel(Uri uri){

        ReadExcelFile readExcelFile = new ReadExcelFile(getApplicationContext(),uri);
        HSSFSheet sheet = readExcelFile.getSheet();

        String dataType = getIntent().getExtras().getString("data type");

        if(TextUtils.equals(dataType,"books")){
            int isbnIndex = readExcelFile.getCellIndex(sheet,"isbn");
            int noOfBooksIndex = readExcelFile.getCellIndex(sheet,"No of Books");
            data.setText("");
            for (int i =1;i<sheet.getPhysicalNumberOfRows();i++){
                double isbnValue = sheet.getRow(i).getCell(isbnIndex).getNumericCellValue();
                String isbn = BigDecimal.valueOf(isbnValue).toPlainString();
                int noOfBooks = (int) sheet.getRow(i).getCell(noOfBooksIndex).getNumericCellValue();
                data.append(isbn+"\t"+noOfBooks+"\n");
                getDataFromExel(isbn,noOfBooks);
            }
        }else if(TextUtils.equals(dataType,"users")) {
            int nameIndex,emailIndex,sapIdIndex,classIndex,divIndex,addressIndex,mobileNumberIndex,passwordIndex;
            nameIndex = readExcelFile.getCellIndex(sheet,"Name");
            emailIndex = readExcelFile.getCellIndex(sheet,"Email");
            sapIdIndex = readExcelFile.getCellIndex(sheet,"Sap Id");
            classIndex = readExcelFile.getCellIndex(sheet,"Class");
            divIndex = readExcelFile.getCellIndex(sheet,"Div");
            addressIndex = readExcelFile.getCellIndex(sheet,"Address");
            mobileNumberIndex = readExcelFile.getCellIndex(sheet,"Mobile Number");
            passwordIndex = readExcelFile.getCellIndex(sheet,"Password");
            data.setText("");
            for (int i = 1;i<10;i++){
                String name = sheet.getRow(i).getCell(nameIndex).toString();
                String email = sheet.getRow(i).getCell(emailIndex).toString();
                double sapIdNum = sheet.getRow(i).getCell(sapIdIndex).getNumericCellValue();
                String sapId = BigDecimal.valueOf(sapIdNum).toPlainString();
                String sClass = sheet.getRow(i).getCell(classIndex).toString();
                String div = sheet.getRow(i).getCell(divIndex).toString();
                String address = sheet.getRow(i).getCell(addressIndex).toString();
                double mobNumberNum = sheet.getRow(i).getCell(mobileNumberIndex).getNumericCellValue();
                String mobNumber = BigDecimal.valueOf(mobNumberNum).toPlainString();
//                data.append(name+"\t"+email+"\t"+sapId+"\t"+sClass+"\t"+div+"\t"+address+"\t"+mobNumber+"\n\n");
                String password = sheet.getRow(i).getCell(passwordIndex).toString();
                data.append(name+"\t"+sapId+"\n");
                RegisterForm registerForm = new RegisterForm();
                registerForm.signUpUsingEmail(name,email,sapId,sClass,div,address,mobNumber,password,false,true);
            }
        }


    }
    private void getDataFromExel(String isbn,int noOfBooks){
        loadData(isbn,false,noOfBooks);
    }

}