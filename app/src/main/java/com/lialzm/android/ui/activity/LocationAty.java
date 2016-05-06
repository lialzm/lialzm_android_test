package com.lialzm.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lialzm.android.R;
import com.lialzm.android.third.location.GaodeMapAty;
import com.lialzm.android.third.location.GaodeRouteAty;
import com.lialzm.android.ui.activity.base.BaseActivity;

/**
 * Created by lcy on 2016/4/14.
 */
public class LocationAty extends BaseActivity implements View.OnClickListener {

    private Button btn_gaode, btn_baidu, btn_gaode_route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        btn_baidu = (Button) findViewById(R.id.btn_baidu);
        btn_gaode = (Button) findViewById(R.id.btn_gaode);
        btn_gaode_route = (Button) findViewById(R.id.btn_gaode_route);

        btn_baidu.setOnClickListener(this);
        btn_gaode.setOnClickListener(this);
        btn_gaode_route.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_baidu:

                break;
            case R.id.btn_gaode:
                intent.setClass(LocationAty.this, GaodeMapAty.class);
                startActivity(intent);
                break;
            case R.id.btn_gaode_route:
                intent.setClass(LocationAty.this, GaodeRouteAty.class);
                startActivity(intent);
                break;
        }
    }
}
