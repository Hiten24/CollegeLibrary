package com.example.onlinecollegelibrary;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.TextView;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

public class ReadExcelFile {
    Context context;
    Uri uri;
    public ReadExcelFile(Context context,Uri uri){
        this.context = context;
        this.uri = uri;
    }

    public HSSFSheet getSheet(){
        HSSFSheet sheet = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(inputStream);
            HSSFWorkbook workbook = new HSSFWorkbook(poifsFileSystem);
            sheet = workbook.getSheetAt(0);


            /*for (int i=0;i<noOfColumn;i++){
                String name = sheet.getRow(0).getCell(i).toString();
                if(TextUtils.equals(name,"isbn")){
                    isbnCellIndex = cnt;
                }else if(TextUtils.equals(name,"No of Books")){
                    noOfBooksCellIndex = cnt;
                }
                cnt++;*/


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sheet;
    }
    public int getCellIndex(HSSFSheet sheet,String value){
        int noOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
//        int noOfRow = sheet.getPhysicalNumberOfRows();
        int cnt = 0;
        int cellIndex = 0;
        for (int i=0;i<noOfColumn;i++){
            String header = sheet.getRow(0).getCell(i).toString();
            if(TextUtils.equals(header,value)){
                cellIndex = cnt;
            }
            cnt++;
        }
        return cellIndex;
    }
}
