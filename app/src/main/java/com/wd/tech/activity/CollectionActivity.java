package com.wd.tech.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.CollectionAdapter;
import com.wd.tech.adapter.LoveAdapter;
import com.wd.tech.bean.CollectionBean;
import com.wd.tech.bean.MyLoveBean;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.CanceFollowPresenter;
import com.wd.tech.presenter.CancelCollectionPresenter;
import com.wd.tech.presenter.CollectionPresenter;
import com.wd.tech.presenter.MyLovePresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends BaseActivity implements DataCall<Result<List<CollectionBean>>> {

    private int userid;
    private String sessionid;
    int page = 1;
    int count = 5;
    List<CollectionBean> loveBeans = new ArrayList<>();
    private CollectionAdapter loveAdapter;
    private CollectionPresenter collectionPresenter;
    private ImageView meiyou;
    private RecyclerView recycler;
    private TextView wancheng;
    private ImageView shan;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
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
        loveAdapter = new CollectionAdapter(this, loveBeans);
        recycler.setAdapter(loveAdapter);
        collectionPresenter = new CollectionPresenter(this);
        collectionPresenter.reqeust(userid, sessionid, page, count);


        //刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                loveBeans.clear();
                collectionPresenter.reqeust(userid, sessionid, page, count);
                refreshlayout.finishRefresh();
            }
        });
        //加载更多
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                collectionPresenter.reqeust(userid, sessionid, page, count);
                refreshlayout.finishLoadmore();
            }
        });

        shan = (ImageView) findViewById(R.id.shan);
        shan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shan.setVisibility(View.GONE);
                wancheng.setVisibility(View.VISIBLE);
                for (int i = 0; i < loveBeans.size(); i++) {
                    loveBeans.get(i).setIscheck(true);
                }
                loveAdapter.notifyDataSetChanged();
            }
        });
        wancheng = (TextView) findViewById(R.id.wancheng);
        wancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wancheng.setVisibility(View.GONE);
                shan.setVisibility(View.VISIBLE);

                String infoId="";
                for (int i = 0; i < loveBeans.size(); i++) {
                    boolean ischeck = loveBeans.get(i).isIscheck2();
                    if(ischeck){
                        int infoId1 = loveBeans.get(i).getInfoId();
                        infoId+=infoId1+",";
                    }
                }
                if(infoId.equals("")){

                }else {
                    CancelCollectionPresenter cancelCollectionPresenter = new CancelCollectionPresenter(new CancelCall());
                    cancelCollectionPresenter.reqeust(userid,sessionid,infoId);
                }

                for (int i = 0; i < loveBeans.size(); i++) {
                    loveBeans.get(i).setIscheck2(false);
                }

                for (int i = 0; i < loveBeans.size(); i++) {
                    loveBeans.get(i).setIscheck(false);
                }
                loveAdapter.notifyDataSetChanged();

            }
        });


    }

    @Override
    protected void destoryData() {

    }

    @Override
    public void success(Result<List<CollectionBean>> data) {
        List<CollectionBean> result = data.getResult();
        loveBeans.addAll(result);
        if (loveBeans.size() == 0) {
            meiyou.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        }
        loveAdapter.notifyDataSetChanged();
    }

    @Override
    public void fail(ApiException e) {

    }


    private class  CancelCall implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(CollectionActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            page=1;
            loveBeans.clear();
            collectionPresenter.reqeust(userid, sessionid, page, count);
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
