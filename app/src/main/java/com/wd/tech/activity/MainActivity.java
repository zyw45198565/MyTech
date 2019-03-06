package com.wd.tech.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Result;
import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.NetWorkManager;
import com.wd.tech.utils.util.RsaCoder;
import com.wd.tech.utils.util.WDActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends WDActivity {

    public static int PERMISSION_REQ = 0x123456;

    private String[] mPermission = new String[] {
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private List<String> mRequestPermission = new ArrayList<String>();


    int i=3;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                i--;
                if(i<=0){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                handler.sendEmptyMessageDelayed(1,1000);
            }
        }
    };

    @Override
    protected void initView() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            for (String one : mPermission) {
                if (PackageManager.PERMISSION_GRANTED != this.checkPermission(one, Process.myPid(), Process.myUid())) {
                    mRequestPermission.add(one);
                }
            }
            if (!mRequestPermission.isEmpty()) {
                this.requestPermissions(mRequestPermission.toArray(new String[mRequestPermission.size()]), PERMISSION_REQ);
                return ;
            }
        }
        startActiviy();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        // 版本兼容
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return;
        }
        if (requestCode == PERMISSION_REQ) {
            for (int i = 0; i < grantResults.length; i++) {
                for (String one : mPermission) {
                    if (permissions[i].equals(one) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mRequestPermission.remove(one);
                    }
                }
            }
            startActiviy();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void destoryData() {
        handler.removeMessages(1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQ) {
            if (resultCode == 0) {
                this.finish();
            }
        }
    }

    public void startActiviy() {
        if (mRequestPermission.isEmpty()) {
            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setTitle("loading register data...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    WDApp app = (WDApp) MainActivity.this.getApplicationContext();
                    app.mFaceDB.loadFaces();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.cancel();
                            handler.sendEmptyMessageDelayed(1,1000);
                        }
                    });
                }
            }).start();
        } else {
            Toast.makeText(this, "PERMISSION DENIED!", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.finish();
                }
            }, 3000);
        }
    }
}
