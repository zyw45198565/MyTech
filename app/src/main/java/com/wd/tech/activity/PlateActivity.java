package com.wd.tech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.HomeAllAdapter;
import com.wd.tech.bean.HomeAll;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.HomeAllPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.WDActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
板块列表
 */
public class PlateActivity extends WDActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.platename)
    TextView platename;
    @BindView(R.id.one_search)
    ImageView oneSearch;
    @BindView(R.id.one_homeall)
    RecyclerView oneHomeall;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private HomeAllPresenter homeAllPresenter;
    private HomeAllAdapter homeAllAdapter;
    private int userid;
    private String sessionid;
    int page = 1;
    int count = 5;
    private String mname;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plate;
    }

    @Override
    protected void initView() {
        userid = WDApp.getShare().getInt("userid", 0);
        sessionid = WDApp.getShare().getString("sessionid", "");

        final Intent intent = getIntent();
        mname = intent.getStringExtra("mname");
        int mid = intent.getIntExtra("mid", 0);
        //列表
        homeAllPresenter = new HomeAllPresenter(new HomeCall());

        homeAllPresenter.reqeust(userid, sessionid, mid, page, count);

        //刷新
        myrefreshLayout();
        //展示列表
        homeallre();

        //点击按钮
        myOnclick();

    }

    private void myrefreshLayout() {
        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh();
            }
        });
        //加载更多
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
            }
        });

//        开始下拉
        refreshLayout.setEnableRefresh(true);//启用刷新
        refreshLayout.setEnableLoadmore(true);//启用加载
//        关闭下拉
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();
    }

    private void myOnclick() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        platename.setText(mname);
        oneSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PlateActivity.this, SearchActivity.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void destoryData() {

    }

    private void homeallre() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        oneHomeall.setLayoutManager(linearLayoutManager);
        homeAllAdapter = new HomeAllAdapter(this);
        oneHomeall.setAdapter(homeAllAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private class HomeCall implements DataCall<Result<List<HomeAll>>> {
        @Override
        public void success(Result<List<HomeAll>> data) {
            List<HomeAll> result = data.getResult();
            if (result.size() > 0) {
                homeAllAdapter.addAll(result);
                homeAllAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

}
