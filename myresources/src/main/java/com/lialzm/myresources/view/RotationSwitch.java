package com.lialzm.myresources.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.lialzm.myresources.R;

import java.util.logging.Logger;

/**
 * 旋转开关
 * 1.开始位置
 * 2.最大可旋转角度
 * 3.旋转角度监听
 * Created by apple on 16/6/19.
 */
public class RotationSwitch extends View {

    private Bitmap thumbBitmap;
    private Paint paint = new Paint();
    private boolean isTouchState = false; //触摸状态
    private float currentPosition; // 当前开关位置
    private int maxPosition; // 开关滑动最大位置


    public RotationSwitch(Context context) {
        super(context);
        initBitmap();
    }

    public RotationSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBitmap();
        Log.d("find", "第一象限1==" + angle(50, 50, 100, 100));
        Log.d("find", "第一象限2==" + angle(60, 40, 100, 100));
        Log.d("find", "第二象限1==" + angle(150, 50, 100, 100));
        Log.d("find", "第二象限2==" + angle(170, 60, 100, 100));
        Log.d("find", "第三象限1==" + angle(50, 150, 100, 100));
        Log.d("find", "第三象限2==" + angle(40, 130, 100, 100));
        Log.d("find", "第四象限1==" + angle(150, 150, 100, 100));
        Log.d("find", "第四象限2==" + angle(140, 170, 100, 100));
    }

    private void initBitmap() {
        thumbBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rotation_thumb);
    }

    /**
     * 测量view
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(thumbBitmap.getWidth(), thumbBitmap.getHeight());
//        setMeasuredDimension(dp2px(100), dp2px(100));
    }

    private int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    //变换图形
    private Matrix matrix = new Matrix();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(thumbBitmap, matrix, paint);
    }

    float startx = 0;//开始转动的点
    float starty = 0;
    float initialX;
    float initialY;

    /**
     * 处理手势
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //获取按钮中心位置
//        int[] location = new int[2];
//        this.getLocationOnScreen(location);
//        int x = location[0];
//        int y = location[1];
        int left = getLeft();
        int top = getTop();
        //圆心
        float centerX =getWidth() / 2.0f;
        float centerY = getHeight() / 2.0f;
        //半径
        float radius = getHeight() / 2.0f;
        Log.d("find", "left==" + left + ",right==" + getRight() + ",top==" + getTop() + ",bottom==" + getBottom());
        Log.d("find", "centerX==" + centerX + ",centerY==" + centerY);

        float angle = 0f;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startx = event.getX();
                starty = event.getY();
                initialX = event.getX();
                initialY = event.getY();
                Log.d("find", "startx1==" + startx + ",starty1==" + starty);
                break;
            //难点:转动的方向,角度
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                float c = (float) Math.sqrt(Math.pow(Math.abs(endY - starty), 2) + Math.pow(Math.abs(endX - startx), 2));
                angle = (float) (Math.acos((Math.pow(radius, 2) + Math.pow(radius, 2) - Math.pow(c, 2)) / (2.0f * radius * radius)));
                angle = (float) (angle * (180 / Math.PI));

                if (!Float.isNaN(angle)) {
                    Log.d("find", "startx==" + startx + ",starty==" + starty);
                    Log.d("find", "endX==" + endX + ",endY==" + endY);
                    Log.d("find", "angle(endX, endY, centerX, centerY)==" + angle(endX, endY, centerX, centerY));
                    Log.d("find", "angle(startx, starty, centerX, centerY)==" + angle(startx, starty, centerX, centerY));
                    if (angle(endX, endY, centerX, centerY) < angle(startx, starty, centerX, centerY)) {//顺时针
                        matrix.postRotate(angle, getWidth() / 2.0f, getHeight() / 2.0f);
                    } else {
                        matrix.postRotate(-angle, getWidth() / 2.0f, getHeight() / 2.0f);
                    }
                    invalidate();
                }
                startx = endX;
                starty = endY;
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return true;
    }

    private float angle(float x1, float y1, float centerX, float centerY) {
        return (float) Math.atan2(-y1 + centerY, x1 - centerX);
    }


}
