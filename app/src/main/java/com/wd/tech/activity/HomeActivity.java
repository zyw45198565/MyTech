package com.wd.tech.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.UserInfoBean;
import com.wd.tech.frag.Frag_01;
import com.wd.tech.frag.Frag_02;
import com.wd.tech.frag.Frag_03;
import com.wd.tech.presenter.UserByUserIdPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.WDActivity;

public class HomeActivity extends WDActivity implements View.OnClickListener {


    private Frag_01 frag_01;
    private Frag_02 frag_02;
    private Frag_03 frag_03;
    private FragmentTransaction fragmentTransaction;
    private LinearLayout mlinearhome;
    private LinearLayout mLinear;
    private DrawerLayout mdraw;
    private RelativeLayout rl1;
    private LinearLayout ll1;
    private int userid;
    private String sessionid;
    private ImageView head;
    private TextView name;
    private TextView qian;
    private ImageView vip;
    private LinearLayout setting;
    private LinearLayout love;
    private LinearLayout shoucang;
    private LinearLayout myinyegral;
    private LinearLayout myTask;
    private LinearLayout tie;
    private LinearLayout tongzhi;

    @Override
    protected void initView() {
        frag_01 = new Frag_01();
        frag_02 = new Frag_02();
        frag_03 = new Frag_03();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl, frag_01);
        fragmentTransaction.add(R.id.fl, frag_02);
        fragmentTransaction.add(R.id.fl, frag_03);
        fragmentTransaction.hide(frag_02);
        fragmentTransaction.hide(frag_03);
        fragmentTransaction.commit();

        RadioGroup radioGroup  = findViewById(R.id.rg);
        radioGroup.check(radioGroup.getChildAt(0).getId());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(frag_01);
                fragmentTransaction.hide(frag_02);
                fragmentTransaction.hide(frag_03);
                switch (checkedId){
                   case  R.id.rb1:
                       fragmentTransaction.show(frag_01);
                       break;
                    case  R.id.rb2:
                        fragmentTransaction.show(frag_02);
                        break;
                    case  R.id.rb3:
                        fragmentTransaction.show(frag_03);
                        break;
                }
                fragmentTransaction.commit();
            }
        });


        mlinearhome = findViewById(R.id.mlinearhome);
        mLinear = findViewById(R.id.mLinear);
        mdraw = findViewById(R.id.mdraw);

        //点击侧滑
        mdraw.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                mlinearhome.layout(mLinear.getRight(), 0, mLinear.getRight() + display.getWidth(), display.getHeight());
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });


        SimpleDraweeView log = findViewById(R.id.log);


        LinearLayout login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        rl1 = findViewById(R.id.rl1);
        ll1 = findViewById(R.id.ll1);
        head = findViewById(R.id.head);
        name = findViewById(R.id.name);
        qian = findViewById(R.id.qian);
        vip = findViewById(R.id.vip);
        setting = findViewById(R.id.setting);
        setting.setOnClickListener(this);
        love = findViewById(R.id.love);
        love.setOnClickListener(this);
        shoucang = findViewById(R.id.shoucang);
        shoucang.setOnClickListener(this);
        myinyegral = findViewById(R.id.myinyegral);
        myinyegral.setOnClickListener(this);
        myTask = findViewById(R.id.myTask);
        myTask.setOnClickListener(this);
        tie = findViewById(R.id.tie);
        tie.setOnClickListener(this);
        tongzhi = findViewById(R.id.tongzhi);
        tongzhi.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences share = WDApp.getShare();
        boolean zai = share.getBoolean("zai", false);
        if(zai){
            rl1.setVisibility(View.GONE);
            ll1.setVisibility(View.VISIBLE);
        }else {
            rl1.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.GONE);
        }

        userid = WDApp.getShare().getInt("userid", 1);
        sessionid = WDApp.getShare().getString("sessionid", "");

        UserByUserIdPresenter userByUserIdPresenter = new UserByUserIdPresenter(new UserByIdClass());
        userByUserIdPresenter.reqeust(userid, sessionid);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void destoryData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.setting:
                Intent intent = new Intent(HomeActivity.this,MySettingActivity.class);
                startActivity(intent);
                break;
            case R.id.love:
                Intent intent1 = new Intent(HomeActivity.this,LoveActivity.class);
                startActivity(intent1);
                break;
            case R.id.shoucang:
                Intent intent2 = new Intent(HomeActivity.this,CollectionActivity.class);
                startActivity(intent2);
                break;
            case R.id.myinyegral:
                Intent intent3 = new Intent(HomeActivity.this,MyintegralActivity.class);
                startActivity(intent3);
                break;
            case R.id.myTask:
                Intent intent4 = new Intent(HomeActivity.this,MytaskActivity.class);
                startActivity(intent4);
                break;
            case R.id.tie:
                Intent intent5 = new Intent(HomeActivity.this,MyPostActivity.class);
                startActivity(intent5);
                break;
            case R.id.tongzhi:
                Intent intent6 = new Intent(HomeActivity.this,MyTongzhiActivity.class);
                startActivity(intent6);
                break;
        }
    }

    private class UserByIdClass implements DataCall<Result<UserInfoBean>> {
        @Override
        public void success(Result<UserInfoBean> data) {
            if(data.getStatus().equals("0000")){

            UserInfoBean result = data.getResult();

            int whetherVip = result.getWhetherVip();
            if(whetherVip==1){
                vip.setVisibility(View.VISIBLE);
            }else {
                vip.setVisibility(View.GONE);
            }
            qian.setText(""+result.getSignature());
            name.setText(result.getNickName()+"");
            Glide.with(HomeActivity.this).load(result.getHeadPic())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(head);
            }

        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
