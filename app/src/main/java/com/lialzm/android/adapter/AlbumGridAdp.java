package com.lialzm.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lialzm.android.R;
import com.lialzm.android.entity.PhotoInfo;
import com.lialzm.android.third.imageLoad.ImageLoaderWrapper;
import com.lialzm.android.third.imageLoad.UniversalAndroidImageLoader;
import com.lialzm.android.util.CommonUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by lcy on 2016/4/1.
 */
public class AlbumGridAdp extends BaseAdapter {

    private Context context;
    private List<PhotoInfo> photoInfos;
    private LayoutInflater layoutInflater;
    private DisplayImageOptions options;
    private DisplayMetrics displayMetrics;
    //记录checkbox
    private Map<Integer, PhotoInfo> mapSelect;
    private ImageLoaderWrapper imageLoaderWrapper;

    public AlbumGridAdp(Context context, List<PhotoInfo> photoInfos, Map<Integer, PhotoInfo> mapSelect) {
        this.context = context;
        this.mapSelect = mapSelect;
        layoutInflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(false)
                .cacheInMemory(false)
//                .showImageOnLoading(R.drawable.ic_gf_default_photo)
//                .showImageOnFail(R.drawable.ic_gf_default_photo)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        displayMetrics = CommonUtil.getScreenPix((Activity) context);
        this.photoInfos = photoInfos;
        imageLoaderWrapper = new UniversalAndroidImageLoader();
    }

    @Override
    public int getCount() {
        return photoInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return photoInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(int position, PhotoInfo photoInfo) {
        this.photoInfos.set(position, photoInfo);
//        notifyDataSetChanged();
    }

    public void addAllData(List<PhotoInfo> photoFolderInfos) {
        this.photoInfos.addAll(photoFolderInfos);
    }

    public List<PhotoInfo> getAllData(){
       return photoInfos;
    }


    public int getSelectCount() {
        return mapSelect.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_album_grid, null);
            viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
//            viewHolder.cb_select = (CheckBox) convertView.findViewById(R.id.cb_select);
            viewHolder.iv_check = (ImageView) convertView.findViewById(R.id.iv_check);
            setHeight(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PhotoInfo photoInfo = (PhotoInfo) getItem(position);

        String photoPath = "";
        if (photoInfo != null) {
            photoPath = photoInfo.getPhotoPath();
        }
//        viewHolder.iv_photo.setImageResource(R.drawable.ic_gf_default_photo);
        if (!photoPath.equals(viewHolder.iv_photo.getTag())) {
            viewHolder.iv_photo.setTag(photoPath);//存储图片地址防止重复加载
            imageLoaderWrapper.displayImage(viewHolder.iv_photo, new File(photoPath), null);
        }
//        ImageLoader.getInstance().displayImage(
//                Uri.fromFile(new File(photoPath)).toString()
//                , new ImageViewAware(viewHolder.iv_photo), options, new ImageSize(displayMetrics.widthPixels / 2, displayMetrics.heightPixels / 2), null, null);

//        viewHolder.iv_photo.setOnClickListener(ivPhotoClick);
      /*  viewHolder.cb_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("cb_select.setOnClickListener");
                    viewHolder.cb_select.setChecked(false);
            }
        });

        viewHolder.cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    LogUtil.d("onCheckedChanged==" + position + "," + isChecked);
                    PhotoInfo photoInfo1 = (PhotoInfo) getItem(position);
                    photoInfo1.setIsCheck(isChecked);
                    setData(position, photoInfo1);
                    photoInfo.setIsCheck(isChecked);
                    if (isChecked) {
                        mapSelect.put(position, photoInfo1);
                    } else {
                        mapSelect.remove(position);
                    }
                }
        });*/
       /* viewHolder.cb_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("mapSelect==" + mapSelect.size());
                Set<Map.Entry<Integer, Boolean>> set = mapSelect.entrySet();
                Iterator<Map.Entry<Integer, Boolean>> iterator = set.iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Boolean> entry = iterator.next();
                    LogUtil.d("Entry==" + entry.getKey() + "," + entry.getValue());
                }
                if (mapSelect.containsKey(position)) {
                    mapSelect.remove(position);
                    viewHolder.cb_select.setChecked(false);
                } else {
                    if (getSelectCount() >= 9) {
                        Toast.makeText(context, "你最多只能选择" + 9 + "张照片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mapSelect.put(position, true);
                    viewHolder.cb_select.setChecked(true);
                }
            }
        });*/


        if (photoInfo.isCheck()) {
            viewHolder.iv_check.setBackgroundColor(Color.rgb(0x3F, 0x51, 0xB5));
        } else {
            viewHolder.iv_check.setBackgroundColor(Color.rgb(0xd2, 0xd2, 0xd7));
        }

//        viewHolder.cb_select.setChecked(photoInfo.isCheck());
        return convertView;
    }

    private void setHeight(final View convertView) {
        int height = displayMetrics.widthPixels / 3 - 8;
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
    }

    public class ViewHolder {
        public ImageView iv_photo;
        public ImageView iv_check;

//        CheckBox cb_select;
    }
}
