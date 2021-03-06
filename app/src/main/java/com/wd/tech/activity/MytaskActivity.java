package com.wd.tech.activity;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.UserTaskBean;
import com.wd.tech.presenter.UserTaskPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MytaskActivity extends BaseActivity implements View.OnClickListener {


    private int userid;
    private String sessionid;
    private Button qian;
    private Button ping;
    private Button tie;
    private Button fenxiang;
    private Button chakan;
    private Button wanshan;
    private Button bangding;
    private UserTaskPresenter userTaskPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mytask;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        userid = WDApp.getShare().getInt("userid", 1);
        sessionid = WDApp.getShare().getString("sessionid", "");

        qian = (Button) findViewById(R.id.qian);
        qian.setOnClickListener(this);
        ping = (Button) findViewById(R.id.ping);
        ping.setOnClickListener(this);
        tie = (Button) findViewById(R.id.tie);
        tie.setOnClickListener(this);
        fenxiang = (Button) findViewById(R.id.fenxiang);
        fenxiang.setOnClickListener(this);
        chakan = (Button) findViewById(R.id.chakan);
        chakan.setOnClickListener(this);
        wanshan = (Button) findViewById(R.id.wanshan);
        wanshan.setOnClickListener(this);
        bangding = (Button) findViewById(R.id.bangding);
        bangding.setOnClickListener(this);
        userTaskPresenter = new UserTaskPresenter(new UserTaskCall());

    }

    @Override
    protected void destoryData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        userTaskPresenter.reqeust(userid,sessionid);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qian:
                Intent intent7 = new Intent(MytaskActivity.this,SignCalendarActivity.class);
                startActivity(intent7);
                finish();
                break;
            case R.id.ping:
                Intent intent8 = new Intent(MytaskActivity.this,HomeActivity.class);
                intent8.putExtra("aaaa",3);
                startActivity(intent8);
                finish();
                break;
            case R.id.tie:
                Intent intent9 = new Intent(MytaskActivity.this,PublishMyInvitationActivity.class);
                startActivity(intent9);
                finish();
                break;
            case R.id.fenxiang:
                Intent intent12 = new Intent(MytaskActivity.this,HomeActivity.class);
                intent12.putExtra("aaaa",1);
                startActivity(intent12);
                finish();
                break;
            case R.id.chakan:
                Intent intent13 = new Intent(MytaskActivity.this,HomeActivity.class);
                intent13.putExtra("aaaa",1);
                startActivity(intent13);
                finish();
                break;
            case R.id.wanshan:
                Intent intent10 = new Intent(MytaskActivity.this,MyWanshanActivity.class);
                startActivity(intent10);
                finish();
                break;
            case R.id.bangding:
                Intent intent11 = new Intent(MytaskActivity.this,MySettingActivity.class);
                startActivity(intent11);
                finish();
                break;
        }
    }

    private class UserTaskCall implements DataCall<Result<List<UserTaskBean>>> {
        @Override
        public void success(Result<List<UserTaskBean>> data) {
            List<UserTaskBean> result = data.getResult();
            for (int i = 0; i < result.size(); i++) {
                    int status = result.get(i).getStatus();
                    if(result.get(i).getTaskId()==1001&&status == 1) {
                            qian.setEnabled(false);
                            qian.setText("已完成");
                            qian.setTextColor(0xffffffff);
                    }else  if(result.get(i).getTaskId()==1002&&status == 1) {
                        ping.setEnabled(false);
                        ping.setText("已完成");
                        ping.setTextColor(0xffffffff);
                    }else  if(result.get(i).getTaskId()==1003&&status == 1) {
                        tie.setEnabled(false);
                        tie.setText("已完成");
                        tie.setTextColor(0xffffffff);
                    }else  if(result.get(i).getTaskId()==1004&&status == 1) {
                        fenxiang.setEnabled(false);
                        fenxiang.setText("已完成");
                        fenxiang.setTextColor(0xffffffff);
                    }else  if(result.get(i).getTaskId()==1005&&status == 1) {
                        chakan.setEnabled(false);
                        chakan.setText("已完成");
                        chakan.setTextColor(0xffffffff);
                    }else  if(result.get(i).getTaskId()==1006&&status == 1) {
                        wanshan.setEnabled(false);
                        wanshan.setText("已完成");
                        wanshan.setTextColor(0xffffffff);
                    }else  if(result.get(i).getTaskId()==1007&&status == 1) {
                        bangding.setEnabled(false);
                        bangding.setText("已完成");
                        bangding.setTextColor(0xffffffff);
                    }

            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
