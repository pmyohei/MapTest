package com.example.maptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector      mGes;
    private float                mScreenScaleX;
    private float                mScreenScaleY;

    private TextView mKyotoNode;
    private TextView mCenterNode;

    private float testScaleX;
    private float testScaleY;
    private float preMoveScaleX;
    private float preMoveScaleY;

    public static float testExScaleX;
    public static float testExScaleY;

    private float mPreX = 0;
    private float mPreY = 0;

    private float mPinchPosDiffX = 0;
    private float mPinchPosDiffY = 0;

    private float mPinchStartDiffX = 1;
    private float mPinchStartDiffY = 1;
    private float mPinchEndDiffX = 1;
    private float mPinchEndDiffY = 1;
    private boolean mPinchStart = true;

    DragViewListener mDragViewListener;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout root = findViewById(R.id.root);

        testScaleX = 1.0f;
        testScaleY = 1.0f;
        preMoveScaleX = 1.0f;
        preMoveScaleY = 1.0f;

        mPinchPosDiffX = 0;
        mPinchPosDiffY = 0;

        float scaleX = root.getScaleX();
        float scaleY = root.getScaleY();

        Log.i("Scale", "?????? getScaleX=" + scaleX + " getScaleY=" + scaleY);

        mScreenScaleX = scaleX;
        mScreenScaleY = scaleY;

        mGes                  = new GestureDetector(this, new MoveListener(this));
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        //Fragment???????????????????????????????????????????????????GestureDetector???????????????????????????
/*
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleGestureDetector.onTouchEvent(event);
                mGes.onTouchEvent(event);

                return false;
            }
        });
*/




        ViewGroup vg_root = findViewById(R.id.root);
        //ViewGroup.LayoutParams params = vg_root.getLayoutParams();

//        CanvasTestView touchView = new CanvasTestView(this);

        TextView tv_center = findViewById(R.id.tv_center);

        mCenterNode = tv_center;

