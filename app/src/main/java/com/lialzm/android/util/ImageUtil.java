package com.lialzm.android.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.lialzm.android.R;
import com.lialzm.android.entity.PhotoFolderInfo;
import com.lialzm.android.entity.PhotoInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lcy on 2016/3/23.
 */
public class ImageUtil {

    //拍照
    public static final int TAKE_PHOTO = 0x1001;
    //从相册选择
    public static final int CHOOSE_PICTURE = 0x1002;
    public static final int CROP = 0x1003;
    public static final int CROP_PICTURE = 0x1004;

    /**
     * 缓存统一存放此目录
     *
     * @return
     */
    public static File getCacheFile(Context context) {
        File file = new File(getCachePath(context) + File.separator + "com.lialzm.android" + File.separator);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 选择图片来源
     *
     * @param
     */
    public static void showPicturePicker(final Activity context, final boolean isCorp, String[] strings, final DialogDoThing... dialogDoThing) {
        if (strings.length == 2) {
            if (!context.getString(R.string.take_photos).equals(strings[0]) || !context.getString(R.string.album).equals(strings[1])) {
                new Throwable("String数组必须使用{拍照,相册}");
                return;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.image_src);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setItems(strings, new DialogInterface.OnClickListener() {
            //类型码
            int REQUEST_CODE;

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    //照相
                    case 0:
                        Uri imageUri = null;
                        String fileName = null;
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (isCorp) {
                            REQUEST_CODE = CROP;
                        } else {
                            REQUEST_CODE = TAKE_PHOTO;
                        }

                        //保存本次截图临时文件名字
                        fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                        String dirPath = ImageUtil.getCacheFile(context).getAbsolutePath() + File.separator + "flowimage";
                        File fileDir = new File(dirPath);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                        File file = new File(dirPath, fileName);
                        imageUri = Uri.fromFile(file);
                        LogUtil.d("imageUri==" + imageUri);
                        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        //保存输出地址
                        SharedPreferences sharedPreferences = context.getSharedPreferences("temp", Context.MODE_PRIVATE);
                        ImageUtil.deletePhotoAtPathAndName(sharedPreferences.getString("tempFilePath", ""));
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("tempFilePath", file.getAbsolutePath());
                        editor.commit();
                        context.startActivityForResult(openCameraIntent, REQUEST_CODE);
                        break;
                    //相册获取图片
                    case 1:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        if (isCorp) {
                            REQUEST_CODE = CROP;
                        } else {
                            REQUEST_CODE = CHOOSE_PICTURE;
                        }
                        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        context.startActivityForResult(openAlbumIntent, REQUEST_CODE);
                        break;
                    case 2:
                        if (dialogDoThing != null && dialogDoThing.length > 0) {
                            dialogDoThing[0].doThing();
                        }

                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    public interface DialogDoThing {
        void doThing();
    }

    public static void deletePhotoAtPathAndName(String path, String fileName) {
        if (checkSDCardAvailable()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i].getName());
                if (files[i].getName().equals(fileName)) {
                    files[i].delete();
                }
            }
        }
    }

    public static void deletePhotoAtPathAndName(String path) {
        if (checkSDCardAvailable()) {
            File folder = new File(path);
            folder.deleteOnExit();
        }
    }

    /**
     * 转换uri
     *
     * @param uri
     * @param activity
     * @return
     */
    @SuppressLint("NewApi")
    public static Uri imageuri(Uri uri, Context activity) {
//		if(android.os.Build.VERSION.SDK_INT<android.os.Build.VERSION_CODES.KITKAT){
//			return uri;
//		}
        if (uri.toString().toLowerCase().startsWith("file")) {
            return uri;
        }
        String path = "";
        boolean isDocumentUri = false;
        try {
            isDocumentUri = DocumentsContract.isDocumentUri(activity, uri);
        } catch (NoClassDefFoundError e) {
            isDocumentUri = false;
        }
        if (isDocumentUri) {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = activity.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
                    new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                path = cursor.getString(columnIndex);
            }
            cursor.close();
        } else {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(uri,
                    projection, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        }
        uri = Uri.fromFile(new File(path));
        return uri;
    }

    /**
     * 剪裁图片
     *
     * @param
     */
    public static Uri crop(Activity activity, Intent data, Map<String, Object> map, String outPath) {
        LogUtil.d("outPath==" + outPath);
        Uri uri = null;
        if (data != null) {
            uri = data.getData();
            if (uri == null) {
                return null;
            }
            uri = imageuri(uri, activity);
        } else {
            String tempFilePath = activity.getSharedPreferences("temp", Context.MODE_PRIVATE).getString("tempFilePath", "");
            uri = Uri.fromFile(new File(tempFilePath));
        }
        LogUtil.d("crop==" + uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            if (entry.getValue() instanceof String) {
                intent.putExtra(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                intent.putExtra(entry.getKey(), (Integer) entry.getValue());
            } else if (entry.getValue() instanceof Boolean) {
                intent.putExtra(entry.getKey(), (Boolean) entry.getValue());
            }
        }
       /* intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 300);
        intent.putExtra("outputFormat", "JPEG");*/
        File file = new File(outPath);
        Uri uri1 = Uri.fromFile(file);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("return-data", false);
        LogUtil.d("uri1==" + uri1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri1);
        activity.startActivityForResult(intent, CROP_PICTURE);
        return uri1;
    }

    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static void savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File photoFile = new File(path, photoName + ".jpg");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void savePhotoToSDCard(Bitmap photoBitmap, File photoFile) {
        if (checkSDCardAvailable()) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取缓存目录
     *
     * @return
     */
    public static String getCachePath(Context context) {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? Environment.getExternalStorageDirectory().getAbsolutePath() : context.getCacheDir().getAbsolutePath();
    }


    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     * 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    //递归删除文件及文件夹
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /**
     * 获取所有图片
     *
     * @param context
     * @return
     */
    public static List<PhotoFolderInfo> getAllPhotoFolder(Context context) {
        List<PhotoFolderInfo> allFolderList = new ArrayList<>();
        final String[] projectionPhotos = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Thumbnails.DATA
        };
        final ArrayList<PhotoFolderInfo> allPhotoFolderList = new ArrayList<>();
        HashMap<Integer, PhotoFolderInfo> bucketMap = new HashMap<>();
        Cursor cursor = null;
        //所有图片
        PhotoFolderInfo allPhotoFolderInfo = new PhotoFolderInfo();
        allPhotoFolderInfo.setFolderId(0);
        allPhotoFolderInfo.setFolderName(context.getResources().getString(R.string.all_photo));
        allPhotoFolderInfo.setPhotoList(new ArrayList<PhotoInfo>());
        allPhotoFolderList.add(0, allPhotoFolderInfo);
//        List<String> selectedList = GalleryFinal.getFunctionConfig().getSelectedList();
//        List<String> filterList = GalleryFinal.getFunctionConfig().getFilterList();
        try {
            cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , projectionPhotos, "", null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
            if (cursor != null) {
                int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                final int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
                while (cursor.moveToNext()) {
                    int bucketId = cursor.getInt(bucketIdColumn);
                    String bucketName = cursor.getString(bucketNameColumn);
                    final int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    final int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    //int thumbImageColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                    final int imageId = cursor.getInt(imageIdColumn);
                    final String path = cursor.getString(dataColumn);
                    //final String thumb = cursor.getString(thumbImageColumn);
                    File file = new File(path);
                    final PhotoInfo photoInfo = new PhotoInfo();
                    photoInfo.setPhotoId(imageId);
                    photoInfo.setPhotoPath(path);
                    //photoInfo.setThumbPath(thumb);
                    if (allPhotoFolderInfo.getCoverPhoto() == null) {
                        allPhotoFolderInfo.setCoverPhoto(photoInfo);
                    }
                    //添加到所有图片
                    allPhotoFolderInfo.getPhotoList().add(photoInfo);

                    //通过bucketId获取文件夹
                    PhotoFolderInfo photoFolderInfo = bucketMap.get(bucketId);

                    if (photoFolderInfo == null) {
                        photoFolderInfo = new PhotoFolderInfo();
                        photoFolderInfo.setPhotoList(new ArrayList<PhotoInfo>());
                        photoFolderInfo.setFolderId(bucketId);
                        LogUtil.d("bucketName=="+bucketName);
                        photoFolderInfo.setFolderName(bucketName);
                        photoFolderInfo.setCoverPhoto(photoInfo);
                        bucketMap.put(bucketId, photoFolderInfo);
                        allPhotoFolderList.add(photoFolderInfo);
                    }
                    photoFolderInfo.getPhotoList().add(photoInfo);

                }
            }
        } catch (Exception ex) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        allFolderList.addAll(allPhotoFolderList);
        return allFolderList;
    }

}
