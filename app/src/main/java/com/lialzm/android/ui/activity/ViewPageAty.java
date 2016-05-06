package com.lialzm.android.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.lialzm.android.R;
import com.lialzm.android.view.CustomViewpage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcy on 2016/3/15.
 */
public class ViewPageAty extends AppCompatActivity {

    CustomViewpage customViewpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpage);
        customViewpage = (CustomViewpage) findViewById(R.id.view_page);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.logo2);
        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.logo);
        ImageView imageView3 = new ImageView(this);
        imageView3.setImageResource(R.drawable.logo);
        ImageView imageView4 = new ImageView(this);
        imageView4.setImageResource(R.drawable.logo);
//        customViewpage.addImage(imageView);
//        customViewpage.addImage(imageView2);
//        customViewpage.addImage(imageView3);
//        customViewpage.addImage(imageView4);

        List<View> views = new ArrayList<>();
        views.add(imageView);
        views.add(imageView2);
        views.add(imageView3);
        views.add(imageView4);
        customViewpage.addImage(views);
    }
}
