package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.UserInfoBean;
import com.wd.tech.presenter.UserByUserIdPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.WDActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tech
 * @date 2019/2/20 11:10
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class MySettingActivity extends WDActivity implements View.OnClickListener,DataCall<Result<UserInfoBean>> {


    private ImageView back;
    private TextView tui;
    private int userid;
    private String sessionid;
    private ImageView head;
    private TextView name;
    private TextView sex;
    private TextView date;
    private TextView phone;
    private TextView emaile;
    private TextView jifen;
    private TextView vip;
    private TextView faceid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mysettings;
    }

    @Override
    protected void initView() {

        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        tui = findViewById(R.id.tui);
        tui.setOnClickListener(this);
        head = findViewById(R.id.head);
        name = findViewById(R.id.name);
        sex = findViewById(R.id.sex);
        date = findViewById(R.id.date);
        phone = findViewById(R.id.phone);
        emaile = findViewById(R.id.emaile);
        jifen = findViewById(R.id.jifen);
        vip = findViewById(R.id.vip);
        faceid = findViewById(R.id.faceid);
    }

    @Override
    protected void destoryData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        userid = WDApp.getShare().getInt("userid", 1);
        sessionid = WDApp.getShare().getString("sessionid", "");
        UserByUserIdPresenter userByUserIdPresenter = new UserByUserIdPresenter(this);
        userByUserIdPresenter.reqeust(userid,sessionid);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tui:
                SharedPreferences share = WDApp.getShare();
                SharedPreferences.Editor edit = share.edit();
                edit.putBoolean("zai",false);
                edit.commit();
                Intent intent = new Intent(MySettingActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void success(Result<UserInfoBean> data) {
        if(data.getStatus().equals("0000")){
            UserInfoBean result = data.getResult();
            Glide.with(this).load(result.getHeadPic()).into(head);
            name.setText(result.getNickName()+"");
            sex.setText(result.getSex()==1?"男":"女");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = new Date(result.getBirthday());
            date.setText(sf.format(date1)+"");
            phone.setText(result.getPhone());
            emaile.setText(result.getEmail()+"");
            jifen.setText(result.getIntegral()+"");
            vip.setText(result.getWhetherVip()==1?"是":"否");
            faceid.setText(result.getWhetherFaceId()==1?"已绑定":"点击绑定");

        }
    }

    @Override
    public void fail(ApiException e) {

    }
}
