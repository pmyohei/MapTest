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

        Log.i("Scale", "初回 getScaleX=" + scaleX + " getScaleY=" + scaleY);

        mScreenScaleX = scaleX;
        mScreenScaleY = scaleY;

        mGes                  = new GestureDetector(this, new MoveListener(this));
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        //Fragmentが受けたタッチイベントを全て２つのGestureDetectorに流してあげます。
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

                Log.i("attach2", "センター getTranslationX=" + tv_center.getTranslationX());
                Log.i("attach2", "センター getLocationInWindow x=" + location[0]);
                Log.i("attach2", "センター getLocationInWindow Y=" + location[1]);

                int[] lo_location = new int[2];
                tv_center.getLocationOnScreen(lo_location);
                Log.i("attach2", "センター getLocationOnScreen x=" + lo_location[0]);
                Log.i("attach2", "センター getLocationOnScreen Y=" + lo_location[1]);

                Log.i("attach2", "センター getLeft=" + tv_center.getLeft());
                Log.i("attach2", "センター getTop=" + tv_center.getTop());

                int width = tv_center.getWidth();

                int x = tv_center.getLeft() + ( tv_center.getWidth() / 2 );
                int y = tv_center.getTop() + ( tv_center.getHeight() / 2 );

                Log.i("attach2", "センターの中心座標(親レイアウトのマージン) x=" + x + " y=" + y);
                Log.i("attach2", "センターの座標(親レイアウトのマージン) getLeft=" + tv_center.getLeft() + " getTop=" + tv_center.getTop());

                //ビューの生成----------------------------------------
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ConstraintLayout node = (ConstraintLayout) inflater.inflate(R.layout.node, vg_root, false);
                //ConstraintLayout node = (ConstraintLayout) inflater.inflate(R.layout.node_test, vg_troot, false);

                ViewGroup.MarginLayoutParams mlp2 = (ViewGroup.MarginLayoutParams) node.getLayoutParams();
                mlp2.setMargins(x + 20, y + 400, mlp2.rightMargin, mlp2.bottomMargin);
                //mlp2.setMargins(10, 10, mlp2.rightMargin, mlp2.bottomMargin);

                Log.i("mlp2", "rightMargin=" + mlp2.rightMargin + " bottomMargin=" + mlp2.bottomMargin);

                vg_root.addView(node);

                //ビューの生成----------------------------------------
                CoordinatorLayout node2 = (CoordinatorLayout) inflater.inflate(R.layout.node_fab, vg_root, false);

                ViewGroup.MarginLayoutParams mlp3 = (ViewGroup.MarginLayoutParams) node2.getLayoutParams();
                mlp3.setMargins(x, y - 600, mlp3.rightMargin, mlp3.bottomMargin);
                //mlp2.setMargins(10, 10, mlp2.rightMargin, mlp2.bottomMargin);

                Log.i("mlp2", "rightMargin=" + mlp3.rightMargin + " bottomMargin=" + mlp3.bottomMargin);

                vg_root.addView(node2);


                //ビューの生成----------------------------------------
                TextView childNode = new TextView(tv_center.getContext());
                childNode.setText( "京都" );

                mKyotoNode = childNode;

                vg_root.addView(childNode, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                ViewTreeObserver observer = childNode.getViewTreeObserver();
                observer.addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        int[] location = new int[2];
                        childNode.getLocationInWindow(location);

                        Log.i("childNode", "京都 getLocationInWindow x=" + location[0]);
                        Log.i("childNode", "京都 getLocationInWindow Y=" + location[1]);
                        Log.i("childNode", "getWidth=" + childNode.getWidth());

                        int x = childNode.getLeft() + ( childNode.getWidth() / 2 );
                        int y = childNode.getTop()  + ( childNode.getHeight() / 2 );

                        Log.i("childNode", "京都の中心座標(親レイアウトのマージン) x=" + x + " y=" + y);

                        Log.i("childNode", "京都の座標(親レイアウトのマージン) getLeft=" + childNode.getLeft());
                        Log.i("childNode", "京都の座標(親レイアウトのマージン) getTop=" + childNode.getTop());
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

                //レイアウトパラメータ
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

                        Log.i("scroll", "rootの座標 移動後(Translation) x=" + (root.getTranslationX() - distance));
                        Log.i("scroll", "rootの座標 移動後(Translation) y=" + (root.getTranslationY() - distance));

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

                        //animation時間 msec
                        translateAnimation.setDuration(500);
                        //animationが終わったそのまま表示にする
                        translateAnimation.setFillAfter(true);

                        //アニメーションリスナー
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

                        //アニメーションの開始
                        root.startAnimation(translateAnimation);
                        */
                    }
                });

