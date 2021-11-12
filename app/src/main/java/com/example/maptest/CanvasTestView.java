package com.example.maptest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CanvasTestView extends View {

    private float xZahyou = 0;
    private float yZahyou = 0;

    private float mParentPosX = 0;
    private float mParentPosY = 0;

    public CanvasTestView(Context context) {
        super(context);
    }


    public CanvasTestView(Context context, float parentPosX, float parentPosY) {
        super(context);

        mParentPosX = parentPosX;
        mParentPosY = parentPosY;
    }

/*    @Override
    public boolean onTouchEvent(MotionEvent event) {

        xZahyou = event.getX();
        yZahyou = event.getY();

        Log.i("onTouchEvent", "xZahyou=" + xZahyou + " yZahyou=" + yZahyou);

        this.invalidate();

        return true;
    }*/

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
/*
        // 半径10の円を描画する
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.RED);

        canvas.drawCircle(xZahyou, yZahyou, 10, p);
*/

        Paint paint = new Paint();
        paint.setStrokeWidth(2f);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        Path path = new Path();
        // スタート地点を移動
        //path.moveTo(250, 250);
        path.moveTo(mParentPosX, mParentPosY);
        // 制御点1 X, 制御点1 Y, 制御点2 X, 制御点2Y, 終点X, 終点Y
        //path.cubicTo(450, 0, 500, 50, 500, 250);
        path.quadTo(mParentPosX, mParentPosY + 300f, mParentPosX + 200f, mParentPosY + 200f);
        //path.quadTo(100f, 800f, xZahyou, yZahyou);

        Log.i("onDraw", "mParentPosX=" + mParentPosX + " mParentPosY=" + mParentPosY);

        canvas.drawPath(path, paint);
    }
}