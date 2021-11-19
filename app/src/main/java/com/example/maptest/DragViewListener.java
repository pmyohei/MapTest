package com.example.maptest;

import android.view.MotionEvent;
import android.view.View;

public class DragViewListener  implements View.OnTouchListener {

    // ドラッグ対象のView
    private View mDragNode;
    private CanvasTestView mParentLine;
    // ドラッグ中に移動量を取得するための変数
    private int oldx;
    private int oldy;

    public DragViewListener(View dragNode, CanvasTestView parentLine) {
        mDragNode   = dragNode;
        mParentLine = parentLine;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // タッチしている位置取得（スクリーン座標）
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:

                //Log.i("childNode", "x - oldx=" + (x - oldx));
                //Log.i("childNode", "getHistoricalX=" + event.getHistoricalX(0)); 落ちる

                // 今回イベントでのView移動先の位置
                int left = mDragNode.getLeft() + (x - oldx);
                int top  = mDragNode.getTop()  + (y - oldy);

                //ノードの移動
                mDragNode.layout(left, top, left + mDragNode.getWidth(), top + mDragNode.getHeight());

                //接続線の描画を更新
                float endPosx = left + mDragNode.getWidth() / 2;
                float endPosy = top  + mDragNode.getHeight() / 2;

                mParentLine.moveEndPos( endPosx, endPosy );

                // 今回のタッチ位置を保持
                oldx = x;
                oldy = y;

                //イベント処理完了
                return true;
        }

        //今回のタッチ位置を保持
        oldx = x;
        oldy = y;

        //イベント処理完了
        return false;
    }



}