/*
        ViewGroup.LayoutParams lp = tv_center.getLayoutParams();

        int[] location = new int[2];
        tv_center.getLocationInWindow(location);

        Log.i("attach", "getTranslationX=" + tv_center.getTranslationX());
        Log.i("attach", "getLocationInWindow x=" + location[0]);
        Log.i("attach", "getLocationInWindow Y=" + location[1]);

        int[] lo_location = new int[2];
        tv_center.getLocationOnScreen(lo_location);
        Log.i("attach", "getLocationOnScreen x=" + lo_location[0]);
        Log.i("attach", "getLocationOnScreen Y=" + lo_location[1]);

*/

        ViewTreeObserver observer = tv_center.getViewTreeObserver();
        observer.addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout()
            {
                Log.i("attach2", "getTranslationX=" + tv_center.getTranslationX());

                int[] location = new int[2];
                tv_center.getLocationInWindow(location);

                Log.i("attach2", "???????????? getTranslationX=" + tv_center.getTranslationX());
                Log.i("attach2", "???????????? getLocationInWindow x=" + location[0]);
                Log.i("attach2", "???????????? getLocationInWindow Y=" + location[1]);

                int[] lo_location = new int[2];
                tv_center.getLocationOnScreen(lo_location);
                Log.i("attach2", "???????????? getLocationOnScreen x=" + lo_location[0]);
                Log.i("attach2", "???????????? getLocationOnScreen Y=" + lo_location[1]);

                Log.i("attach2", "???????????? getLeft=" + tv_center.getLeft());
                Log.i("attach2", "???????????? getTop=" + tv_center.getTop());

                int width = tv_center.getWidth();

                int x = tv_center.getLeft() + ( tv_center.getWidth() / 2 );
                int y = tv_center.getTop() + ( tv_center.getHeight() / 2 );

                Log.i("attach2", "???????????????????????????(?????????????????????????????????) x=" + x + " y=" + y);
                Log.i("attach2", "?????????????????????(?????????????????????????????????) getLeft=" + tv_center.getLeft() + " getTop=" + tv_center.getTop());

                //??????????????????----------------------------------------
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ConstraintLayout node = (ConstraintLayout) inflater.inflate(R.layout.node, vg_root, false);
                //ConstraintLayout node = (ConstraintLayout) inflater.inflate(R.layout.node_test, vg_troot, false);

                ViewGroup.MarginLayoutParams mlp2 = (ViewGroup.MarginLayoutParams) node.getLayoutParams();
                mlp2.setMargins(x + 20, y + 400, mlp2.rightMargin, mlp2.bottomMargin);
                //mlp2.setMargins(10, 10, mlp2.rightMargin, mlp2.bottomMargin);

                Log.i("mlp2", "rightMargin=" + mlp2.rightMargin + " bottomMargin=" + mlp2.bottomMargin);

                vg_root.addView(node);

                //??????????????????----------------------------------------
                CoordinatorLayout node2 = (CoordinatorLayout) inflater.inflate(R.layout.node_fab, vg_root, false);

                ViewGroup.MarginLayoutParams mlp3 = (ViewGroup.MarginLayoutParams) node2.getLayoutParams();
                mlp3.setMargins(x, y - 600, mlp3.rightMargin, mlp3.bottomMargin);
                //mlp2.setMargins(10, 10, mlp2.rightMargin, mlp2.bottomMargin);

                Log.i("mlp2", "rightMargin=" + mlp3.rightMargin + " bottomMargin=" + mlp3.bottomMargin);

                vg_root.addView(node2);


                //??????????????????----------------------------------------
                TextView childNode = new TextView(tv_center.getContext());
                childNode.setText( "??????" );

                mKyotoNode = childNode;

                vg_root.addView(childNode, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                ViewTreeObserver observer = childNode.getViewTreeObserver();
                observer.addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        int[] location = new int[2];
                        childNode.getLocationInWindow(location);

                        Log.i("childNode", "?????? getLocationInWindow x=" + location[0]);
                        Log.i("childNode", "?????? getLocationInWindow Y=" + location[1]);
                        Log.i("childNode", "getWidth=" + childNode.getWidth());

                        int x = childNode.getLeft() + ( childNode.getWidth() / 2 );
                        int y = childNode.getTop()  + ( childNode.getHeight() / 2 );

                        Log.i("childNode", "?????????????????????(?????????????????????????????????) x=" + x + " y=" + y);

                        Log.i("childNode", "???????????????(?????????????????????????????????) getLeft=" + childNode.getLeft());
                        Log.i("childNode", "???????????????(?????????????????????????????????) getTop=" + childNode.getTop());
                    }
                });

                int tox = x + width * 2;
                int toy = y + width * 2;

                Log.i("width * 2", "width * 2=" + (width * 2));

                DisplayMetrics metrics = getResources().getDisplayMetrics();

                float marginAddpx = (width * 2) * metrics.density;
                float marginAdddp = (width * 2) / metrics.density;

                Log.i("width", "marginAddpx=" + marginAddpx);
                Log.i("width", "marginAdddp=" + marginAdddp);

                Log.i("scroll", "tox=" + tox);
                Log.i("scroll", "toy=" + toy);

                //??????????????????????????????
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) childNode.getLayoutParams();
                mlp.setMargins(tox, toy, mlp.rightMargin, mlp.bottomMargin);

                Log.i("mlp", "rightMargin=" + mlp.rightMargin + " bottomMargin=" + mlp.bottomMargin);

                childNode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.i("childNode", "click2");
/*
                        FrameLayout root = findViewById(R.id.root);

                        Log.i("scroll", "root.getTranslationX()=" + root.getTranslationX());
                        Log.i("scroll", "root.getTranslationY()=" + root.getTranslationY());
                        Log.i("scroll", "v.getTranslationX()=" + v.getTranslationX());
                        Log.i("scroll", "v.getTranslationY()=" + v.getTranslationY());

                        int distance = width * 2;

                        Log.i("scroll", "root????????? ?????????(Translation) x=" + (root.getTranslationX() - distance));
                        Log.i("scroll", "root????????? ?????????(Translation) y=" + (root.getTranslationY() - distance));

                        DisplayMetrics metrics = getResources().getDisplayMetrics();

                        float toPX = distance * metrics.density;
                        float toDP = distance / metrics.density;

                        Log.i("scroll", "toPX*=" + toPX);
                        Log.i("scroll", "toDP/=" + toDP);

                        float diffx = tox - root.getTranslationX();
                        float diffy = toy - root.getTranslationY();

                        root.setTranslationX( root.getTranslationX() - distance );
                        root.setTranslationY( root.getTranslationY() - distance );
*/

