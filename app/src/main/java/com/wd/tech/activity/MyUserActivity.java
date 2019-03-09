package com.wd.tech.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.UserInfoBean;
import com.wd.tech.presenter.UserByUserIdPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;

public class MyUserActivity extends BaseActivity {


    private int userid;
    private String sessionid;
    private SimpleDraweeView userdetailsicon;
    private TextView userdetailsname;
    private TextView userdetailsintegral;
    private TextView userdetailsintegral1;
    private TextView user_details_signature;
    private TextView user_details_sex;
    private TextView user_details_phone;
    private TextView user_details_email;
    private ImageView user_details_whetherVip;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_user;
    }

    @Override
    protected void initView() {

        ImageView  userdetailsback = (ImageView) findViewById(R.id.user_details_back);
        userdetailsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userdetailsicon = (SimpleDraweeView) findViewById(R.id.user_details_icon);
        userdetailsname = (TextView) findViewById(R.id.user_details_name);
        userdetailsintegral1 = (TextView) findViewById(R.id.user_details_integral);
        user_details_signature = (TextView) findViewById(R.id.user_details_signature);
        user_details_sex = (TextView) findViewById(R.id.user_details_sex);
        user_details_phone = (TextView) findViewById(R.id.user_details_phone);
        user_details_whetherVip = (ImageView) findViewById(R.id.user_details_whetherVip);

        user_details_email = (TextView) findViewById(R.id.user_details_email);
        TextView wanshan = (TextView) findViewById(R.id.wanshan);
        wanshan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyUserActivity.this,MyWanshanActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        userid = WDApp.getShare().getInt("userid", 1);
        sessionid = WDApp.getShare().getString("sessionid", "");

        UserByUserIdPresenter userByUserIdPresenter = new UserByUserIdPresenter(new UserByIdClass());
        userByUserIdPresenter.reqeust(userid, sessionid);
    }

    @Override
    protected void destoryData() {

    }

    private class UserByIdClass implements DataCall<Result<UserInfoBean>> {
        @Override
        public void success(Result<UserInfoBean> data) {
            UserInfoBean result = data.getResult();
            userdetailsicon.setImageURI(result.getHeadPic());
            userdetailsname.setText(result.getNickName());
            userdetailsintegral1.setText(""+result.getIntegral());
            if (TextUtils.isEmpty(result.getSignature())){
                user_details_signature.setText("暂无签名");
            }else {
                user_details_signature.setText(result.getSignature());
            }
            if (result.getWhetherVip()==1){
                user_details_whetherVip.setVisibility(View.VISIBLE);
            }else {
                user_details_whetherVip.setVisibility(View.GONE);
            }

            user_details_sex.setText(result.getSex()==1?"男":"女");
            user_details_phone.setText(result.getPhone());
            if (TextUtils.isEmpty(result.getEmail())){
                user_details_email.setText("暂无邮箱");
            }else {
                user_details_email.setText(result.getEmail());
            }


        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
