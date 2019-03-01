package com.wd.tech.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.UserintegralBean;
import com.wd.tech.presenter.ByIntegralPresenter;
import com.wd.tech.presenter.UserIntegralPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;


import butterknife.BindView;
import butterknife.ButterKnife;

public class IntegralActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.t_integral)
    TextView tIntegral;
    @BindView(R.id.my_integral)
    TextView myIntegral;
    @BindView(R.id.convert)
    TextView convert;
    @BindView(R.id.simple)
    SimpleDraweeView simple;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.writer)
    TextView writer;
    @BindView(R.id.like)
    CheckBox like;
    @BindView(R.id.likenum)
    TextView likenum;
    @BindView(R.id.sharenum)
    TextView sharenum;
    private int userid;
    private String sessionid;
    int page = 1;
    int count = 1;
    int plateId = 1;
    private int did;
    private int integralCost;
    private ByIntegralPresenter byIntegralPresenter;
    private UserIntegralPresenter userIntegralPresenter;
    private Dialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_integral;
    }

    @Override
    protected void initView() {
        userid = WDApp.getShare().getInt("userid", 0);
        sessionid = WDApp.getShare().getString("sessionid", "");

        //设置详情信息
        mydetails();
        //点击
        myclick();
        dialog = new Dialog(IntegralActivity.this, R.style.DialogTheme);

    }

    private void myclick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byIntegralPresenter = new ByIntegralPresenter(new ByIntegral());
                byIntegralPresenter.reqeust(userid,sessionid,did,integralCost);
            }
        });

    }

    private void mydetails() {
        Intent intent = getIntent();
        did = intent.getIntExtra("did", 0);
        String title1 = intent.getStringExtra("title");
        String thumbnail = intent.getStringExtra("thumbnail");
        String source = intent.getStringExtra("source");
        int whetherCollection = intent.getIntExtra("whetherCollection", 0);
        int share = intent.getIntExtra("share", 0);
        String summary = intent.getStringExtra("summary");
        simple.setImageURI(thumbnail);
        title.setText(title1);
        Log.i("title", title1);
        writer.setText(source);
        if (whetherCollection == 1) {
            like.setChecked(true);
        } else {
            like.setChecked(false);
        }

        sharenum.setText(share + "");
        content.setText(summary);
        integralCost = intent.getIntExtra("integralCost", 0);
        tIntegral.setText(integralCost + "分");
    }

    @Override
    protected void destoryData() {
        byIntegralPresenter=null;
        userIntegralPresenter=null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        userIntegralPresenter = new UserIntegralPresenter(new UserIntegral());
        userIntegralPresenter.reqeust(userid,sessionid);
    }

    private class ByIntegral implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(IntegralActivity.this,data.getMessage(),Toast.LENGTH_SHORT).show();
            if(data.getStatus().equals("0000")){
                setResult(1,new Intent());
                View view = View.inflate(IntegralActivity.this, R.layout.buy_success, null);
                dialog.setContentView(view);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.show();
                getshoud();

//                finish();
            }


        }

        @Override
        public void fail(ApiException e) {

        }
    }
    private void getshoud() {
        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65，根据实际情况调整
        p.height = (int) (d.getHeight() * 0.65);
        dialogWindow.setAttributes(p);
    }

    private class UserIntegral implements DataCall<Result<UserintegralBean>> {
        @Override
        public void success(Result<UserintegralBean> data) {
            int amount = data.getResult().getAmount();
            //我的积分
            myIntegral.setText(amount+"分");
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
