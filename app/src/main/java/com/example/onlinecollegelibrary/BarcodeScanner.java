package com.example.onlinecollegelibrary;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class BarcodeScanner extends AddBooks {

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    BottomSheetBehavior bottomSheetBehavior;
    String barcodeData,barcodeOldData;
    private Boolean firstDetection = true;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    SparseArray<Barcode> barcodes = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        ConstraintLayout constraintLayout = findViewById(R.id.add_book_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_DRAGGING){
                    firstDetection = true;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        surfaceView = findViewById(R.id.surfaceView);

        initialiseDetectorAndSources();
        if(barcodeData != null){
            updateBottomSheetDetails(barcodeData);
        }


    }

    private void initialiseDetectorAndSources(){

        Toast.makeText(this,"Barcode Scanner Started",Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(1920,1080)
                .setFacing(cameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(15.0f)
                .setAutoFocusEnabled(true)
                .build();



        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(surfaceView.getHolder());
                    }else {
//                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION);
                        ActivityCompat.requestPermissions(BarcodeScanner.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

//        BarcodeDetector bx = new BarcodeDetector(barcodeDetector,)

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                /*final SparseArray<Barcode> */
                barcodes = detections.getDetectedItems();
                if (barcodes != null) {
                    Frame.Metadata meta = detections.getFrameMetadata();
                    float width = meta.getWidth();
                    float height = meta.getHeight();

                    float boxWidth = width * 80 / 100;
                    float boxHeight = height * 20 / 100;

                    if (barcodes.size() != 0 && firstDetection) {

                        int tempId = barcodes.keyAt(0);
                        Barcode barcode = barcodes.get(tempId);
                        float ractLeft = width / 2 - boxWidth / 2;
                        float ractRight = width / 2 + boxWidth / 2;
                        float ractTop = height / 2 - boxHeight / 2;
                        float ractBottom = height / 2 + boxHeight / 2;

                        float barcodeLeft = barcode.getBoundingBox().left;
                        float barcodeRight = barcode.getBoundingBox().right;
                        float barcodeTop = barcode.getBoundingBox().top;
                        float barcodeBottom = barcode.getBoundingBox().bottom;

                        if (barcodeLeft > ractLeft && barcodeRight < ractRight && barcodeTop > ractTop && barcodeBottom < ractBottom) {
//
                            barcodeData = barcodes.valueAt(0).displayValue;
                            firstDetection = false;
                            updateBottomSheetDetails(barcodeData);
                            Log.d("Barcode Value", barcodeData);
//
                        } else {
                            Log.d("Barcode Invalid", "Put barcode in rectangle");
                        }
                    } else {
//                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                        Log.d("barcode", "Barcode Scanned");
                    }
                }
            }
        });

    }

    private void updateBottomSheetDetails(String isbnNumber){
//        if(barcodeOldData != barcodeData && barcodeData != null)
//            barcodeDetector.release();
            loadData(isbnNumber);
         /*   barcodeOldData = barcodeData;
        }*/

//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        getSupportActionBar().hide();
//        flashButton.setSelected(false);
//        updateFlashMode(false);
        cameraSource.release();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        getSupportActionBar().hide();
//        cameraSourcePreview.initialiseDetectorAndSources(this,surfaceView,barcodeDataFeild);
        initialiseDetectorAndSources();
    }


}