package com.wd.tech.activity;

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

import java.util.List;

public class MytaskActivity extends BaseActivity {


    private int userid;
    private String sessionid;
    private Button qian;
    private Button ping;
    private Button tie;
    private Button fenxiang;
    private Button chakan;
    private Button wanshan;
    private Button bangding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mytask;
    }

    @Override
    protected void initView() {
        userid = WDApp.getShare().getInt("userid", 1);
        sessionid = WDApp.getShare().getString("sessionid", "");

        qian = (Button) findViewById(R.id.qian);
        ping = (Button) findViewById(R.id.ping);
        tie = (Button) findViewById(R.id.tie);
        fenxiang = (Button) findViewById(R.id.fenxiang);
        chakan = (Button) findViewById(R.id.chakan);
        wanshan = (Button) findViewById(R.id.wanshan);
        bangding = (Button) findViewById(R.id.bangding);

        UserTaskPresenter userTaskPresenter = new UserTaskPresenter(new  UserTaskCall());
        userTaskPresenter.reqeust(userid,sessionid);
    }

    @Override
    protected void destoryData() {

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
