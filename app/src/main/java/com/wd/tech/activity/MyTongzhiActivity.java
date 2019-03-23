package com.wd.tech.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.LoveAdapter;
import com.wd.tech.adapter.SysNoticeAdapter;
import com.wd.tech.bean.MyLoveBean;
import com.wd.tech.bean.MyTongzhiBean;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.CanceFollowPresenter;
import com.wd.tech.presenter.MyLovePresenter;
import com.wd.tech.presenter.SysNoticePresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

public class MyTongzhiActivity extends BaseActivity implements DataCall<Result<List<MyTongzhiBean>>> {


    private int userid;
    private String sessionid;
    int page=1;
    int count=5;
    List<MyTongzhiBean> loveBeans = new ArrayList<>();
    private SysNoticeAdapter loveAdapter;
    private SysNoticePresenter myLovePresenter;
    private ImageView meiyou;
    private RecyclerView recycler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_tongzhi;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userid = WDApp.getShare().getInt("userid", 0);
        sessionid = WDApp.getShare().getString("sessionid", "");
        SmartRefreshLayout mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        meiyou = (ImageView) findViewById(R.id.meiyou);
        //开始下拉
        mRefreshLayout.setEnableRefresh(true);//启用刷新
        mRefreshLayout.setEnableLoadmore(true);//启用加载
        recycler.setLayoutManager(new LinearLayoutManager(this));
        loveAdapter = new SysNoticeAdapter(this,loveBeans);
        recycler.setAdapter(loveAdapter);
        myLovePresenter = new SysNoticePresenter(this);
        myLovePresenter.reqeust(userid,sessionid,page,count);


        //刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page=1;
                loveBeans.clear();
                myLovePresenter.reqeust(userid,sessionid,page,count);
                refreshlayout.finishRefresh();
            }
        });
        //加载更多
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                myLovePresenter.reqeust(userid,sessionid,page,count);
                refreshlayout.finishLoadmore();
            }
        });

    }

    @Override
    protected void destoryData() {

    }



    @Override
    public void success(Result<List<MyTongzhiBean>> data) {
        List<MyTongzhiBean> result = data.getResult();
        loveBeans.addAll(result);
        if(loveBeans.size()==0){
            meiyou.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        }
        loveAdapter.notifyDataSetChanged();
    }

    @Override
    public void fail(ApiException e) {

    }


}