/*
                        TranslateAnimation translateAnimation = new TranslateAnimation(
                                Animation.ABSOLUTE, root.getTranslationX(),
                                Animation.ABSOLUTE, root.getTranslationX() - distance,
                                Animation.ABSOLUTE, root.getTranslationY(),
                                Animation.ABSOLUTE, root.getTranslationY() - distance);

                        //animation?????? msec
                        translateAnimation.setDuration(500);
                        //animation??????????????????????????????????????????
                        translateAnimation.setFillAfter(true);

                        //?????????????????????????????????
                        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation animation) {

                                Log.i("scroll", "root.getTranslationX() after=" + root.getTranslationX());
                                Log.i("scroll", "root.getTranslationY() after=" + root.getTranslationY());
                            }

                            @Override
                            public void onAnimationStart(Animation animation) { }
                            @Override
                            public void onAnimationRepeat(Animation animation) { }
                        });

                        //??????????????????????????????
                        root.startAnimation(translateAnimation);
                        */
                    }
                });

/*                childNode.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //?????????????????????????????????????????????
                        return true;
                    }
                });*/

/*
                childNode.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.i("childNode", "onTouch");

                        //?????????????????????????????????????????????
                        return false;
                    }
                });
*/


                //????????????----------------------------------------
                CanvasTestView pathView = new CanvasTestView(tv_center.getContext(), x, y, x + width * 2, y + width * 2);
                vg_root.addView(pathView);

                //?????????????????????
                mDragViewListener = new DragViewListener( childNode, pathView );
                childNode.setOnTouchListener( mDragViewListener );

