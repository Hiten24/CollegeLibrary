 package com.example.onlinecollegelibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

 public class BarcodeGraphicBase extends View {
     private final Paint boxPaint;
     private final Paint scrimPaint;
     private final Paint eraserPaint;

     final int boxCornerRadius;
     final Paint pathPaint;
     Paint scanPaint;
     RectF boxRect;

     public BarcodeGraphicBase(Context context, AttributeSet attr) {
         super(context,attr);

         scanPaint = new Paint();
         scanPaint.setColor(Color.RED);
         scanPaint.setStyle(Paint.Style.STROKE);
         scanPaint.setStrokeWidth(4);

         boxPaint = new Paint();
         boxPaint.setColor(Color.parseColor("#40000000"));
         boxPaint.setStyle(Paint.Style.STROKE);
         boxPaint.setStrokeWidth(4);

         scrimPaint = new Paint();
         scrimPaint.setColor(Color.parseColor("#99000000"));
         eraserPaint = new Paint();
         eraserPaint.setStrokeWidth(boxPaint.getStrokeWidth());
         eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

         boxCornerRadius = 12;

         pathPaint = new Paint();
         pathPaint.setColor(Color.rgb(255,255,255));
         pathPaint.setStyle(Paint.Style.STROKE);
         pathPaint.setStrokeWidth(boxPaint.getStrokeWidth());
         pathPaint.setPathEffect(new CornerPathEffect(boxCornerRadius));

         Display display = context.getDisplay();
         Point size = new Point();
         display.getSize(size);

         float boxWidth = size.x * 80 / 100;
         float boxHeight = size.y *  20 / 100;

         float cx = size.x / 2;
         float cy = size.y / 2;
         boxRect = new RectF(cx - boxWidth / 2, cy - boxHeight / 2, cx + boxWidth / 2, cy + boxHeight / 2);
     }

     /*public BarcodeGraphicBase(Context context) {
         super(context);
         boxPaint = new Paint();
         boxPaint.setColor(Color.parseColor("#40000000"));
         boxPaint.setStyle(Paint.Style.STROKE);
         boxPaint.setStrokeWidth(4);

         scrimPaint = new Paint();
         scrimPaint.setColor(Color.parseColor("#99000000"));
         eraserPaint = new Paint();
         eraserPaint.setStrokeWidth(boxPaint.getStrokeWidth());
         eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

         boxCornerRadius = 8;

         pathPaint = new Paint();
         pathPaint.setColor(Color.rgb(255,255,255));
         pathPaint.setStyle(Paint.Style.STROKE);
         pathPaint.setStrokeWidth(boxPaint.getStrokeWidth());
         pathPaint.setPathEffect(new CornerPathEffect(boxCornerRadius));

         boxRect =new RectF(100,200,100,200);
     }*/

     /*@Override
     protected void onDraw(Canvas canvas) {
         super.onDraw(canvas);
         canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),scrimPaint);

         eraserPaint.setStyle(Paint.Style.FILL);
         canvas.drawRoundRect(new RectF(50,50,canvas.getWidth()/2,canvas.getHeight()/2),boxCornerRadius,boxCornerRadius,eraserPaint);
         eraserPaint.setStyle(Paint.Style.STROKE);
         canvas.drawRoundRect(new RectF(50,50,canvas.getWidth()/2,canvas.getHeight()/2),boxCornerRadius,boxCornerRadius,eraserPaint);

         canvas.drawRoundRect(new RectF(50,50,canvas.getWidth()/2,canvas.getHeight()/2),boxCornerRadius,boxCornerRadius,boxPaint);
     }*/

     @Override
     protected void onDraw(Canvas canvas) {


         canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),scrimPaint);

         eraserPaint.setStyle(Paint.Style.FILL);
         canvas.drawRoundRect(boxRect,boxCornerRadius,boxCornerRadius,eraserPaint);
         eraserPaint.setStyle(Paint.Style.STROKE);
         canvas.drawRoundRect(boxRect,boxCornerRadius,boxCornerRadius,eraserPaint);

         canvas.drawRoundRect(boxRect,boxCornerRadius,boxCornerRadius,boxPaint);
     }

     /*protected void onSizeChanged(int w, int h, int oldw, int oldh) {
         super.onSizeChanged(w, h, oldw, oldh);

         boxRect = PreferenceUtil.getBarcodeReticleBox(w,h);

 //        boxRect = new RectF(width/3,height - height/4,w - width/3,height + height/4);
     }*/
     /*@Override
     protected void draw(Canvas canvas) {
         canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),scrimPaint);

         eraserPaint.setStyle(Paint.Style.FILL);
         canvas.drawRoundRect(boxRect,boxCornerRadius,boxCornerRadius,eraserPaint);
         eraserPaint.setStyle(Paint.Style.STROKE);
         canvas.drawRoundRect(boxRect,boxCornerRadius,boxCornerRadius,eraserPaint);

         canvas.drawRoundRect(boxRect,boxCornerRadius,boxCornerRadius,boxPaint);
     }*/


     /*public BarcodeGraphicBase(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
         super(context, attrs, defStyleAttr);
     }

     public BarcodeGraphicBase(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
         super(context, attrs, defStyleAttr, defStyleRes);
     }*/


 }
