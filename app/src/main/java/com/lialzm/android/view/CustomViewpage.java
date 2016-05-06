package com.lialzm.android.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lialzm.android.R;
import com.lialzm.android.util.CommonUtil;
import com.lialzm.android.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 带小圆点的viewpager
 * Created by lcy on 2016/3/10.
 */
public class CustomViewpage extends RelativeLayout {

    private Context context;
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private BannerAdapter bannerAdapter;

    private final Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (linearLayout.getChildCount() != 0) {
                viewPager.setCurrentItem(msg.what);
            }
            super.handleMessage(msg);
        }
    };

    public CustomViewpage(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomViewpage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        viewPager = new ViewPager(getContext());
        bannerAdapter = new BannerAdapter();
        viewPager.setAdapter(bannerAdapter);
        addView(viewPager);
        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(linearLayout, layoutParams);

        //手动滑动和自动滑动的冲突问题
        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        LogUtil.d("ACTION_DOWN");
                        timer.cancel();
                        task.cancel();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        LogUtil.d("ACTION_MOVE");
                        timer.cancel();
                        task.cancel();
                        break;
                    case MotionEvent.ACTION_UP:
                        LogUtil.d("ACTION_UP");
                        timer = new Timer();
                        initTask();
                        timer.schedule(task, 5 * 1000, 5 * 1000);
                        break;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("find", "onPageScrolled==" + position + "," + positionOffset + "," + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    if (i == position % linearLayout.getChildCount()) {
                        linearLayout.getChildAt(i).setEnabled(true);
                    } else {
                        linearLayout.getChildAt(i).setEnabled(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    Timer timer = new Timer();

    TimerTask task;

    /**
     * 初始化task
     */
    private void initTask() {
        task = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = viewPager.getCurrentItem() + 1;
                handler.sendMessage(message);
            }
        };
    }

    public void addImage(List<View> views) {
        bannerAdapter.addAllData(views);
        for (View view : views
                ) {
            View dot = new View(getContext());
            dot.setBackgroundResource(R.drawable.dot_bg_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.dp2px(context, 5), CommonUtil.dp2px(context, 5));
            params.leftMargin = 10;
            if (bannerAdapter.getCount() == 1) {
                dot.setEnabled(true);
            } else {
                dot.setEnabled(false);
            }
            dot.setLayoutParams(params);
            linearLayout.addView(dot);
        }
        LogUtil.d("addImage==" + bannerAdapter.getCount());
        //使刚开始就可以往左滑动
        viewPager.setCurrentItem(views.size() * 100);
        initTask();
        timer.schedule(task, 5 * 1000, 5 * 1000);
    }


    /**
     * ViewPager的适配器
     */
    private class BannerAdapter extends PagerAdapter {
        List<View> imageViewContainer = new ArrayList<>();

        public void addData(View view) {
            imageViewContainer.add(view);
            notifyDataSetChanged();
        }

        public void addAllData(List<View> views) {
            imageViewContainer.addAll(views);
            notifyDataSetChanged();
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.d("find", "destroyItem==" + position);
            container.removeView(imageViewContainer.get(position % imageViewContainer.size()));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d("find", "instantoniateItem==" + position + "," + (position % imageViewContainer.size()));

            View view = imageViewContainer.get(position % imageViewContainer.size());
            /*if (container.indexOfChild(view) != -1) {
                container.removeView(view);
            }*/
            // 为每一个page添加点击事件
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
//                    Toast.makeText(MainActivity.this, "Page 被点击了", Toast.LENGTH_SHORT).show();
                }

            });

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }


    public int getCurrentItem() {
        return viewPager.getCurrentItem();
    }

    public void setCurrentItem(int position) {
        viewPager.setCurrentItem(position);
    }

    public void setOnTouchListener(OnTouchListener l) {
        viewPager.setOnTouchListener(l);
    }
}