/*
                childNode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        Log.i("onFocusChange", "hasFocus=" + hasFocus);
                    }
                });
*/


                //??????????????????----------------------------------------
                TextView hNode = new TextView(tv_center.getContext());
                hNode.setText( "?????????" );

                vg_root.addView(hNode, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                observer = hNode.getViewTreeObserver();
                observer.addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        int[] location = new int[2];
                        hNode.getLocationInWindow(location);

                        Log.i("hNode", "getLocationInWindow x=" + location[0]);
                        Log.i("hNode", "getLocationInWindow Y=" + location[1]);
                        Log.i("hNode", "getWidth=" + hNode.getWidth());
                    }
                });

                int htox = x + width * 4;
                int htoy = y - width * 2;

                //??????????????????????????????
                mlp = (ViewGroup.MarginLayoutParams) hNode.getLayoutParams();
                mlp.setMargins(htox, htoy, mlp.rightMargin, mlp.bottomMargin);


                //??????????????????----------------------------------------
                TextView oNode = new TextView(tv_center.getContext());
                oNode.setText( "??????" );

                //API??????
                Drawable drawable;
                if (Build.VERSION.SDK_INT >= 23) {
                    drawable = AppCompatResources.getDrawable(tv_center.getContext(), R.drawable.circle);
                } else {
                    drawable = DrawableCompat.wrap( AppCompatResources.getDrawable(tv_center.getContext(), R.drawable.circle) );
                }
                //Padding??????
                oNode.setPadding(
                        oNode.getPaddingLeft() + 10,
                        oNode.getPaddingTop() + 10,
                        oNode.getPaddingRight() + 10,
                        oNode.getPaddingBottom() + 10
                );

                //?????????????????????????????????
                oNode.setBackground(drawable);

                vg_root.addView(oNode, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                observer = oNode.getViewTreeObserver();
                observer.addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        int[] location = new int[2];
                        oNode.getLocationInWindow(location);

                        Log.i("oNode", "getLocationInWindow x=" + location[0]);
                        Log.i("oNode", "getLocationInWindow Y=" + location[1]);
                        Log.i("oNode", "getWidth=" + oNode.getWidth());
                    }
                });

                htox = x - width * 8;
                htoy = y + width * 2;

                //??????????????????????????????
                mlp = (ViewGroup.MarginLayoutParams) oNode.getLayoutParams();
                mlp.setMargins(htox, htoy, mlp.rightMargin, mlp.bottomMargin);


                //??????????????????----------------------------------------
                TextView moveNode = new TextView(tv_center.getContext());
                moveNode.setText( "??????????????????" );

                vg_root.addView(moveNode, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                int movex = x;
                int movey = y + width * 3;

                //??????????????????????????????
                mlp = (ViewGroup.MarginLayoutParams) moveNode.getLayoutParams();
                mlp.setMargins(movex, movey, mlp.rightMargin, mlp.bottomMargin);

                moveNode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //movep

                        FrameLayout root = findViewById(R.id.root);

                        //???????????????????????????
                        float scrollStartX = root.getTranslationX() + mPinchPosDiffX;
                        float scrollStartY = root.getTranslationY() + mPinchPosDiffY;

                        Log.i("move", "??????????????????????????? x=" + scrollStartX + " y=" + scrollStartY);

                        //?????????root???Translation??????????????????????????????????????????
                        float rootPosX = scrollStartX / testScaleX;
                        float rootPosY = scrollStartY / testScaleY;

                        Log.i("move", "?????????root???Translation?????? testScaleX=" + testScaleX + " x=" + rootPosX + " y=" + rootPosY);
                        Log.i("move", "?????????center????????????????????? left=" + tv_center.getLeft() + " top=" + tv_center.getTop());

                        //?????????????????????????????????1?????????????????????????????????
                        mPinchPosDiffX = 0;
                        mPinchPosDiffY = 0;

                        //?????????Translation???????????????????????????????????????????????????=???????????????????????????????????????????????????????????????????????????
                        float rootMarginX = tv_center.getLeft() - rootPosX;
                        float rootMarginY = tv_center.getTop() - rootPosY;

                        //??????????????????????????????????????????
                        int toLeft = hNode.getLeft();
                        int toTop  = hNode.getTop();

                        Log.i("move", "Left(?????????)=" + toLeft + " Left(????????????)=" + tv_center.getLeft());
                        Log.i("move", "rootMarginX=" + rootMarginX + " rootMarginY=" + rootMarginY);

                        //????????????????????????????????????
                        //int MarginDiffX = toLeft - (int)rootMarginX;
                        //int MarginDiffY = toTop  - (int)rootMarginY;
//
                        //Log.i("move", "????????? MarginDiffX=" + MarginDiffX + " MarginDiffY=" + MarginDiffY);

                        //???????????????????????????????????????
                        float MarginPinchDiffX = (int)(testScaleX * (toLeft - rootMarginX));
                        float MarginPinchDiffY = (int)(testScaleY * (toTop  - rootMarginY));

                        //???????????????
                        //root.setTranslationX( rootPosX - MarginDiffX );
                        //root.setTranslationY( rootPosY - MarginDiffY );

                        Log.i("move", "?????????(?????????????????? ???????????? float) MarginPinchDiffX=" + MarginPinchDiffX + " MarginPinchDiffY=" + MarginPinchDiffY);

                        preMoveScaleX = testScaleX;
                        preMoveScaleY = testScaleY;

                        //??????????????????
                        final int MOVE_DURATION = 500;

                        Scroller scroller = new Scroller(v.getContext(), new DecelerateInterpolator());

                        // ??????????????????????????????
                        scroller.startScroll(
                                (int)scrollStartX,
                                (int)scrollStartY,
                                (int)-MarginPinchDiffX,
                                (int)-MarginPinchDiffY,
                                MOVE_DURATION       // ????????????????????????????????? [milliseconds]
                        );


                        ValueAnimator scrollAnimator = ValueAnimator.ofFloat(0, 1).setDuration(MOVE_DURATION);
                        scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                                Log.i("Scroller", "onAnimationUpdate");

                                if (!scroller.isFinished()) {
                                    scroller.computeScrollOffset();
                                    //setPieRotation(scroller.getCurrY());

                                    root.setTranslationX( scroller.getCurrX() );
                                    root.setTranslationY( scroller.getCurrY() );

                                } else {
                                    scrollAnimator.cancel();
                                    //onScrollFinished();

                                    //jikken
                                    //root.setScaleX(preScale);
                                    //root.setScaleY(preScale);
                                    //
                                }
                            }
                        });
                        scrollAnimator.start();


