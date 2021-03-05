package com.example.onlinecollegelibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class CameraSourcePreview {

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private Activity BarcodeScannerActivity;
    String barcodeData;
    private BottomSheetBehavior bottomSheetBehavior;
    TextView barcodeDataFeild;
    public String initialiseDetectorAndSources(Context context, SurfaceView surfaceView){

        Toast.makeText(context,"Barcode Scanner Started",Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(context,barcodeDetector)
                .setRequestedPreviewSize(1920,1080)
                .setFacing(cameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(15.0f)
                .setAutoFocusEnabled(true)
                .build();



        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(surfaceView.getHolder());
                    }else {
//                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION);
                        ActivityCompat.requestPermissions(BarcodeScannerActivity, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
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
                Toast.makeText(context, "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                if (detections.detectorIsOperational()){
                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                    Frame.Metadata meta = detections.getFrameMetadata();
                    float width = meta.getWidth();
                    float height = meta.getHeight();

                    float boxWidth = width * 80 / 100;
                    float boxHeight = height *  20 / 100;

                    if(barcodes.size() != 0) {
                        int tempId = barcodes.keyAt(0);
                        Barcode barcode = barcodes.get(tempId);
                        float ractLeft = width/2 - boxWidth / 2;
                        float ractRight = width/2 + boxWidth / 2;
                        float ractTop = height/2 - boxHeight / 2;
                        float ractBottom = height/2 + boxHeight / 2;

                        float barcodeLeft = barcode.getBoundingBox().left;
                        float barcodeRight = barcode.getBoundingBox().right;
                        float barcodeTop = barcode.getBoundingBox().top;
                        float barcodeBottom = barcode.getBoundingBox().bottom;

                        if (barcodeLeft > ractLeft && barcodeRight < ractRight && barcodeTop > ractTop && barcodeBottom < ractBottom) {
//                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                                        barcodeData = barcodes.valueAt(0).displayValue;
                                        Log.d("Barcode Value",barcodeData);
//                                        barcodeDataFeild.setText(barcodeData);

                        }else {
                            Log.d("Barcode Invalid","Put barcode in rectangle");
                        }
                    }
                }else {
                    Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return barcodeData;
    }
    /*public String getIsbnNumber(){
        return barcodeData;
    }*/
    public void releaseCamera(){
        cameraSource.release();
    }

}
