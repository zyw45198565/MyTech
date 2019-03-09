package com.wd.tech.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.chat.EMClient;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.UserInfoBean;
import com.wd.tech.hractivity.DetecterActivity;
import com.wd.tech.hractivity.RegisterActivity;
import com.wd.tech.presenter.ModifyEmailPresenter;
import com.wd.tech.presenter.ModifyHeadPicPresenter;
import com.wd.tech.presenter.UserByUserIdPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tech
 * @date 2019/2/20 11:10
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class MySettingActivity extends BaseActivity implements View.OnClickListener,DataCall<Result<UserInfoBean>> {
    private final String TAG = this.getClass().toString();

    private static final int REQUEST_CODE_IMAGE_CAMERA = 5;
    private static final int REQUEST_CODE_IMAGE_OP = 6;
    private static final int REQUEST_CODE_OP = 7;

    private ImageView back;
    private TextView tui;
    private int userid;
    private String sessionid;
    private ImageView head;
    private TextView name;
    private TextView sex;
    private TextView date1;
    private TextView phone;
    private TextView emaile;
    private TextView jifen;
    private TextView vip;
    private TextView faceid;
    private ModifyHeadPicPresenter modifyHeadPicPresenter;
    private UserByUserIdPresenter userByUserIdPresenter;
    private LinearLayout you;
    private UserInfoBean result;
    String emaile123="";
    private String qian111;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mysettings;
    }

    @Override
    protected void initView() {

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        tui = (TextView) findViewById(R.id.tui);
        tui.setOnClickListener(this);
        head = (ImageView) findViewById(R.id.head);
        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        date1 = (TextView) findViewById(R.id.date);
        phone = (TextView) findViewById(R.id.phone);
        emaile = (TextView) findViewById(R.id.emaile);
        jifen = (TextView) findViewById(R.id.jifen);
        vip = (TextView) findViewById(R.id.vip);
        faceid = (TextView) findViewById(R.id.faceid);
        LinearLayout head = (LinearLayout) findViewById(R.id.llhead);
        head.setOnClickListener(this);
        LinearLayout lldate = (LinearLayout) findViewById(R.id.lldate);
        lldate.setOnClickListener(this);
        you = (LinearLayout) findViewById(R.id.you);
        you.setOnClickListener(this);
        TextView xiupwd = (TextView) findViewById(R.id.xiupwd);
        xiupwd.setOnClickListener(this);
        modifyHeadPicPresenter = new ModifyHeadPicPresenter(new UpHead());
        TextView faceid = (TextView) findViewById(R.id.faceid);
        faceid.setOnClickListener(this);
        LinearLayout qian = (LinearLayout) findViewById(R.id.qian);
        qian.setOnClickListener(this);
    }

    @Override
    protected void destoryData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        userid = WDApp.getShare().getInt("userid", 1);
        sessionid = WDApp.getShare().getString("sessionid", "");
        userByUserIdPresenter = new UserByUserIdPresenter(this);
        userByUserIdPresenter.reqeust(userid,sessionid);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tui:
                Dialog dialog = new AlertDialog.Builder(MySettingActivity.this).setMessage("退出登录?")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences share = WDApp.getShare();
                                SharedPreferences.Editor edit = share.edit();
                                edit.putBoolean("zai",false);
                                edit.commit();
                                EMClient.getInstance().logout(true);
                                Intent intent = new Intent(MySettingActivity.this,LoginActivity.class);
                                startActivity(intent);

                                finish();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
            case R.id.llhead:
                final Dialog dialog1 = new Dialog(MySettingActivity.this,R.style.DialogTheme);
                View inflate = View.inflate(MySettingActivity.this, R.layout.myhead_dialog, null);
                dialog1.setContentView(inflate);
                Window dialogWindow = dialog1.getWindow();
                dialogWindow.setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高
                lp.width=d.getWidth();
                dialogWindow.setAttributes(lp);
                dialog1.show();
                LinearLayout xiangji = inflate.findViewById(R.id.xiangji);
                xiangji.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 2);// 采用ForResult打开

                        dialog1.dismiss();
                    }
                });
                LinearLayout xiangce = inflate.findViewById(R.id.xiangce);
                xiangce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");// 开启Pictures画面Type设定为image
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(intent, 1);

                        dialog1.dismiss();
                    }
                });
                LinearLayout quxiao = inflate.findViewById(R.id.quxiao);
                quxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                break;
            case R.id.lldate:
                TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

                        date1.setText(sf.format(date)+"");
                    }
                })
                        .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示.setCancelText("取消")
                        .setSubmitText("确定").build();
                pvTime.show();
                break;
            case R.id.you:
                final EditText editemail = new EditText(this);
                editemail.setText(emaile123);
                AlertDialog builder3 = new AlertDialog.Builder(this)
                        .setTitle("修改邮箱")
                        .setView(editemail)//设置输入框
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String trim = editemail.getText().toString().trim();
                                boolean email1 = isEmail(trim + "");
                                if (email1) {
                                    emaile.setText(trim);
                                    emaile123 = trim;
                                    ModifyEmailPresenter modifyEmailPresenter = new ModifyEmailPresenter(new ModifyEmaileCall());
                                    modifyEmailPresenter.reqeust(userid,sessionid,trim);
                                } else {
                                    UIUtils.showToastSafe("请输入正确的邮箱");
                                    return;
                                }
                            }
                        }).setNegativeButton("取消", null).create();
                builder3.show();
                break;
            case R.id.xiupwd:
            startActivity(new Intent(MySettingActivity.this,UserPwdActivity.class));
                break;
            case R.id.faceid:
                new AlertDialog.Builder(this)
                        .setTitle("请选择注册方式")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setItems(new String[]{"打开图片", "拍摄照片"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 1:
                                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                        ContentValues values = new ContentValues(1);
                                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                        ((WDApp)(MySettingActivity.this.getApplicationContext())).setCaptureImage(uri);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                        startActivityForResult(intent, REQUEST_CODE_IMAGE_CAMERA);
                                        break;
                                    case 0:
                                        Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
                                        getImageByalbum.addCategory(Intent.CATEGORY_OPENABLE);
                                        getImageByalbum.setType("image/jpeg");
                                        startActivityForResult(getImageByalbum, REQUEST_CODE_IMAGE_OP);
                                        break;
                                    default:;
                                }
                            }
                        })
                        .show();
                break;
            case R.id.qian:
                Intent intent = new Intent(MySettingActivity.this, QianActivity.class);
                intent.putExtra("ming",qian111);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void success(Result<UserInfoBean> data) {
        if(data.getStatus().equals("0000")){
            result = data.getResult();
            qian111 = result.getSignature();
            Glide.with(this).load(result.getHeadPic())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(head);
            name.setText(result.getNickName()+"");
            sex.setText(result.getSex()==1?"男":"女");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Date date2 = new Date(result.getBirthday());
            date1.setText(sf.format(date2)+"");
            phone.setText(result.getPhone());
            emaile123 = result.getEmail()+"";
            emaile.setText(result.getEmail()+"");
            jifen.setText(result.getIntegral()+"");
            vip.setText(result.getWhetherVip()==1?"是":"否");
            faceid.setText(result.getWhetherFaceId()==1?"已绑定":"点击绑定");

        }
    }

    @Override
    public void fail(ApiException e) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_IMAGE_CAMERA && resultCode == RESULT_OK) {
            Uri mPath = ((WDApp)(MySettingActivity.this.getApplicationContext())).getCaptureImage();
            String file = getPath(mPath);
            Bitmap bmp = WDApp.decodeImage(file);
            startRegister(bmp, file);
        }


        if(data==null){
            return;
        }
        if(requestCode==1){
            String icon = getFilePath("icon", 0, data);
            modifyHeadPicPresenter.reqeust(userid,sessionid,icon,1);
        }
        if(requestCode==2){
            Bundle extras = data.getExtras();
            Bitmap data1 = (Bitmap) extras.get("data");
            File file = compressImage(data1);
            modifyHeadPicPresenter.reqeust(userid,sessionid,file,2);
        }

        if (requestCode == REQUEST_CODE_IMAGE_OP && resultCode == RESULT_OK) {
            Uri mPath = data.getData();
            String file = getPath(mPath);
            Bitmap bmp = WDApp.decodeImage(file);
            if (bmp == null || bmp.getWidth() <= 0 || bmp.getHeight() <= 0 ) {
                Log.e("----", "error");
            } else {
                Log.i("----", "bmp [" + bmp.getWidth() + "," + bmp.getHeight());
            }
            startRegister(bmp, file);
        } else if (requestCode == REQUEST_CODE_OP) {
            Log.i("----", "RESULT =" + resultCode);
            if (data == null) {
                return;
            }
            Bundle bundle = data.getExtras();
            String path = bundle.getString("imagePath");
            Log.i("----", "path="+path);
        }
    }

    private class UpHead implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(MySettingActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            userByUserIdPresenter.reqeust(userid,sessionid);
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    /**
     * 压缩图片（质量压缩）
     * @param bitmap
     */
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(),filename+".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                Log.e("---",e.getMessage());
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            Log.e("----",e.getMessage());
            e.printStackTrace();
        }
        recycleBitmap(bitmap);
        return file;
    }
    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps==null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

    //正则邮箱
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }


    private class ModifyEmaileCall implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(MySettingActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void fail(ApiException e) {

        }
    }


    /**
     * @param uri
     * @return
     */
    private String getPath(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(this, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(this, contentUri, selection, selectionArgs);
                }
            }
        }
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor actualimagecursor = this.getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        String end = img_path.substring(img_path.length() - 4);
        if (0 != end.compareToIgnoreCase(".jpg") && 0 != end.compareToIgnoreCase(".png")) {
            return null;
        }
        return img_path;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param mBitmap
     */
    private void startRegister(Bitmap mBitmap, String file) {
        Intent it = new Intent(MySettingActivity.this, RegisterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("imagePath", file);
        it.putExtras(bundle);
        startActivityForResult(it, REQUEST_CODE_OP);
    }

    private void startDetector(int camera) {
        Intent it = new Intent(MySettingActivity.this, DetecterActivity.class);
        it.putExtra("Camera", camera);
        startActivityForResult(it, REQUEST_CODE_OP);
    }

}
