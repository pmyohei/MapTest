package com.example.maptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ScaleGestureDetector mScaleGestureDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout root = findViewById(R.id.root);

        GestureDetector dec   = new GestureDetector(this, new MoveListener());
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        //Fragmentが受けたタッチイベントを全て２つのGestureDetectorに流してあげます。
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //mScaleGestureDetector.onTouchEvent(event);
                dec.onTouchEvent(event);
                return true;
            }
        });

        ViewGroup vg = findViewById(R.id.root);
        //ViewGroup.LayoutParams params = vg.getLayoutParams();

//        CanvasTestView touchView = new CanvasTestView(this);

        TextView tv_center = findViewById(R.id.tv_center);

        int[] location = new int[2];
        tv_center.getLocationInWindow(location);

        Log.i("attach", "getTranslationX=" + tv_center.getTranslationX());
        Log.i("attach", "getLocationInWindow x=" + location[0]);
        Log.i("attach", "getLocationInWindow Y=" + location[1]);

        int[] lo_location = new int[2];
        tv_center.getLocationOnScreen(lo_location);
        Log.i("attach", "getLocationOnScreen x=" + lo_location[0]);
        Log.i("attach", "getLocationOnScreen Y=" + lo_location[1]);


        ViewTreeObserver observer = tv_center.getViewTreeObserver();
        observer.addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout()
            {
                Log.i("attach2", "getTranslationX=" + tv_center.getTranslationX());

                int[] location = new int[2];
                tv_center.getLocationInWindow(location);

                Log.i("attach2", "getTranslationX=" + tv_center.getTranslationX());
                Log.i("attach2", "getLocationInWindow x=" + location[0]);
                Log.i("attach2", "getLocationInWindow Y=" + location[1]);

                int[] lo_location = new int[2];
                tv_center.getLocationOnScreen(lo_location);
                Log.i("attach2", "getLocationOnScreen x=" + lo_location[0]);
                Log.i("attach2", "getLocationOnScreen Y=" + lo_location[1]);

                Log.i("attach2", "getLeft=" + tv_center.getLeft());
                Log.i("attach2", "getTop=" + tv_center.getTop());

                int x = tv_center.getLeft() + ( tv_center.getWidth() / 2 );
                int y = tv_center.getTop() + ( tv_center.getHeight() / 2 );

                CanvasTestView touchView = new CanvasTestView(tv_center.getContext(), x, y);
                vg.addView(touchView);

                tv_center.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

/*
        CanvasTestView touchView = new CanvasTestView(tv_center.getContext(), 435, 1128);
        vg.addView(touchView);
*/


/*        touchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "check");
            }
        });*/

        //現在位置からの移動
        //root.setTranslationX(100.0f);
        //root.setTranslationY(200.0f);
    }


    //スワイプ操作が行われた際の処理です。
    private class MoveListener extends GestureDetector.SimpleOnGestureListener {

        private boolean mIsXSeq = true;
        private boolean mIsYSeq = true;
        private boolean mPreXCode = true;
        private boolean mPreYCode = true;

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            ConstraintLayout root = findViewById(R.id.root);

            Log.i("onScroll", "----------------------------");

            Log.i("root", "before getTranslationX=" + root.getTranslationX());

/*            int diffX = 0;
            int diffY = 0;*/
            float diffX = 0;
            float diffY = 0;

            if( mIsXSeq == (distanceX >= 0) ){
                //diffX = (int)(distanceX * 0.9f);
                //diffX = distanceX * 0.8f;

                if( Math.abs(distanceX) >= 1.0 ){
                    diffX = distanceX;
                }
            }

            if( mIsYSeq == (distanceY >= 0) ){
                //diffY = (int)(distanceY * 0.9f);
                //diffY = distanceY * 0.8f;
                //diffY = distanceY;

                if( Math.abs(distanceY) >= 1.0 ){
                    diffY = distanceY;
                }
            }

            float x = root.getTranslationX();
            float y = root.getTranslationY();

            root.setTranslationX(x - diffX);
            root.setTranslationY(y - diffY);
/*
            root.setTranslationX(x - distanceX);
            root.setTranslationY(y - distanceY);
*/

            Log.i("root", "after  getTranslationX=" + root.getTranslationX());
            Log.i("difftest", "distanceX=" + distanceX + " distanceY=" + distanceY);
            Log.i("difftest", "diffX=" + diffX + " diffY=" + diffY);

            if( mPreXCode == (distanceX >= 0) ){
                mIsXSeq = mPreXCode;
            }

            if( mPreYCode == (distanceY >= 0) ){
                mIsYSeq = mPreYCode;
            }

            mPreXCode = (distanceX >= 0);
            mPreYCode = (distanceY >= 0);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    //ピンチ（拡大・縮小）操作が行われた際の処理です。
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // 素のgetScaleFactor()の値を掛けてしまうと動きが大きすぎるため、
            // 緩やかにするため補正している。
            //mActiveScale *= (mScaleGestureDetector.getScaleFactor() -1f)/20f +1f;

     /*       // 拡大しすぎない、縮小しすぎないようにしている。
            mActiveScale = Math.max(mMinimumScale, Math.min(mActiveScale, mMaximumScale));

            // 画像がView外へ吹っ飛ぶとか、変な余白ができるので、強制的に位置調整する。
            new FrameInLayout().update();

            // ImageViewのMatrixを更新する。
            new UpdateMatrix().update();
*/
            return super.onScale(detector);
        }
    }

}