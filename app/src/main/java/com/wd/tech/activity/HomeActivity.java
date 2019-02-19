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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.frag.Frag_01;
import com.wd.tech.frag.Frag_02;
import com.wd.tech.frag.Frag_03;
import com.wd.tech.utils.util.WDActivity;

public class HomeActivity extends WDActivity {


    private Frag_01 frag_01;
    private Frag_02 frag_02;
    private Frag_03 frag_03;
    private FragmentTransaction fragmentTransaction;
    private LinearLayout mlinearhome;
    private LinearLayout mLinear;
    private DrawerLayout mdraw;
    private RelativeLayout rl1;
    private LinearLayout ll1;

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
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void destoryData() {

    }
}