/*                        Handler handler = new Handler();
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                if( scroller.computeScrollOffset() ){
                                    float currX = scroller.getCurrX();
                                    float currY = scroller.getCurrY();


                                }

                                //invalidate();

*//*                                if(!scroller.isFinished()){
                                    handler.postDelayed(this, ANIMATION_INTERVAL);
                                }*//*
                            }
                        });*/

                    }
                });

                //??????????????????----------------------------------------
                TextView moveONode = new TextView(tv_center.getContext());
                moveONode.setText( "???????????????" );

                vg_root.addView(moveONode, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                movex = x;
                movey = y - width * 3;

                //??????????????????????????????
                mlp = (ViewGroup.MarginLayoutParams) moveONode.getLayoutParams();
                mlp.setMargins(movex, movey, mlp.rightMargin, mlp.bottomMargin);

                moveONode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, GarallyActivity.class);
                        startActivity(intent);


/*                        FrameLayout root = findViewById(R.id.root);

                        //?????????root???Translation??????
                        float nowx = root.getTranslationX();
                        float nowy = root.getTranslationY();

                        Log.i("move", "?????????root???Translation?????? x=" + nowx + " y=" + nowy);

                        //?????????Translation???????????????????????????????????????????????????
                        float nowMarginx = 4000 - nowx;
                        float nowMarginy = 4000 - nowy;

                        Log.i("move", "?????????Translation??????????????????????????????????????????????????? x=" + nowMarginx + " y=" + nowMarginy);

                        //??????????????????????????????????????????
                        int toleft = oNode.getLeft();
                        int totop  = oNode.getTop();

                        Log.i("move", "???????????????????????????????????????????????? toleft=" + toleft + " totop=" + totop);

                        //?????????
                        //int distancex = (int) Math.abs( toleft - nowMarginx );
                        //int distancey = (int) Math.abs( totop  - nowMarginy );
                        int distancex = toleft - (int)nowMarginx;
                        int distancey = totop  - (int)nowMarginy;

                        Log.i("move", "????????? distancex=" + distancex + " distancey=" + distancey);

                        root.setTranslationX( nowx - distancex );
                        root.setTranslationY( nowy - distancey );*/
                    }
                });


                //--??????
                //findViewById(R.id.tv_center).setVisibility( View.INVISIBLE );
                //findViewById(R.id.tv_center).setVisibility( View.VISIBLE );
                //findViewById(R.id.tv_center).invalidate();
                //--

                //??????????????????----------------------------------------
                TextView chohuku = new TextView(tv_center.getContext());
                chohuku.setText( "??????" );

                //API??????
                if (Build.VERSION.SDK_INT >= 23) {
                    drawable = AppCompatResources.getDrawable(tv_center.getContext(), R.drawable.circle);
                } else {
                    drawable = DrawableCompat.wrap( AppCompatResources.getDrawable(tv_center.getContext(), R.drawable.circle) );
                }
                //Padding??????
                chohuku.setPadding(
                        chohuku.getPaddingLeft() + 10,
                        chohuku.getPaddingTop() + 10,
                        chohuku.getPaddingRight() + 10,
                        chohuku.getPaddingBottom() + 10
                );

                //?????????????????????????????????
                chohuku.setBackground(drawable);

                vg_root.addView(chohuku, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                //??????????????????????????????
                mlp = (ViewGroup.MarginLayoutParams) chohuku.getLayoutParams();
                mlp.setMargins(x + 20, y + 40, mlp.rightMargin, mlp.bottomMargin);


                //test.setLeft( x + width * 2 );
                //test.setTop( y + width * 2 );


                tv_center.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

/*        //??????????????????----------------------------------------
        TextView aaaa = new TextView(tv_center.getContext());
        aaaa.setText( "testtest" );

        aaaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("scroll", "click");
            }
        });

        vg_root.addView(aaaa);*/

/*
        CanvasTestView touchView = new CanvasTestView(tv_center.getContext(), 435, 1128);
        vg_troot.addView(touchView);
*/


/*        touchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "check");
            }
        });*/

        //???????????????????????????
        //root.setTranslationX(100.0f);
        //root.setTranslationY(200.0f);
    }


    //??????????????????????????????????????????????????????
    private class MoveListener extends GestureDetector.SimpleOnGestureListener {

        private boolean mIsXSeq = true;
        private boolean mIsYSeq = true;
        private boolean mPreXCode = true;
        private boolean mPreYCode = true;

        private Scroller mScroller;

        public MoveListener( Context context ){
            mScroller = new Scroller(context, new DecelerateInterpolator());
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            //?????????????????????????????????
            mScroller.forceFinished(true);

            Log.i("onFling", "velocityX=" + velocityX + " velocityY=" + velocityY);

            int SCALE = 2;

//            mScroller.fling(currentX, currentY, velocityX / SCALE, velocityY / SCALE, minX, minY, maxX, maxY);
//            postInvalidate();

            FrameLayout root = findViewById(R.id.root);

            float nowx = root.getTranslationX();
            float nowy = root.getTranslationY();

            //Log.i("onFling", "nowx=" + nowx + " nowy=" + nowy);

            //??????????????????
            final int MOVE_DURATION = 1000;

            // ??????????????????????????????
            mScroller.fling(
                    (int)nowx,                          // scroll ??????????????? (X)
                    (int)nowy,                          // scroll ??????????????? (Y)
                    (int)velocityX / SCALE,     //??????
                    (int)velocityY / SCALE,     //??????
                    -2000,
                    2000,
                    -2000,
                    2000
            );

            ValueAnimator scrollAnimator = ValueAnimator.ofFloat(0, 1).setDuration(5000);
            scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    if (!mScroller.isFinished()) {
                        mScroller.computeScrollOffset();
                        //setPieRotation(scroller.getCurrY());

                        root.setTranslationX( mScroller.getCurrX() );
                        root.setTranslationY( mScroller.getCurrY() );

                    } else {
                        scrollAnimator.cancel();
                        //onScrollFinished();
                    }
                }
            });
            scrollAnimator.start();

            return false;
        }