/*                childNode.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //この後のクリックイベントはなし
                        return true;
                    }
                });*/

/*
                childNode.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.i("childNode", "onTouch");

                        //この後のクリックイベントはなし
                        return false;
                    }
                });
*/


                //線の描画----------------------------------------
                CanvasTestView pathView = new CanvasTestView(tv_center.getContext(), x, y, x + width * 2, y + width * 2);
                vg_root.addView(pathView);

                //タッチリスナー
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


                //ビューの生成----------------------------------------
                TextView hNode = new TextView(tv_center.getContext());
                hNode.setText( "北海道" );

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

                //レイアウトパラメータ
                mlp = (ViewGroup.MarginLayoutParams) hNode.getLayoutParams();
                mlp.setMargins(htox, htoy, mlp.rightMargin, mlp.bottomMargin);


                //ビューの生成----------------------------------------
                TextView oNode = new TextView(tv_center.getContext());
                oNode.setText( "沖縄" );

                //API対応
                Drawable drawable;
                if (Build.VERSION.SDK_INT >= 23) {
                    drawable = AppCompatResources.getDrawable(tv_center.getContext(), R.drawable.circle);
                } else {
                    drawable = DrawableCompat.wrap( AppCompatResources.getDrawable(tv_center.getContext(), R.drawable.circle) );
                }
                //Padding設定
                oNode.setPadding(
                        oNode.getPaddingLeft() + 10,
                        oNode.getPaddingTop() + 10,
                        oNode.getPaddingRight() + 10,
                        oNode.getPaddingBottom() + 10
                );

                //適用対象のビューを取得
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

                //レイアウトパラメータ
                mlp = (ViewGroup.MarginLayoutParams) oNode.getLayoutParams();
                mlp.setMargins(htox, htoy, mlp.rightMargin, mlp.bottomMargin);



                //ビューの生成----------------------------------------
                TextView moveNode = new TextView(tv_center.getContext());
                moveNode.setText( "北海道に移動" );

                vg_root.addView(moveNode, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                int movex = x;
                int movey = y + width * 3;

                //レイアウトパラメータ
                mlp = (ViewGroup.MarginLayoutParams) moveNode.getLayoutParams();
                mlp.setMargins(movex, movey, mlp.rightMargin, mlp.bottomMargin);

                moveNode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //movep

                        FrameLayout root = findViewById(R.id.root);

                        //スクロール開始位置
                        float scrollStartX = root.getTranslationX() + mPinchPosDiffX;
                        float scrollStartY = root.getTranslationY() + mPinchPosDiffY;

                        Log.i("move", "スクロール開始位置 x=" + scrollStartX + " y=" + scrollStartY);

                        //現在のrootのTranslation座標（ピンチの調整を無効化）
                        float rootPosX = scrollStartX / testScaleX;
                        float rootPosY = scrollStartY / testScaleY;

                        Log.i("move", "現在のrootのTranslation座標 testScaleX=" + testScaleX + " x=" + rootPosX + " y=" + rootPosY);

                        //ピンチ操作の差分反映は1度のみ。移動後はクリア
                        mPinchPosDiffX = 0;
                        mPinchPosDiffY = 0;

                        //現在のTranslation座標に対応する親レイアウトマージン=中心座標の親レイアウトマージン値（ピンチ考慮なし）
                        float rootMarginX = 4000 - rootPosX;
                        float rootMarginY = 4000 - rootPosY;

                        //移動先の親レイアウトマージン
                        int toLeft = hNode.getLeft();
                        int toTop  = hNode.getTop();

                        Log.i("move", "Left(移動先)=" + toLeft + " Left(センター)=" + tv_center.getLeft());
                        Log.i("move", "rootMarginX=" + rootMarginX + " rootMarginY=" + rootMarginY);

                        //移動量（ピンチ考慮なし）
                        int MarginDiffX = toLeft - (int)rootMarginX;
                        int MarginDiffY = toTop  - (int)rootMarginY;

                        Log.i("move", "移動量 MarginDiffX=" + MarginDiffX + " MarginDiffY=" + MarginDiffY);

                        //移動量：スケール比率を考慮
                        float MarginPinchDiffX = (int)(testScaleX * MarginDiffX);
                        float MarginPinchDiffY = (int)(testScaleY * MarginDiffY);

                        Log.i("move", "移動量(スケール考慮) MarginDiffX=" + MarginDiffX + " MarginDiffY=" + MarginDiffY);
                        Log.i("move", "移動後 TranslationX=" + ( rootPosX - MarginDiffX ) + " TranslationY=" + ( rootPosY - MarginDiffY ));

                        //位置を反映
                        //root.setTranslationX( rootPosX - MarginDiffX );
                        //root.setTranslationY( rootPosY - MarginDiffY );

                        Log.i("move", "移動量(スケール考慮 int) MarginDiffX=" + MarginDiffX * (int)root.getScaleX() + " MarginDiffY=" + MarginDiffY * (int)root.getScaleY());

                        float marginX = 4000 - root.getTranslationX();
                        float marginY = 4000 - root.getTranslationY();

                        Log.i("move", "移動量(スケール考慮 比率取得 float) MarginPinchDiffX=" + MarginPinchDiffX + " MarginPinchDiffY=" + MarginPinchDiffY);

                        preMoveScaleX = testScaleX;
                        preMoveScaleY = testScaleY;


                        //スクローラー
                        final int MOVE_DURATION = 500;

                        Scroller scroller = new Scroller(v.getContext(), new DecelerateInterpolator());

                        // アニメーションを開始
                        scroller.startScroll(
                                //(int)rootPosX,      // scroll の開始位置 (X)
                                //(int)rootPosY,      // scroll の開始位置 (Y)
                                (int)scrollStartX,
                                (int)scrollStartY,
                                //(int)root.getTranslationX(),
                                //(int)root.getTranslationY(),

                                //-MarginDiffX,      // 移動する距離、正の値だとコンテンツが左にスクロールする (X)
                                //-MarginDiffY,      // 移動する距離、正の値だとコンテンツが左にスクロールする (Y)
                                //(int)-MarginDiffFroatX,
                                //(int)-MarginDiffFroatY,
                                (int)-MarginPinchDiffX,
                                (int)-MarginPinchDiffY,

                                MOVE_DURATION       // スクロールにかかる時間 [milliseconds]
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

                //ビューの生成----------------------------------------
                TextView moveONode = new TextView(tv_center.getContext());
                moveONode.setText( "沖縄に移動" );

                vg_root.addView(moveONode, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                movex = x;
                movey = y - width * 3;

                //レイアウトパラメータ
                mlp = (ViewGroup.MarginLayoutParams) moveONode.getLayoutParams();
                mlp.setMargins(movex, movey, mlp.rightMargin, mlp.bottomMargin);

                moveONode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, GarallyActivity.class);
                        startActivity(intent);


/*                        FrameLayout root = findViewById(R.id.root);

                        //現在のrootのTranslation座標
                        float nowx = root.getTranslationX();
                        float nowy = root.getTranslationY();

                        Log.i("move", "現在のrootのTranslation座標 x=" + nowx + " y=" + nowy);

                        //現在のTranslation座標に対応する親レイアウトマージン
                        float nowMarginx = 4000 - nowx;
                        float nowMarginy = 4000 - nowy;

                        Log.i("move", "現在のTranslation座標に対応する親レイアウトマージン x=" + nowMarginx + " y=" + nowMarginy);

                        //移動先の親レイアウトマージン
                        int toleft = oNode.getLeft();
                        int totop  = oNode.getTop();

                        Log.i("move", "移動先（）の親レイアウトマージン toleft=" + toleft + " totop=" + totop);

                        //移動量
                        //int distancex = (int) Math.abs( toleft - nowMarginx );
                        //int distancey = (int) Math.abs( totop  - nowMarginy );
                        int distancex = toleft - (int)nowMarginx;
                        int distancey = totop  - (int)nowMarginy;

                        Log.i("move", "移動量 distancex=" + distancex + " distancey=" + distancey);

                        root.setTranslationX( nowx - distancex );
                        root.setTranslationY( nowy - distancey );*/
                    }
                });


                //--実験
                //findViewById(R.id.tv_center).setVisibility( View.INVISIBLE );
                //findViewById(R.id.tv_center).setVisibility( View.VISIBLE );
                //findViewById(R.id.tv_center).invalidate();
                //--

                //ビューの生成----------------------------------------
                TextView chohuku = new TextView(tv_center.getContext());
                chohuku.setText( "重複" );

                //API対応
                if (Build.VERSION.SDK_INT >= 23) {
                    drawable = AppCompatResources.getDrawable(tv_center.getContext(), R.drawable.circle);
                } else {
                    drawable = DrawableCompat.wrap( AppCompatResources.getDrawable(tv_center.getContext(), R.drawable.circle) );
                }
                //Padding設定
                chohuku.setPadding(
                        chohuku.getPaddingLeft() + 10,
                        chohuku.getPaddingTop() + 10,
                        chohuku.getPaddingRight() + 10,
                        chohuku.getPaddingBottom() + 10
                );

                //適用対象のビューを取得
                chohuku.setBackground(drawable);

                vg_root.addView(chohuku, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                //レイアウトパラメータ
                mlp = (ViewGroup.MarginLayoutParams) chohuku.getLayoutParams();
                mlp.setMargins(x + 20, y + 40, mlp.rightMargin, mlp.bottomMargin);


                //test.setLeft( x + width * 2 );
                //test.setTop( y + width * 2 );


                tv_center.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

/*        //ビューの生成----------------------------------------
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

        private Scroller mScroller;

        public MoveListener( Context context ){
            mScroller = new Scroller(context, new DecelerateInterpolator());
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            //現在のスクロールを停止
            mScroller.forceFinished(true);

            Log.i("onFling", "velocityX=" + velocityX + " velocityY=" + velocityY);

            int SCALE = 2;

//            mScroller.fling(currentX, currentY, velocityX / SCALE, velocityY / SCALE, minX, minY, maxX, maxY);
//            postInvalidate();

            FrameLayout root = findViewById(R.id.root);

            float nowx = root.getTranslationX();
            float nowy = root.getTranslationY();

            //Log.i("onFling", "nowx=" + nowx + " nowy=" + nowy);

            //スクローラー
            final int MOVE_DURATION = 1000;

            // アニメーションを開始
            mScroller.fling(
                    (int)nowx,                          // scroll の開始位置 (X)
                    (int)nowy,                          // scroll の開始位置 (Y)
                    (int)velocityX / SCALE,     //初速
                    (int)velocityY / SCALE,     //初速
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

    //ピンチ（拡大・縮小）操作が行われた際の処理です。
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
            Log.i("onScale", "設定前 getScaleX=" + scaleX + " getScaleY=" + scaleY);

            root.setScaleX( mRootScaleX * scaleFactor );
            root.setScaleY( mRootScaleY * scaleFactor );

            Log.i("onScale", "設定後 getScaleX=" + scaleX + " getScaleY=" + scaleY);

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
            //return true;
        }


        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

            int[] location = new int[2];
            mCenterNode.getLocationInWindow(location);

            Log.i("onScaleEnd", "センター(スケール変更) getTranslationX=" + mCenterNode.getTranslationX() + " getTranslationY=" + mCenterNode.getTranslationY());
            Log.i("onScaleEnd", "センター(スケール変更) getLocationInWindow x=" + location[0]);
            Log.i("onScaleEnd", "センター(スケール変更) getLocationInWindow Y=" + location[1]);

            int[] location2 = new int[2];
            mKyotoNode.getLocationInWindow(location2);

            Log.i("onScaleEnd", "京都(スケール変更) getTranslationX=" + mKyotoNode.getTranslationX() + " getTranslationY=" + mKyotoNode.getTranslationY());
            Log.i("onScaleEnd", "京都(スケール変更) getLocationInWindow x=" + location2[0]);
            Log.i("onScaleEnd", "京都(スケール変更) getLocationInWindow Y=" + location2[1]);


            float diffX = location2[0] - location[0];
            float diffY = location2[1] - location[1];

            mPinchEndDiffX = diffX;
            mPinchEndDiffY = diffY;

            //変化率(累積)
            testScaleX *= (diffX / preDiffX);
            testScaleY *= (diffY / preDiffY);

            Log.i("onScaleEnd", "testScaleX=" + testScaleX + " testScaleY=" + testScaleY);

            mDragViewListener.setTestScaleX( testScaleX );
            mDragViewListener.setTestScaleY( testScaleY );

            //ピンチ開始時との位置のズレを保持
            //※連続で操作される可能性があるため、累計させる
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

        //ピンチ操作なら、終了
        //※ここで終了させないと、以降の移動処理も実施されてしまう
        if( motionEvent.getPointerCount() > 1 ){
            return true;
        }

        mGes.onTouchEvent(motionEvent);

        FrameLayout root = findViewById(R.id.root);

        Log.i("onTouchEvent", "タッチした瞬間 TranslationX=" + root.getTranslationX() + " TranslationY=" + root.getTranslationY());


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

                //前回空の移動量を反映
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