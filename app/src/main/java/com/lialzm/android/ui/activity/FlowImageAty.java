package com.lialzm.android.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lialzm.android.R;
import com.lialzm.android.entity.PhotoFolderInfo;
import com.lialzm.android.entity.PhotoInfo;
import com.lialzm.android.util.ImageUtil;
import com.lialzm.android.util.LogUtil;
import com.lialzm.android.view.CustomFlowImage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lcy on 2016/3/22.
 */
public class FlowImageAty extends Activity {
    private CustomFlowImage custom_flow_image;
    private ImageView iv_get_image;
    private int i = 0;
    private PhotoFolderInfo photoFolderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        custom_flow_image = (CustomFlowImage) findViewById(R.id.custom_flow_image);
        View view = getLayoutInflater().inflate(R.layout.item_photo, custom_flow_image, false);
        ImageView iv_del_icon = (ImageView) view.findViewById(R.id.iv_del_icon);
        iv_del_icon.setVisibility(View.INVISIBLE);
        custom_flow_image.addView(view, "");
        custom_flow_image.setOnItemClickListener(new CustomFlowImage.OnItemClick() {
            @Override
            public void onClick(View v, int position) {
                Log.d("find", "onClick==" + v.getId() + "," + position);
                if (position == custom_flow_image.getChildCount() - 1 && custom_flow_image.isCanAddImage()) {
//                    ImageUtil.showPicturePicker(FlowImageAty.this, true, new String[]{getString(R.string.take_photos), getString(R.string.album)});
                    Intent intent = new Intent();
                    intent.putExtra("photoFolderInfo", photoFolderInfo);
                    intent.setClass(FlowImageAty.this, AlbumGridAty.class);
                    startActivityForResult(intent, 12);

                } else {
                    if (v.getId() == R.id.iv_del_icon) {
                        custom_flow_image.removeChild(position);
                    } else {
                        Intent intent = new Intent(FlowImageAty.this, PreviewAty.class);
                        intent.putExtra("photo", custom_flow_image.getPics());
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
                }

            }
        });


       /* iv_get_image = (ImageView) findViewById(R.id.iv_get_image);
        iv_get_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*View view;
                if (i == 0) {
                    view = getLayoutInflater().inflate(R.layout.item_photo, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.iv_get_image);
                    imageView.setImageResource(R.drawable.food1);
                    i++;
                } else {
                    view = getLayoutInflater().inflate(R.layout.item_photo, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.iv_get_image);
                    imageView.setImageResource(R.drawable.food2);
                }
                custom_flow_image.addView(view);*//*
            }
        });*/
    }

    Uri uri2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 选择图片结束
            case ImageUtil.CROP:
                if (RESULT_CANCELED == resultCode) {
                    break;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("outputX", 225);
                map.put("outputY", 225);
                map.put("crop", "true");
                map.put("aspectX", 1);
                map.put("scale", false);
                map.put("aspectY", 1);
                String dirPath = ImageUtil.getCacheFile(FlowImageAty.this).getAbsolutePath() + File.separator + "flowimage";
                LogUtil.d("dirPath==" + dirPath);
                File file = new File(dirPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                uri2 = ImageUtil.crop(FlowImageAty.this, data, map, dirPath + File.separator + System.currentTimeMillis() + ".jpg");

                break;
            // 剪裁图片结束
            case ImageUtil.CROP_PICTURE:
                if (RESULT_CANCELED == resultCode) {
                    break;
                }
                if (data == null) {
                    return;
                } else {
//                    Bitmap photo = null;
                    if (data == null) {
                        return;
                    } else {
//                        Uri photoUri = uri2;
//                        if (photoUri != null) {
//                            photo = BitmapFactory.decodeFile(photoUri.getPath());
//                        }
//                        if (photo == null) {
//                            return;
//                        }
                    }
                    LogUtil.d("url2==" + uri2);
                    Bitmap bitmap = ImageUtil.getImageThumbnail(uri2.getPath(), 225, 225);
                    View view = getLayoutInflater().inflate(R.layout.item_photo, custom_flow_image, false);

                    ImageView imageView = (ImageView) view.findViewById(R.id.iv_get_image);
                    imageView.setImageBitmap(bitmap);
                    custom_flow_image.addView(view, uri2.getPath());
                   /* String fileName = String.valueOf(System.currentTimeMillis());
                    File dirFile = new File(System.getProperty("java.io.tmpdir", ".") + "/FlowImage");
                    dirFile.mkdirs();
                    File tempFile = null;
                    try {
                        tempFile = File.createTempFile(fileName, ".png", dirFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageUtil.savePhotoToSDCard(photo, tempFile);
                    String newPicUrl = tempFile.getAbsolutePath();
                    LogUtil.d("newPicUrl==" + newPicUrl);
//                    Bitmap bitmap = BitmapFactory.decodeFile(newPicUrl, null);
                    Bitmap bitmap = ImageUtil.getImageThumbnail(newPicUrl, 225, 225);
                    View view = getLayoutInflater().inflate(R.layout.item_photo, custom_flow_image, false);
                  *//*  ImageUtil.savePhotoToSDCard(photo, ImageUtil.getCachePath(FlowImageAty.this), "update");
                    String newPicUrl = ImageUtil.getCachePath(FlowImageAty.this) + "/update.jpg";

//                    Bitmap bitmap = BitmapFactory.decodeFile(newPicUrl, null);
                    Bitmap bitmap = ImageUtil.getImageThumbnail(newPicUrl, 225, 225);
                    View view = getLayoutInflater().inflate(R.layout.item_photo, custom_flow_image, false);*//*

                    ImageView imageView = (ImageView) view.findViewById(R.id.iv_get_image);
                    imageView.setImageBitmap(bitmap);
                    custom_flow_image.addView(view, newPicUrl);*/


                }
                break;
            case 12:
                if (RESULT_CANCELED == resultCode) {
                    break;
                }
                photoFolderInfo = (PhotoFolderInfo) data.getSerializableExtra("photos");
                List<PhotoInfo> infos = photoFolderInfo.getPhotoList();
                for (PhotoInfo photoInfo : infos
                        ) {
                    View view = getLayoutInflater().inflate(R.layout.item_photo, custom_flow_image, false);
                    ImageView imageView = (ImageView) view.findViewById(R.id.iv_get_image);
                    Bitmap bitmap = ImageUtil.getImageThumbnail(photoInfo.getPhotoPath(), 225, 225);
                    imageView.setImageBitmap(bitmap);
                    custom_flow_image.addView(view, photoInfo.getPhotoPath());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清除所有图片
        String dirPath = ImageUtil.getCacheFile(this).getAbsolutePath() + File.separator + "flowimage";
        LogUtil.d("delete==" + dirPath);
        File file = new File(dirPath);
        ImageUtil.delete(file);
    }


}
