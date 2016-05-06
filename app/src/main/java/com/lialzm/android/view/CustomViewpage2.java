package com.lialzm.android.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lialzm.android.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片浏览器
 * Created by lcy on 2016/3/10.
 */
public class CustomViewpage2 extends RelativeLayout {

    private ViewPager viewPager;
    private BannerAdapter bannerAdapter;

    public CustomViewpage2(Context context) {
        super(context);
        init();
    }

    public CustomViewpage2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        viewPager = new ViewPager(getContext());
        bannerAdapter = new BannerAdapter();
        viewPager.setAdapter(bannerAdapter);
        addView(viewPager);


    }

    public void setViewPagerOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    /**
     * 指示器为文字
     *
     * @param view
     */
    public void addImage(View view) {
       /* view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("addImageonClick");

            }
        });*/
        bannerAdapter.addData(view);
        /*if (bannerAdapter.getCount() == 1) {

        } else {

        }*/
    }

    public void setCurrent(int position) {
        LogUtil.d("setCurrent==" + position);
        viewPager.setCurrentItem(position, true);
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
            container.removeView(imageViewContainer.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d("find", "instantiateItem==" + position);
            if (position == 1) {

            }
            View view = imageViewContainer.get(position);

            // 为每一个page添加点击事件
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (onItemClickLister != null) {
                        onItemClickLister.onItemClick(v);
                    }
                }

            });

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return imageViewContainer.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private OnItemClickLister onItemClickLister;

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    public interface OnItemClickLister {
        void onItemClick(View v);
    }


}
