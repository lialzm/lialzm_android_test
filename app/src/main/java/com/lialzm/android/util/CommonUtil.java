package com.lialzm.android.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;

/**
 * Created by lcy on 2016/3/23.
 */
public class CommonUtil {

    public static void showDialog(Context context, String title, String[] items) {
        new AlertDialog.Builder(context).setTitle(title)
                .setItems(items, null)
                .setNegativeButton("取消", null).show();
    }

    /**
     * dp 转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取手机大小（分辨率）
     *
     * @param activity
     * @return
     */
    public static DisplayMetrics getScreenPix(Activity activity) {
        // DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        // 获取手机窗口的Display 来初始化DisplayMetrics 对象
        // getManager()获取显示定制窗口的管理器。
        // 获取默认显示Display对象
        // 通过Display 对象的数据来初始化一个DisplayMetrics 对象
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displaysMetrics);
        return displaysMetrics;
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void showerror(Context context, int rCode){
        try {
            switch (rCode) {
                //服务错误码
                case 1001:
                    throw new AMapException(AMapException.AMAP_SIGNATURE_ERROR);
                case 1002:
                    throw new AMapException(AMapException.AMAP_INVALID_USER_KEY);
                case 1003:
                    throw new AMapException(AMapException.AMAP_SERVICE_NOT_AVAILBALE);
                case 1004:
                    throw new AMapException(AMapException.AMAP_DAILY_QUERY_OVER_LIMIT);
                case 1005:
                    throw new AMapException(AMapException.AMAP_ACCESS_TOO_FREQUENT);
                case 1006:
                    throw new AMapException(AMapException.AMAP_INVALID_USER_IP);
                case 1007:
                    throw new AMapException(AMapException.AMAP_INVALID_USER_DOMAIN);
                case 1008:
                    throw new AMapException(AMapException.AMAP_INVALID_USER_SCODE);
                case 1009:
                    throw new AMapException(AMapException.AMAP_USERKEY_PLAT_NOMATCH);
                case 1010:
                    throw new AMapException(AMapException.AMAP_IP_QUERY_OVER_LIMIT);
                case 1011:
                    throw new AMapException(AMapException.AMAP_NOT_SUPPORT_HTTPS);
                case 1012:
                    throw new AMapException(AMapException.AMAP_INSUFFICIENT_PRIVILEGES);
                case 1013:
                    throw new AMapException(AMapException.AMAP_USER_KEY_RECYCLED);
                case 1100:
                    throw new AMapException(AMapException.AMAP_ENGINE_RESPONSE_ERROR);
                case 1101:
                    throw new AMapException(AMapException.AMAP_ENGINE_RESPONSE_DATA_ERROR);
                case 1102:
                    throw new AMapException(AMapException.AMAP_ENGINE_CONNECT_TIMEOUT);
                case 1103:
                    throw new AMapException(AMapException.AMAP_ENGINE_RETURN_TIMEOUT);
                case 1200:
                    throw new AMapException(AMapException.AMAP_SERVICE_INVALID_PARAMS);
                case 1201:
                    throw new AMapException(AMapException.AMAP_SERVICE_MISSING_REQUIRED_PARAMS);
                case 1202:
                    throw new AMapException(AMapException.AMAP_SERVICE_ILLEGAL_REQUEST);
                case 1203:
                    throw new AMapException(AMapException.AMAP_SERVICE_UNKNOWN_ERROR);
                    //sdk返回错误
                case 1800:
                    throw new AMapException(AMapException.AMAP_CLIENT_ERRORCODE_MISSSING);
                case 1801:
                    throw new AMapException(AMapException.AMAP_CLIENT_ERROR_PROTOCOL);
                case 1802:
                    throw new AMapException(AMapException.AMAP_CLIENT_SOCKET_TIMEOUT_EXCEPTION);
                case 1803:
                    throw new AMapException(AMapException.AMAP_CLIENT_URL_EXCEPTION);
                case 1804:
                    throw new AMapException(AMapException.AMAP_CLIENT_UNKNOWHOST_EXCEPTION);
                case 1806:
                    throw new AMapException(AMapException.AMAP_CLIENT_NETWORK_EXCEPTION);
                case 1900:
                    throw new AMapException(AMapException.AMAP_CLIENT_UNKNOWN_ERROR);
                case 1901:
                    throw new AMapException(AMapException.AMAP_CLIENT_INVALID_PARAMETER);
                case 1902:
                    throw new AMapException(AMapException.AMAP_CLIENT_IO_EXCEPTION);
                case 1903:
                    throw new AMapException(AMapException.AMAP_CLIENT_NULLPOINT_EXCEPTION);
                    //云图和附近错误码
                case 2000:
                    throw new AMapException(AMapException.AMAP_SERVICE_TABLEID_NOT_EXIST);
                case 2001:
                    throw new AMapException(AMapException.AMAP_ID_NOT_EXIST);
                case 2002:
                    throw new AMapException(AMapException.AMAP_SERVICE_MAINTENANCE);
                case 2003:
                    throw new AMapException(AMapException.AMAP_ENGINE_TABLEID_NOT_EXIST);
                case 2100:
                    throw new AMapException(AMapException.AMAP_NEARBY_INVALID_USERID);
                case 2101:
                    throw new AMapException(AMapException.AMAP_NEARBY_KEY_NOT_BIND);
                case 2200:
                    throw new AMapException(AMapException.AMAP_CLIENT_UPLOADAUTO_STARTED_ERROR);
                case 2201:
                    throw new AMapException(AMapException.AMAP_CLIENT_USERID_ILLEGAL);
                case 2202:
                    throw new AMapException(AMapException.AMAP_CLIENT_NEARBY_NULL_RESULT);
                case 2203:
                    throw new AMapException(AMapException.AMAP_CLIENT_UPLOAD_TOO_FREQUENT);
                case 2204:
                    throw new AMapException(AMapException.AMAP_CLIENT_UPLOAD_LOCATION_ERROR);
                    //路径规划
                case 3000:
                    throw new AMapException(AMapException.AMAP_ROUTE_OUT_OF_SERVICE);
                case 3001:
                    throw new AMapException(AMapException.AMAP_ROUTE_NO_ROADS_NEARBY);
                case 3002:
                    throw new AMapException(AMapException.AMAP_ROUTE_FAIL);
                case 3003:
                    throw new AMapException(AMapException.AMAP_OVER_DIRECTION_RANGE);
                    //短传分享
                case 4000:
                    throw new AMapException(AMapException.AMAP_SHARE_LICENSE_IS_EXPIRED);
                case 4001:
                    throw new AMapException(AMapException.AMAP_SHARE_FAILURE);
                default:
                    Toast.makeText(context,"错误码："+rCode , Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


}
