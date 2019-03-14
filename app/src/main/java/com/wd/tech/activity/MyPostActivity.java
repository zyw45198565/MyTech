package com.wd.tech.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.LoveAdapter;
import com.wd.tech.adapter.MyPostAdapter;
import com.wd.tech.bean.MyLoveBean;
import com.wd.tech.bean.MyPostByIdBean;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.CanceFollowPresenter;
import com.wd.tech.presenter.DeletePostPresenter;
import com.wd.tech.presenter.MyLovePresenter;
import com.wd.tech.presenter.MyPostByIdPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

public class MyPostActivity extends BaseActivity  implements DataCall<Result<List<MyPostByIdBean>>>{


    private int userid;
    private String sessionid;
    int page=1;
    int count=5;
    List<MyPostByIdBean> loveBeans = new ArrayList<>();
    private MyPostAdapter loveAdapter;
    private MyPostByIdPresenter myLovePresenter;
    private ImageView meiyou;
    private RecyclerView recycler;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_post;
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
        loveAdapter = new MyPostAdapter(this,loveBeans);
        loveAdapter.getShan(new MyPostAdapter.Shan() {
            @Override
            public void onShan(final int i) {
                final Dialog dialog = new Dialog(MyPostActivity.this,R.style.DialogTheme);
                View inflate = View.inflate(MyPostActivity.this, R.layout.mypost_dialog, null);
                dialog.setContentView(inflate);
                Window dialogWindow = dialog.getWindow();
                dialogWindow.setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高
                lp.width=d.getWidth();
                dialogWindow.setAttributes(lp);
                dialog.show();

                Button queren = inflate.findViewById(R.id.queren);
                queren.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeletePostPresenter deletePostPresenter = new DeletePostPresenter(new DeletePostCall());
                        deletePostPresenter.reqeust(userid,sessionid,loveBeans.get(i).getId()+"");
                        dialog.dismiss();
                    }
                });

                Button quxiao = inflate.findViewById(R.id.quxiao);
                quxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        recycler.setAdapter(loveAdapter);
        myLovePresenter = new MyPostByIdPresenter(this);
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
    public void success(Result<List<MyPostByIdBean>> data) {
        List<MyPostByIdBean> result = data.getResult();
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

    private class DeletePostCall implements DataCall<Result> {
        @Override
        public void success(Result data) {
            page=1;
            loveBeans.clear();
            myLovePresenter.reqeust(userid,sessionid,page,count);
            Toast.makeText(MyPostActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
