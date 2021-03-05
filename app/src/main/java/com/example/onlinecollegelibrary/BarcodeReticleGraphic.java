package com.example.onlinecollegelibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

public class BarcodeReticleGraphic extends /*GraphicOverlay.Graphic*/ View {
    private final Paint boxPaint;
    RectF boxRect;
    int radiusSize;

    public BarcodeReticleGraphic(Context context, AttributeSet attr) {
        super(context,attr);
        boxPaint = new Paint();
        boxPaint.setColor(Color.parseColor("#9AFFFFFF"));
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(4);

        Display display = context.getDisplay();
        Point size = new Point();
        display.getSize(size);

        float boxWidth = size.x * 80 / 100;
        float boxHeight = size.y *  20 / 100;

        float cx = size.x / 2;
        float cy = size.y / 2;
        boxRect = new RectF(cx - boxWidth / 2, cy - boxHeight / 2, cx + boxWidth / 2, cy + boxHeight / 2);

        radiusSize = 12;
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawRoundRect(boxRect,radiusSize,radiusSize,boxPaint);
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
        int desiredHeight = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        width = widthSize;
        height = heightSize;
        Log.d("mywidth","width : "+width +" Height : "+height);
        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }
        setMeasuredDimension(width, height);
    }*/
}
