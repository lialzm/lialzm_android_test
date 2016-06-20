package com.lialzm.myresources.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lialzm.myresources.R;

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

    public RotationSwitch(Context context) {
        super(context);
        initBitmap();
    }

    public RotationSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBitmap();
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
    }

    //变换图形
    private Matrix matrix = new Matrix();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(thumbBitmap, 0, 0, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float startx = 0;//开始转动的点
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            //难点:转动的方向
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);
    }
}
