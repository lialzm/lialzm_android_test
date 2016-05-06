package com.lialzm.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lialzm.android.R;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Settings;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_vp, btn_et, btn_flow, btn_album, btn_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.initialize(
                Settings.getInstance()
                        .isShowMethodLink(true)
                        .isShowThreadInfo(false)
                        .setMethodOffset(0)
                        .setLogPriority(true ? Log.VERBOSE : Log.ASSERT)
        );
        btn_vp = (Button) findViewById(R.id.btn_vp);
        btn_et = (Button) findViewById(R.id.btn_et);
        btn_flow = (Button) findViewById(R.id.btn_flow);
        btn_album = (Button) findViewById(R.id.btn_album);
        btn_location = (Button) findViewById(R.id.btn_location);
        btn_vp.setOnClickListener(this);
        btn_et.setOnClickListener(this);
        btn_flow.setOnClickListener(this);
        btn_album.setOnClickListener(this);
        btn_location.setOnClickListener(this);
      /*  UmengPush umengPush = new UmengPush(this, new UmengPush.IpushToken() {
            @Override
            public void push(String token) {
//TODO 处理token
            }
        });
        umengPush.init();
        umengPush.enable();*/
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_et:
                intent.setClass(this, EditTextAty.class);
                startActivity(intent);
                break;
            case R.id.btn_vp:
                intent.setClass(this, ViewPageAty.class);
                startActivity(intent);
                break;
            case R.id.btn_flow:
                intent.setClass(this, FlowImageAty.class);
                startActivity(intent);
                break;
            case R.id.btn_album:
                intent.setClass(this, AlbumListAty.class);
                startActivity(intent);
                break;
            case R.id.btn_location:
                intent.setClass(this, LocationAty.class);
                startActivity(intent);
               /* final GaodeLocation gaodeLocation = new GaodeLocation();
                gaodeLocation.init(MainActivity.this);
                gaodeLocation.startLocation();
                gaodeLocation.setOnLocationFinishListener(new GaodeLocation.OnLocationFinish() {
                    @Override
                    public void locationFinish(LocationInfo location) {

                        gaodeLocation.stopLocation();
                    }
                });*/
                break;
        }
    }
}