/*
        @Override
        public void onLongPress(MotionEvent e) {
        }
*/


/*
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            FrameLayout root = findViewById(R.id.root);

            //Log.i("onScroll", "----------------------------");

            Log.i("root", "before getTranslationX=" + root.getTranslationX());

*/
/*            int diffX = 0;
            int diffY = 0;*//*

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
*/
/*
            root.setTranslationX(x - distanceX);
            root.setTranslationY(y - distanceY);
*//*


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
*/


    }

    //????????????????????????????????????????????????????????????????????????
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        float mRootScaleX;
        float mRootScaleY;

        float preDiffX;
        float preDiffY;

        float preX;
        float preY;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            Log.i("onScale", "onScaleBegin");

            FrameLayout root = findViewById(R.id.root);

            mRootScaleX = root.getScaleX();
            mRootScaleY = root.getScaleY();

            int[] location = new int[2];
            int[] location2 = new int[2];
            mCenterNode.getLocationInWindow(location);
            mKyotoNode.getLocationInWindow(location2);

            preDiffX = location2[0] - location[0];
            preDiffY = location2[1] - location[1];

            preX = location[0];
            preY = location[1];

            if( mPinchStart ){
                mPinchStartDiffX = preDiffX;
                mPinchStartDiffY = preDiffY;
                mPinchStart = false;
            }

            return super.onScaleBegin(detector);
            //return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            FrameLayout root = findViewById(R.id.root);

            float scaleFactor = detector.getScaleFactor();

            float scaleX = root.getScaleX();
            float scaleY = root.getScaleY();

            Log.i("onScale", "getScaleFactor=" + scaleFactor);
            Log.i("onScale", "????????? getScaleX=" + scaleX + " getScaleY=" + scaleY);

            root.setScaleX( mRootScaleX * scaleFactor );
            root.setScaleY( mRootScaleY * scaleFactor );

            Log.i("onScale", "????????? getScaleX=" + scaleX + " getScaleY=" + scaleY);

            // ??????getScaleFactor()???????????????????????????????????????????????????????????????
            // ?????????????????????????????????????????????
            //mActiveScale *= (mScaleGestureDetector.getScaleFactor() -1f)/20f +1f;

     /*       // ?????????????????????????????????????????????????????????????????????
            mActiveScale = Math.max(mMinimumScale, Math.min(mActiveScale, mMaximumScale));

            // ?????????View?????????????????????????????????????????????????????????????????????????????????????????????
            new FrameInLayout().update();

            // ImageView???Matrix??????????????????
            new UpdateMatrix().update();
*/
            return super.onScale(detector);
            //return true;
        }


        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

            int[] location = new int[2];
            mCenterNode.getLocationInWindow(location);

            Log.i("onScaleEnd", "????????????(??????????????????) getTranslationX=" + mCenterNode.getTranslationX() + " getTranslationY=" + mCenterNode.getTranslationY());
            Log.i("onScaleEnd", "????????????(??????????????????) getLocationInWindow x=" + location[0]);
            Log.i("onScaleEnd", "????????????(??????????????????) getLocationInWindow Y=" + location[1]);

            int[] location2 = new int[2];
            mKyotoNode.getLocationInWindow(location2);

            Log.i("onScaleEnd", "??????(??????????????????) getTranslationX=" + mKyotoNode.getTranslationX() + " getTranslationY=" + mKyotoNode.getTranslationY());
            Log.i("onScaleEnd", "??????(??????????????????) getLocationInWindow x=" + location2[0]);
            Log.i("onScaleEnd", "??????(??????????????????) getLocationInWindow Y=" + location2[1]);


            float diffX = location2[0] - location[0];
            float diffY = location2[1] - location[1];

            mPinchEndDiffX = diffX;
            mPinchEndDiffY = diffY;

            //?????????(??????)
            testScaleX *= (diffX / preDiffX);
            testScaleY *= (diffY / preDiffY);

            Log.i("onScaleEnd", "testScaleX=" + testScaleX + " testScaleY=" + testScaleY);

            mDragViewListener.setTestScaleX( testScaleX );
            mDragViewListener.setTestScaleY( testScaleY );

            //????????????????????????????????????????????????
            //?????????????????????????????????????????????????????????????????????
            mPinchPosDiffX += (preX - location[0]);
            mPinchPosDiffY += (preY - location[1]);

            Log.i("Pinch", "mPinchDiffX=" + mPinchPosDiffX + " mPinchDiffY=" + mPinchPosDiffY);

            super.onScaleEnd(detector);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {


        Log.i("onTouchEvent", "findPointerIndex (0)=" + motionEvent.findPointerIndex (0) + " findPointerIndex (1)=" + motionEvent.findPointerIndex (1));
        Log.i("onTouchEvent", "getPointerCount=" + motionEvent.getPointerCount());
        //Log.i("onScale", "getPointerId(0)=" + motionEvent.getActionIndex(motionEvent));

        mScaleGestureDetector.onTouchEvent(motionEvent);

        //??????????????????????????????
        //????????????????????????????????????????????????????????????????????????????????????
        if( motionEvent.getPointerCount() > 1 ){
            return true;
        }

        mGes.onTouchEvent(motionEvent);

        FrameLayout root = findViewById(R.id.root);

        Log.i("onTouchEvent", "????????????????????? TranslationX=" + root.getTranslationX() + " TranslationY=" + root.getTranslationY());


        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mPreX = motionEvent.getX();
                mPreY = motionEvent.getY();

                break;

            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_MOVE:

                float diffx = motionEvent.getX() - mPreX;
                float diffy = motionEvent.getY() - mPreY;

                Log.i("MotionEvent", "preX=" + mPreX + " preY=" + mPreY);
                Log.i("MotionEvent", "getX=" + motionEvent.getX() + " getY=" + motionEvent.getY());
                Log.i("MotionEvent", "diffX=" + diffx + " diffY=" + diffy);

                float x = root.getTranslationX();
                float y = root.getTranslationY();

                //??????????????????????????????
                root.setTranslationX(x + diffx);
                root.setTranslationY(y + diffy);

                mPreX = motionEvent.getX();
                mPreY = motionEvent.getY();

                break;

            case MotionEvent.ACTION_CANCEL:
                // something to do
                break;
        }

        return false;
    }

}