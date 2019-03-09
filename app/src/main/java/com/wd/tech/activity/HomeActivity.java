package com.wd.tech.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeActivity extends WDActivity implements View.OnClickListener {


    private Frag_01 mfrag_01;
    private Frag_02 mfrag_02;
    private Frag_03 mfrag_03;
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
    private LinearLayout qian1;
    private int mClickFragmentId;
    private int mCurrentFragmentId;
    private RadioGroup radioGroup;
    FragmentManager supportFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState!=null){
            supportFragmentManager = getSupportFragmentManager();
            mfrag_01 = (Frag_01) supportFragmentManager.findFragmentByTag("frag1");
            mfrag_02 = (Frag_02) supportFragmentManager.findFragmentByTag("frag2");
            mfrag_03 = (Frag_03) supportFragmentManager.findFragmentByTag("frag3");
        }
        super.onCreate(savedInstanceState);


        EventBus.getDefault().register(this);
        if(supportFragmentManager==null){
            supportFragmentManager = getSupportFragmentManager();
        }
        fragmentTransaction = supportFragmentManager.beginTransaction();
        if(mfrag_01==null){
            mfrag_01=new Frag_01();
            mfrag_02=new Frag_02();
            mfrag_03=new Frag_03();
            fragmentTransaction.add(R.id.fl,mfrag_01,"frag1").add(R.id.fl,mfrag_02,"frag2").add(R.id.fl,mfrag_03,"frag3").show(mfrag_01).hide(mfrag_02).hide(mfrag_03).commit();
        }else {
            fragmentTransaction.show(mfrag_01).hide(mfrag_02).hide(mfrag_03).commit();
        }
        mCurrentFragmentId = R.id.rb1;

        radioGroup  = findViewById(R.id.rg);
        radioGroup.check(radioGroup.getChildAt(0).getId());
        findViewById(R.id.rb1).setOnClickListener(this);
        findViewById(R.id.rb2).setOnClickListener(this);
        findViewById(R.id.rb3).setOnClickListener(this);

        mlinearhome = findViewById(R.id.mlinearhome);
        mLinear = findViewById(R.id.mLinear);
        mdraw = findViewById(R.id.mdraw);

        mdraw.setScrimColor(Color.TRANSPARENT);//去除阴影
        mLinear.measure(0, 0);
        final float width = mLinear.getMeasuredWidth() * 0.2f;//获取布局宽度，并获得左移大小
        mLinear.setTranslationX(-width);                 //底布局左移
        //点击侧滑
        mdraw.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
                mLinear.setTranslationX(-width + width * v);               //底布局跟着移动
                mlinearhome.setTranslationX(view.getMeasuredWidth() * v);   //主界面布局移动，移动长度等于抽屉的移动长度

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
                intent(LoginActivity.class);
            }
        });

        rl1 = findViewById(R.id.rl1);
        ll1 = findViewById(R.id.ll1);
        head = findViewById(R.id.head);
        head.setOnClickListener(this);
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
        qian1 = findViewById(R.id.qiandao);
        qian1.setOnClickListener(this);
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void onResume() {
        super.onResume();
        mdraw.closeDrawer(mLinear);
        SharedPreferences share = WDApp.getShare();
        boolean zai = share.getBoolean("zai", false);
        if(zai){
            rl1.setVisibility(View.GONE);
            ll1.setVisibility(View.VISIBLE);
            userid = WDApp.getShare().getInt("userid", 1);
            sessionid = WDApp.getShare().getString("sessionid", "");
            UserByUserIdPresenter userByUserIdPresenter = new UserByUserIdPresenter(new UserByIdClass());
            userByUserIdPresenter.reqeust(userid, sessionid);
            if (mClickFragmentId == R.id.rb2){
                radioGroup.check(mClickFragmentId);
                mCurrentFragmentId = R.id.rb2;
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mfrag_01).hide(mfrag_03).show(mfrag_02).commit();
                mClickFragmentId = 0;
            }
        }else {
            if (mClickFragmentId == R.id.rb2){
                radioGroup.check(mCurrentFragmentId);
            }
            if(mCurrentFragmentId==R.id.rb2){
                mCurrentFragmentId=R.id.rb1;
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mfrag_02).hide(mfrag_03).show(mfrag_01).commit();
                radioGroup.check(mCurrentFragmentId);
            }
            mClickFragmentId = 0;
            rl1.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.GONE);
        }

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
            case R.id.qiandao:
                Intent intent7 = new Intent(HomeActivity.this,SignCalendarActivity.class);
                startActivity(intent7);
                break;
            case R.id.head:
                Intent intent8 = new Intent(HomeActivity.this,MyUserActivity.class);
                startActivity(intent8);
                break;
            case  R.id.rb1:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                mCurrentFragmentId = R.id.rb1;
                fragmentTransaction.hide(mfrag_02).hide(mfrag_03);
                fragmentTransaction.show(mfrag_01);
                fragmentTransaction.commit();
                break;
            case  R.id.rb2:
                //判断是否有登录用户
                boolean isLogin = WDApp.getShare().getBoolean("zai",false);
                if (isLogin){
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mCurrentFragmentId = R.id.rb2;
                    fragmentTransaction.hide(mfrag_01).hide(mfrag_03);
                    fragmentTransaction.show(mfrag_02);
                    fragmentTransaction.commit();
                }else{
                    mClickFragmentId = R.id.rb2;
                    intent(LoginActivity.class);
                }
                break;
            case  R.id.rb3:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                mCurrentFragmentId = R.id.rb3;
                fragmentTransaction.hide(mfrag_01).hide(mfrag_02);
                fragmentTransaction.show(mfrag_03);
                fragmentTransaction.commit();
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
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void eventbus(Message message){
        if(message.what==1){
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
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int intExtra = intent.getIntExtra("aaaa", 0);
        if(intExtra==3){
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            radioGroup.check(radioGroup.getChildAt(2).getId());
            fragmentTransaction.hide(mfrag_01).hide(mfrag_02);
            fragmentTransaction.show(mfrag_03);
            fragmentTransaction.commit();
        }

    }
}
