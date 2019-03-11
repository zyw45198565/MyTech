package com.wd.tech;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.wd.tech.activity.ChatActivity;
import com.wd.tech.bean.Conversation;
import com.wd.tech.greendao.ConversationDao;
import com.wd.tech.greendao.DaoUtils;
import com.wd.tech.hractivity.FaceDB;

import java.io.File;
import java.util.Iterator;
import java.util.List;


/**
 * @name: MyApplication
 * @remark:
 */
public class WDApp extends MultiDexApplication {

    private final String TAG = this.getClass().toString();
   public FaceDB mFaceDB;
    Uri mImage;


    public void setCaptureImage(Uri uri) {
        mImage = uri;
    }

    public Uri getCaptureImage() {
        return mImage;
    }

    /** 主线程ID */
    private static int mMainThreadId = -1;
    /** 主线程ID */
    private static Thread mMainThread;
    /** 主线程Handler */
    private static Handler mMainThreadHandler;
    /** 主线程Looper */
    private static Looper mMainLooper;

    /**
     * context 全局唯一的上下文
     */
    private static Context context;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences em_sp_at_message;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();

        mFaceDB = new FaceDB(this.getExternalCacheDir().getPath());
        mImage = null;

        context=this;
        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();
        sharedPreferences = getSharedPreferences("share.xml",MODE_PRIVATE);
        em_sp_at_message = getSharedPreferences("EM_SP_AT_MESSAGE", Context.MODE_PRIVATE);

        Fresco.initialize(this,getConfig());//图片加载框架初始化

        /*CrashReport.initCrashReport(getApplicationContext(), "16d12f5b5e", false);

        UMConfigure.init(this,  UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(true);
        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        XGPushConfig.setMiPushAppId(getApplicationContext(), "APPID");
        XGPushConfig.setMiPushAppKey(getApplicationContext(), "APPKEY");
        XGPushConfig.setMzPushAppId(this, "APPID");
        XGPushConfig.setMzPushAppKey(this, "APPKEY");
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.d("TPush", "注册成功，设备token为：" + data);
            }
            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });*/
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        EaseUI.getInstance().init(this,null);
        EMClient.getInstance().setDebugMode(true);
        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                username = username.toLowerCase();
                EaseUser easeUser = new EaseUser(username);
                List<Conversation> aa = DaoUtils.getInstance().getConversationDao().loadAll();
                Conversation conversation = DaoUtils.getInstance().getConversationDao().queryBuilder().where(ConversationDao.Properties.UserName.eq(username)).build().unique();
                if (conversation!=null) {
                    easeUser.setNickname(conversation.getNickName());
                    easeUser.setAvatar(conversation.getHeadPic());
                }
                return easeUser;
            }
        });
//        MultiDex.install(this);
        getMessage();
    }


    private ImagePipelineConfig getConfig() {
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+"image");
        if (!file.exists()){
            file.mkdir();
        }
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setMainDiskCacheConfig(DiskCacheConfig.newBuilder(this)
                        .setBaseDirectoryPath(file).build())
                .build();
        return config;
    }

    public static SharedPreferences getShare(){
        return sharedPreferences;
    }
    public static SharedPreferences getEShare(){
        return em_sp_at_message;
    }

    /**
     * @author: 康海涛 QQ2541849981
     * @describe: 获取全局Application的上下文
     * @return 全局唯一的上下文
     */
    public static Context getContext() {
        return context;
    }

    /** 获取主线程ID */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /** 获取主线程 */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /** 获取主线程的handler */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /** 获取主线程的looper */
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }


    /**
     * @param path
     * @return
     */
    public static Bitmap decodeImage(String path) {
        Bitmap res;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inSampleSize = 1;
            op.inJustDecodeBounds = false;
            //op.inMutable = true;
            res = BitmapFactory.decodeFile(path, op);
            //rotate and scale.
            Matrix matrix = new Matrix();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }

            Bitmap temp = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, true);
            Log.d("com.arcsoft", "check target Image:" + temp.getWidth() + "X" + temp.getHeight());

            if (!temp.equals(res)) {
                res.recycle();
            }
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void getMessage(){

        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                if (list != null){
                    for (int i = 0; i < list.size(); i++) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setDefaults(Notification.DEFAULT_ALL);
                        Intent intent = new Intent(context, ChatActivity.class);//将要跳转的界面
                        intent.putExtra(EaseConstant.EXTRA_USER_ID,list.get(i).getUserName());
                        intent.putExtra("userNames",list.get(i).getUserName());
                        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                        //Intent intent = new Intent();//只显示通知，无页面跳转
                        builder.setAutoCancel(true);//点击后消失
                        builder.setSmallIcon(R.mipmap.icon111);//设置通知栏消息标题的头像
                        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);//设置通知铃声
                        builder.setTicker("状态栏显示的文字");

                        Conversation conversation = DaoUtils.getInstance().getConversationDao().queryBuilder().where(ConversationDao.Properties.UserName.eq(list.get(i).getUserName())).build().unique();
                        if (conversation!=null){
                            builder.setContentTitle(conversation.getNickName());
                        }
                        String message = list.get(i).getBody().toString().substring(5, list.get(i).getBody().toString().length() - 1);

                        builder.setContentText(message);
                        //利用PendingIntent来包装我们的intent对象,使其延迟跳转
                        PendingIntent intentPend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                        builder.setContentIntent(intentPend);
                        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                        manager.notify(0, builder.build());
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });

    }
}