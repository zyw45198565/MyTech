package com.wd.tech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.HomeAllAdapter;
import com.wd.tech.bean.HomeAll;
import com.wd.tech.bean.Result;
import com.wd.tech.frag.Frag_01;
import com.wd.tech.presenter.HomeAllPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntegralActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.one_recycle)
    RecyclerView oneRecycle;
    @BindView(R.id.t_integral)
    TextView tIntegral;
    @BindView(R.id.my_integral)
    TextView myIntegral;
    @BindView(R.id.convert)
    TextView convert;
    private HomeAllPresenter homeAllPresenter;
    private HomeAllAdapter homeAllAdapter;
    private int userid;
    private String sessionid;
    int page=1;
    int count =1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_integral;
    }

    @Override
    protected void initView() {
        userid = WDApp.getShare().getInt("userid", 0);
        sessionid = WDApp.getShare().getString("sessionid", "");

        Intent intent = getIntent();
        int zid = intent.getIntExtra("zid", 0);

        homeAllPresenter = new HomeAllPresenter(new HomeCall());

        homeAllPresenter.reqeust(userid, sessionid, zid, page, count);

        oneRecycle.setLayoutManager(new LinearLayoutManager(this));
        oneRecycle.setAdapter(homeAllAdapter);

    }

    @Override
    protected void destoryData() {

    }
    private class HomeCall implements DataCall<Result<List<HomeAll>>> {
        @Override
        public void success(Result<List<HomeAll>> data) {
            List<HomeAll> result = data.getResult();
            if(result.size()>0){
                homeAllAdapter.addAll(result);
                homeAllAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

}
