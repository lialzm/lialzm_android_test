package com.lialzm.android.third.push.umeng;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.lialzm.android.R;
import com.lialzm.android.util.LogUtil;
import com.umeng.common.message.UmengMessageDeviceConfig;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by lcy on 2016/3/29.
 */
public class UmengPush {
    private PushAgent mPushAgent;
    private Context context;
    private IpushToken ipushToken;

    public UmengPush(Context context, IpushToken ipushToken) {
        this.context = context;
        this.ipushToken = ipushToken;
    }

    public void init() {
        mPushAgent = PushAgent.getInstance(context);
        mPushAgent.setDebugMode(true);
        PushAgent.getInstance(context).onAppStart();
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 参考集成文档的1.6.3
             * http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        LogUtil.d("dealWithCustomMessage");
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(context.getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(context.getApplicationContext()).trackMsgDismissed(msg);
                        }
                       /* UTrack.getInstance(context).setClearPrevMessage(true);
                        UTrack.getInstance(context).trackMsgClick(msg);
                        UHandler b = PushAgent.getInstance(context)
                                .getNotificationClickHandler();
                        if (b != null) {
                            msg.clickOrDismiss = true;
                            b.handleMessage(context, msg);
                        }*/

                        String custom = msg.custom;
                        LogUtil.d("custom==" + custom);
                        try {
                            JSONObject jsonObject = new JSONObject(custom);
                            String title = jsonObject.getString("title");
                            String notifi_msg = jsonObject.getString("msg");
                            int icon = R.mipmap.ic_launcher;
                            long when = System.currentTimeMillis();
                            Notification noti = new Notification(icon, title, when + 10000);
                            noti.defaults |= Notification.DEFAULT_ALL;
                            noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
                            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
//                            remoteView.setImageViewResource(R.id.image, R.drawable.ic_launcher);
                            remoteView.setTextViewText(R.id.notification_title, title);
                            remoteView.setTextViewText(R.id.notification_text, notifi_msg);
                            noti.contentView = remoteView;
                            // 3、为Notification的contentIntent字段定义一个Intent(注意，使用自定义View不需要setLatestEventInfo()方法)
                            //这儿点击后简单启动Settings模块
                               /* PendingIntent contentIntent = PendingIntent.getActivity
                                        (context, 0, intent, 0);*/
                            PendingIntent contentIntent = getClickPendingIntent(context, msg);
                            noti.contentIntent = contentIntent;
                            NotificationManager mnotiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            mnotiManager.notify(0, noti);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                });
            }

            /**
             * 参考集成文档的1.6.4
             * http://dev.umeng.com/push/android/integration#1_6_4
             * */
           /* @Override
            public Notification getNotification(Context context,
                                                UMessage msg) {
                LogUtils.d("getNotification==" + msg.builder_id + "," + getApplicationContext().getPackageName());
                switch (msg.builder_id) {
                    case 0:
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView);
                        builder.setContentTitle(msg.title)
                                .setContentText(msg.text)
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);
                        Notification mNotification = builder.build();
                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        builder.build();
                        return mNotification;
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }*/
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 参考集成文档的1.6.2
         * http://dev.umeng.com/push/android/integration#1_6_2
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Map<String, String> map = msg.extra;
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
                ActivityManager.RunningTaskInfo rti = runningTasks.get(0);
                ComponentName component = rti.topActivity;
                String packageName = component.getPackageName();
                //取消通知栏
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(0);
                String custom = msg.custom;
                if ("0".equals(map.get("pushType"))) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(custom);
                        String title = jsonObject.getString("title");
                        String notifi_msg = jsonObject.getString("msg");
                        if (context.getPackageName().equals(packageName)) {//app在前台
                            /*Intent intent = new Intent(context, DialogActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("text", title + ":" + notifi_msg);
                            startActivity(intent);*/
                        } else {
//                    Intent var3 = getPackageManager().getLaunchIntentForPackage(getPackageName());
                          /*  Intent var3 = new Intent(context, LoginActivity.class);
                            var3.putExtra("text", title + ":" + notifi_msg);
                            var3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    var3.setPackage((String) null);
                            var3.putExtra("action", "1");
                            startActivity(var3);*/
                        }
                /*Intent var3 = getPackageManager().getLaunchIntentForPackage(getPackageName());
                var3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                var3.setPackage((String) null);
                var3.putExtra("action", "1");
                startActivity(var3);*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Intent var3 = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                    var3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    var3.setPackage((String) null);
                    var3.putExtra("action", "1");
                    context.startActivity(var3);
                }
            }
        };
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        //参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    public void enable() {
        //开启友盟推送
        mPushAgent = PushAgent.getInstance(context);
        mPushAgent.onAppStart();
        //开启推送并设置注册的回调处理
        mPushAgent.enable(mRegisterCallback);
    }


    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 2:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.fail_to_download), Toast.LENGTH_LONG).show();
                    break;
                case 1:
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.unable_to_download_without_sdcard), Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }

        }
    };

    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    //获取token
                    getMsg();
                }
            });
        }
    };

    private void getMsg() {
        String info = String.format("enabled:%s  isRegistered:%s  DeviceToken:%s " +
                        "SdkVersion:%s AppVersionCode:%s AppVersionName:%s",
                mPushAgent.isEnabled(), mPushAgent.isRegistered(),
                mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
                UmengMessageDeviceConfig.getAppVersionCode(context), UmengMessageDeviceConfig.getAppVersionName(context));
        String token = mPushAgent.getRegistrationId();
        LogUtil.d("token==" + token);
        if (TextUtils.isEmpty(token))
            return;
        ipushToken.push(token);
    }

    /**
     * 上传token
     */
    public interface IpushToken {
        void push(String token);
    }


}
