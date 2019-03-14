package com.wd.tech.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.publish.IntegralRecordAdapter;
import com.wd.tech.bean.IntegralRecordBean;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.UserintegralBean;
import com.wd.tech.presenter.IntegralRecordPresenter;
import com.wd.tech.presenter.SignDaysPresenter;
import com.wd.tech.presenter.UserIntegralPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

public class MyintegralActivity extends BaseActivity {


    private TextView integral;
    private int userid;
    private String sessionid;
    private TextView lianxu;
    List<IntegralRecordBean> integralRecordBeans = new ArrayList<>();
    private IntegralRecordAdapter integralRecordAdapter;
    int page = 1;
    int count = 5;
    private IntegralRecordPresenter integralRecordPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myintegral;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        userid = WDApp.getShare().getInt("userid", 1);
        sessionid = WDApp.getShare().getString("sessionid", "");

        integral = (TextView) findViewById(R.id.integral);
        lianxu = (TextView) findViewById(R.id.lianxu);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        integralRecordAdapter = new IntegralRecordAdapter(this,integralRecordBeans);
        recycler.setAdapter(integralRecordAdapter);

        UserIntegralPresenter userIntegralPresenter = new UserIntegralPresenter(new UserIntegralCall());
        userIntegralPresenter.reqeust(userid,sessionid);
        SignDaysPresenter signDaysPresenter = new SignDaysPresenter(new SignDaysCall());
        signDaysPresenter.reqeust(userid,sessionid);
        integralRecordPresenter = new IntegralRecordPresenter(new IngtegralRecordCall());
        integralRecordPresenter.reqeust(userid,sessionid,page,count);

        SmartRefreshLayout mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh);
        mRefreshLayout.setEnableRefresh(true);//启用刷新
        mRefreshLayout.setEnableLoadmore(true);//启用加载

        //刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page=1;
                integralRecordBeans.clear();
                integralRecordPresenter.reqeust(userid,sessionid,page,count);
                refreshlayout.finishRefresh();
            }
        });
        //加载更多
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                integralRecordPresenter.reqeust(userid,sessionid,page,count);
                refreshlayout.finishLoadmore();
            }
        });

    }

    @Override
    protected void destoryData() {

    }

    private class  UserIntegralCall implements DataCall<Result<UserintegralBean>> {
        @Override
        public void success(Result<UserintegralBean> data) {
            integral.setText(data.getResult().getAmount()+"");
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    private class  SignDaysCall implements DataCall<Result<Integer>> {
        @Override
        public void success(Result<Integer> data) {
            lianxu.setText("您已连续签到"+data.getResult()+"天");
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    private class  IngtegralRecordCall implements DataCall<Result<List<IntegralRecordBean>>> {
        @Override
        public void success(Result<List<IntegralRecordBean>> data) {
            integralRecordBeans.addAll(data.getResult());
            integralRecordAdapter.notifyDataSetChanged();
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
