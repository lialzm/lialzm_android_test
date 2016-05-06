package com.lialzm.android.ui.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lialzm.android.R;
import com.lialzm.android.util.LogUtil;
import com.lialzm.android.view.CustomViewpage2;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

/**
 * 图片预览
 * Created by lcy on 2016/3/24.
 */
public class PreviewAty extends AppCompatActivity {
    private CustomViewpage2 customViewpage;
    //图片数量
    private int photoSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        //设置返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //隐藏标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        customViewpage = (CustomViewpage2) findViewById(R.id.view_page);
        final String[] photos = getIntent().getStringArrayExtra("photo");
        final int position = getIntent().getIntExtra("position", 0);
        customViewpage = (CustomViewpage2) findViewById(R.id.view_page);

        for (int i = 0; i < photos.length; i++) {
            String photo = photos[i];
            if (TextUtils.isEmpty(photo)) {
                continue;
            }
            if (!photo.contains("http://")) {
                photo = Uri.fromFile(new File(photo)).toString();
            }
            photoSize++;
            final ImageView imageView = new ImageView(this);
            ImageLoader.getInstance().displayImage(photo, imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.setImageBitmap(loadedImage);
                    customViewpage.addImage(imageView);
                    LogUtil.d("onLoadingComplete==" + imageUri + "," + photos[photos.length - 1]);
                    customViewpage.setCurrent(position);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
        customViewpage.setOnItemClickLister(new CustomViewpage2.OnItemClickLister() {
            @Override
            public void onItemClick(View v) {
                toggleImmersiveMode();
            }
        });
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (View.SYSTEM_UI_FLAG_VISIBLE == visibility) {//此处需要添加顶部和底部消失和出现的动画效果
                    toolbar.startAnimation(AnimationUtils.loadAnimation(PreviewAty.this, R.anim.top_enter_anim));
                } else {
                    toolbar.startAnimation(AnimationUtils.loadAnimation(PreviewAty.this, R.anim.top_exit_anim));
                }
            }
        });
        toolbar_title.setText(String.valueOf((position + 1) + "/" + photoSize));
        customViewpage.setViewPagerOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolbar_title.setText(String.valueOf((position + 1) + "/" + photoSize));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 切换沉浸栏模式（Immersive - Mode）
     */
    private void toggleImmersiveMode() {
        if (Build.VERSION.SDK_INT >= 11) {
            int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
            // Navigation bar hiding:  Backwards compatible to ICS.
            if (Build.VERSION.SDK_INT >= 14) {
                uiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            // Status bar hiding: Backwards compatible to Jellybean
            if (Build.VERSION.SDK_INT >= 16) {
                uiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
            }
            // Immersive mode: Backward compatible to KitKat.
            if (Build.VERSION.SDK_INT >= 18) {
                uiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

}
