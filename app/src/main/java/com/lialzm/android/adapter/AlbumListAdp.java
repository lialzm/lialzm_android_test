package com.lialzm.android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lialzm.android.R;
import com.lialzm.android.entity.PhotoFolderInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcy on 2016/4/1.
 */
public class AlbumListAdp extends BaseAdapter {

    private Context context;
    private List<PhotoFolderInfo> fileInfos = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private DisplayImageOptions options;

    public AlbumListAdp(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(false)
                .cacheInMemory(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.ic_gf_default_photo)
                .showImageOnFail(R.drawable.ic_gf_default_photo)
                .build();
    }

    public void addAllData(List<PhotoFolderInfo> fileInfos) {
        this.fileInfos.addAll(fileInfos);
    }

    @Override
    public int getCount() {
        return fileInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return fileInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_album_list, null);
            viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String photoPath = fileInfos.get(position).getCoverPhoto().getPhotoPath();

        ImageLoader.getInstance().displayImage(
                Uri.fromFile(new File(photoPath)).toString()
                , new ImageViewAware(viewHolder.iv_photo), options, new ImageSize(200, 200), null, null);

        viewHolder.tv_name.setText(fileInfos.get(position).getFolderName());
        return convertView;
    }


    class ViewHolder {
        ImageView iv_photo;
        TextView tv_name;
    }
}
