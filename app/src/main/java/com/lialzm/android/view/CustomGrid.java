package com.lialzm.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lialzm.android.util.LogUtil;

/**
 * Created by lcy on 2016/4/5.
 */
public class CustomGrid extends GridView implements AdapterView.OnItemClickListener {

    public CustomGrid(Context context) {
        super(context);
        init();
    }

    public CustomGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnItemClickListener(this);
        for (int i=0;i<getChildCount();i++){
            View view=getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.d("onClick");
                }
            });
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return super.onTouchEvent(ev);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtil.d("onItemClick");
    }
}
