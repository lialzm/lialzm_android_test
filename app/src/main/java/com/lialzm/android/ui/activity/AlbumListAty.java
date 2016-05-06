package com.lialzm.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lialzm.android.R;
import com.lialzm.android.adapter.AlbumListAdp;
import com.lialzm.android.util.ImageUtil;
import com.lialzm.android.entity.PhotoFolderInfo;

import java.util.List;

/**
 * 图片浏览器
 * Created by lcy on 2016/3/24.
 */
public class AlbumListAty extends AppCompatActivity {
    //图片数量
    private int photoSize = 0;
    private ListView lv_album;
    private static final int SUCCESS = 0;
    private AlbumListAdp albumAdp;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    List<PhotoFolderInfo> photoFolderInfos = (List<PhotoFolderInfo>) msg.obj;
                    albumAdp.addAllData(photoFolderInfos);
                    albumAdp.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        lv_album = (ListView) findViewById(R.id.lv_album);
        setSupportActionBar(toolbar);
        //设置返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //隐藏标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        albumAdp = new AlbumListAdp(this);
        lv_album.setAdapter(albumAdp);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PhotoFolderInfo> photoFolderInfos = ImageUtil.getAllPhotoFolder(AlbumListAty.this);
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = photoFolderInfos;
                handler.sendMessage(message);
            }
        }).start();
        lv_album.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoFolderInfo photoFolderInfo = (PhotoFolderInfo) albumAdp.getItem(position);
                Intent intent = new Intent();
                intent.setClass(AlbumListAty.this, AlbumGridAty.class);
                intent.putExtra("photoFolderInfo",photoFolderInfo);
                startActivity(intent);
            }
        });
    }

}