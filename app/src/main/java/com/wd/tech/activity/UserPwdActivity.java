package com.wd.tech.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.ModifyUserPwdPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.EncryptUtil;
import com.wd.tech.utils.util.RsaCoder;

public class UserPwdActivity extends BaseActivity {


    private EditText jiu;
    private EditText xin;
    private int userid;
    private String sessionid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_pwd;
    }

    @Override
    protected void initView() {

        userid = WDApp.getShare().getInt("userid", 1);
        sessionid = WDApp.getShare().getString("sessionid", "");

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        jiu = (EditText) findViewById(R.id.jiu);
        xin = (EditText) findViewById(R.id.xin);
        Button  queding = (Button) findViewById(R.id.queding);
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = jiu.getText().toString().trim();
                String trim1 = xin.getText().toString().trim();
                if(trim.equals("")||trim1.equals("")){
                    return;
                }
                String encrypt = "";
                String encrypt1="";

                try {
                    encrypt = RsaCoder.encryptByPublicKey(trim);
                    encrypt1  = RsaCoder.encryptByPublicKey(trim1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ModifyUserPwdPresenter modifyUserPwdPresenter = new ModifyUserPwdPresenter(new UserPwdCall());
                modifyUserPwdPresenter.reqeust(userid,sessionid,encrypt,encrypt1);
            }
        });
    }

    @Override
    protected void destoryData() {

    }

    private class UserPwdCall implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(UserPwdActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            if(data.getStatus().equals("0000")){
                finish();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
