package com.lialzm.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.lialzm.android.R;
import com.lialzm.android.adapter.AlbumGridAdp;
import com.lialzm.android.entity.PhotoFolderInfo;
import com.lialzm.android.entity.PhotoInfo;
import com.lialzm.android.ui.activity.base.BaseActivity;
import com.lialzm.android.util.CommonUtil;
import com.lialzm.android.util.ImageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lcy on 2016/4/1.
 */
public class AlbumGridAty extends BaseActivity {

    private GridView gridView;
    private AlbumGridAdp gridAdp;
    //记录checkbox
    private Map<Integer, PhotoInfo> mapSelect = new HashMap<>();

    private List<PhotoInfo> photoInfos = new ArrayList<>();

    private FloatingActionButton float_btn;
    List<PhotoInfo> infos;
    private static final int SUCCESS = 0;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    List<PhotoFolderInfo> photoFolderInfos = (List<PhotoFolderInfo>) msg.obj;
                    List<PhotoInfo> infos2 = photoFolderInfos.get(0).getPhotoList();
                    for (PhotoInfo p : infos2
                            ) {
                        if (infos==null){
                            break;
                        }
                        for (PhotoInfo i : infos
                                ) {
                            if (p.equals(i)) {
                                infos2.set(infos2.indexOf(p), i);
                            }
                        }
                    }
                    gridAdp.addAllData(infos2);
                    gridAdp.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_grid);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("全部图片");
        final TextView toolbar_right = (TextView) findViewById(R.id.toolbar_right);
        float_btn = (FloatingActionButton) findViewById(R.id.float_btn);
        toolbar_right.setText(getString(R.string.select_photo, 0, 9));
        setSupportActionBar(toolbar);
        //设置返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //隐藏标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        gridView = (GridView) findViewById(R.id.grid);
        PhotoFolderInfo photoFolderInfo = (PhotoFolderInfo) getIntent().getSerializableExtra("photoFolderInfo");
        if (photoFolderInfo != null) {
            infos = photoFolderInfo.getPhotoList();
        }


//        photoFolderInfo = (PhotoFolderInfo) getIntent().getSerializableExtra("photoFolderInfo");
//        photoInfos = photoFolderInfo.getPhotoList();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PhotoFolderInfo> photoFolderInfos = ImageUtil.getAllPhotoFolder(AlbumGridAty.this);
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = photoFolderInfos;
                handler.sendMessage(message);
            }
        }).start();
        gridAdp = new AlbumGridAdp(this, photoInfos, mapSelect);
        gridView.setAdapter(gridAdp);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoInfo photoInfo = (PhotoInfo) gridAdp.getItem(position);
                if (photoInfo.isCheck()) {
                    photoInfo.setIsCheck(false);
                    mapSelect.remove(position);
                } else {
                    if (gridAdp.getSelectCount() < 9) {
                        photoInfo.setIsCheck(true);
                        mapSelect.put(position, photoInfo);
                    } else {
                        CommonUtil.showToast(AlbumGridAty.this, "最多只能9张");
                    }
                }
                gridAdp.setData(position, photoInfo);
                gridAdp.notifyDataSetChanged();
                toolbar_right.setText(getString(R.string.select_photo, mapSelect.size(), 9));
            }
        });
        float_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                PhotoFolderInfo photoFolderInfo = new PhotoFolderInfo();
                photoFolderInfo.setPhotoList(getPhoto());
                intent.putExtra("photos", photoFolderInfo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 获取所有选中的图片
     *
     * @return
     */
    private List<PhotoInfo> getPhoto() {
        List<PhotoInfo> infos = new ArrayList<>();
        for (PhotoInfo photoInfo :
                gridAdp.getAllData()) {
            if (photoInfo.isCheck()) {
                infos.add(photoInfo);
            }
        }
        return infos;
    }
}
