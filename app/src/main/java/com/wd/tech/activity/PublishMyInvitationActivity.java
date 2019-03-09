package com.wd.tech.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.publish.ImageAdapter2;
import com.wd.tech.bean.Result;
import com.wd.tech.frag.Frag_03;
import com.wd.tech.presenter.ReleasePostPresenter;
import com.wd.tech.presenter.TheTaskPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.StringUtils;
import com.wd.tech.utils.util.UIUtils;
import com.wd.tech.utils.util.WDActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class PublishMyInvitationActivity extends WDActivity implements View.OnClickListener {

    @BindView(R.id.publish_myinvitation_publish)
    TextView mPublishMyinvitationPublish;
    @BindView(R.id.publish_myinvitation_edit)
    EditText mPublishMyinvitationEdit;//输入的内容
    @BindView(R.id.publish_myinvitation_number)
    TextView mPublishMyinvitationNumber;//输入的字数
    @BindView(R.id.publish_myinvitation_picter)
    RecyclerView mPublishMyinvitationPicter;//要上传的图片
    ArrayList<Object> objects = new ArrayList<>();
    private PopupWindow mPop;
    private View mParent;
    private ImageAdapter2 mImageAdapter2;
    private ReleasePostPresenter releasePostPresenter;
    private SharedPreferences sp;
    private int userid;
    private String sessionid;
    List<File> files = new ArrayList<>();
    private TheTaskPresenter theTaskPresenter;
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.READ_EXTERNAL_STORAGE
            ,Manifest.permission.READ_PHONE_STATE};



    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_my_invitation;
    }

    @OnTextChanged(value = R.id.publish_myinvitation_edit, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void editTextDetailChange(Editable editable) {
        int detailLength = mPublishMyinvitationEdit.length();
        mPublishMyinvitationNumber.setText(detailLength + "/300");
    }
    @OnClick({R.id.publish_myinvitation_cancel,R.id.publish_myinvitation_publish})
    public void PublishClick(View view){
        switch (view.getId()){
            case R.id.publish_myinvitation_cancel://取消
                finish();
                break;
            case R.id.publish_myinvitation_publish://点击发表
                String trim = mPublishMyinvitationEdit.getText().toString().trim();
                finish();
                releasePostPresenter.reqeust(userid,sessionid,trim,files);
                break;
        }
    }

    @Override
    protected void initView() {

        //做任务
        theTaskPresenter = new TheTaskPresenter(new TheTaskCall());


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
            }
        }



        mParent = View.inflate(PublishMyInvitationActivity.this, R.layout.activity_publish_my_invitation, null);
        objects.add(R.drawable.addpicture);

        sp = WDApp.getShare();
        userid = sp.getInt("userid", 0);
        sessionid = sp.getString("sessionid", "");

        mPublishMyinvitationPicter.setLayoutManager(new GridLayoutManager(this,3));
        mImageAdapter2 = new ImageAdapter2(objects, new ImageAdapter2.Open() {//相册设置图片
            @Override
            public void onDakaiXiangCe() {
                onViewClicked();
            }
        });
        mPublishMyinvitationPicter.setAdapter(mImageAdapter2);
        releasePostPresenter = new ReleasePostPresenter(new myReleasePostCall());//发布帖子
    }

    private void onViewClicked() {//弹框
        View popShow = View.inflate(PublishMyInvitationActivity.this,R.layout.layout_popupwindow_btn,null);
        hideInput();//隐藏软键盘
        mPop = new PopupWindow(popShow, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        mPop.setFocusable(true);//获取焦点
        mPop.setTouchable(true);
        mPop.setOutsideTouchable(true);//点击外部窗口可消失
        mPop.setBackgroundDrawable(new BitmapDrawable());//设置背景 必须写

        LinearLayout camera = popShow.findViewById(R.id.publish_pop_camera);//找弹框组件
        LinearLayout picture = popShow.findViewById(R.id.publish_pop_picture);
        Button cancel = popShow.findViewById(R.id.publish_pop_cancel);

        camera.setOnClickListener(this);//相机
        picture.setOnClickListener(this);//相册
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();//点击弹框消失
            }
        });
        mPop.showAtLocation(mParent, Gravity.BOTTOM,0,0);//父类底部显示
    }

    @Override//相册回调
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        if(requestCode==0){
            String filePath = getFilePath("icon",requestCode,data);
            objects.add(filePath);
            mImageAdapter2.notifyDataSetChanged();


            Uri data1 = data.getData();
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor actualimagecursor = managedQuery(data1,proj,null,null,null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            final File file = new File(img_path);
            files.add(file);
        }

        if(requestCode == 1){
            Bundle extras = data.getExtras();
            Bitmap data1 = (Bitmap) extras.get("data");
            File file = compressImage(data1);
            files.add(file);
            String absolutePath = file.getAbsolutePath();
            objects.add(absolutePath);
            mImageAdapter2.notifyDataSetChanged();
        }
    }

    @Override
    protected void destoryData() {
        releasePostPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    protected void hideInput() {//隐藏键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish_pop_camera://相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 相机的 隐式回传意图 android.media.action.IMAGE_CAPTURE
                intent.addCategory("android.intent.category.DEFAULT");// 添加种类
                startActivityForResult(intent, 1); // 跳转回传( 意图, 请求码 )
                mPop.dismiss();
                break;
            case R.id.publish_pop_picture://相册

                Intent intent1 = new Intent(Intent.ACTION_PICK);// 设置相册的意图（权限）
                intent1.setType("image/*");// 设置显式MIME数据类型
                startActivityForResult(intent1, 0);// 跳转回传
                mPop.dismiss();
                break;
        }
    }

    private class myReleasePostCall implements DataCall<Result> {//发布帖子
        @Override
        public void success(Result data) {
            if(data.getStatus().equals("0000")){
                /*finish();*/
                theTaskPresenter.reqeust(userid,sessionid,1003);
            }
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("发布帖子：  "+e.getMessage());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" +
                        grantResults[i]);
            }
        }
    }

    public String getFilePath(String fileName, int requestCode, Intent data) {//得到图片路径
        if (requestCode == 1) {//相机
            return fileName;
        } else if (requestCode == 0) {//相册
            Uri uri = data.getData();
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
            int actualImageColumnIndex = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String imgpath = actualimagecursor.getString(actualImageColumnIndex);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// 4.0以上平台会自动关闭cursor,所以加上版本判断,OK
                actualimagecursor.close();
            }
            return imgpath;
        }
        return null;
    }


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

    private class TheTaskCall implements DataCall<Result> {
        @Override
        public void success(Result data) {

        }

        @Override
        public void fail(ApiException e) {

        }
    }

}
