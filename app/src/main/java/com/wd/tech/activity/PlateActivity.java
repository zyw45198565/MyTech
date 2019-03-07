package com.wd.tech.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.HomeAllAdapter;
import com.wd.tech.bean.HomeAll;
import com.wd.tech.bean.Result;
import com.wd.tech.frag.Frag_01;
import com.wd.tech.presenter.AddCollectionPresenter;
import com.wd.tech.presenter.CancelCollectionPresenter;
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
    int count = 10;
    private String mname;
    private Dialog dialog;
    private List<HomeAll> result;
    private IWXAPI wxapi;
    private int homei;
    private int mid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plate;
    }

    @Override
    protected void initView() {
        if (WDApp.getShare().getBoolean("zai",false)) {
            userid = WDApp.getShare().getInt("userid", 0);
            sessionid = WDApp.getShare().getString("sessionid", "");
        }else{
            userid=0;
            sessionid="";
        }
        // 通过WXAPIFactory工厂，获取IWXAPI的实例  APP_ID为微信的AppID
        wxapi = WXAPIFactory.createWXAPI(PlateActivity.this, "wx4c96b6b8da494224", true);

        // 将应用的appId注册到微信
        wxapi.registerApp("wx4c96b6b8da494224");
        final Intent intent = getIntent();
        mname = intent.getStringExtra("mname");
        mid = intent.getIntExtra("mid", 0);
        //列表
        homeAllPresenter = new HomeAllPresenter(new HomeCall());

        homeAllPresenter.reqeust(userid, sessionid, mid, page, count);
        homeAllAdapter = new HomeAllAdapter(this);

        //刷新
        myrefreshLayout();
        //展示列表
        homeallre();

        //点击按钮
        myOnclick();
        //收藏喜欢
        collectlove();
    }

    private void collectlove() {
        homeAllAdapter.xihuan(new HomeAllAdapter.Mylove() {
            @Override
            public void win(int id, int whetherCollection, int possion) {
                homei = possion;
                if (whetherCollection == 2) {
                    AddCollectionPresenter addCollectionPresenter = new AddCollectionPresenter(new MyCollect());
                    addCollectionPresenter.reqeust(userid, sessionid, id);
                } else {
                    String eid = id + "";
                    CancelCollectionPresenter cancelCollectionPresenter = new CancelCollectionPresenter(new MyCancelCollect());
                    cancelCollectionPresenter.reqeust(userid, sessionid, eid);
                }
            }
        });
    }

    private void myrefreshLayout() {
        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                homeAllAdapter.clearAll();
                page = 1;
                homeAllPresenter.reqeust(userid, sessionid, mid, page, count);
                refreshlayout.finishRefresh();
            }
        });
        //加载更多
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                homeAllPresenter.reqeust(userid, sessionid, mid, page, count);
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
        oneHomeall.setAdapter(homeAllAdapter);
        homeAllAdapter.sharecircle(new HomeAllAdapter.MyShare() {
            @Override
            public void share(final int possion) {
                dialog = new Dialog(PlateActivity.this, R.style.DialogTheme);

                View view = View.inflate(PlateActivity.this, R.layout.twoshare, null);
                dialog.setContentView(view);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.show();
                getshoud();
                TextView cancle = view.findViewById(R.id.cancel);
                ImageView wxfriend = view.findViewById(R.id.wxfriend);
                ImageView weixinf = view.findViewById(R.id.weixinf);
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                wxfriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //微信朋友圈
                        WeChatShare(possion, 1);
                    }
                });
                weixinf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //微信朋友圈
                        WeChatShare(possion, 2);
                    }
                });


            }
        });

    }

    private void getshoud() {
        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65，根据实际情况调整
        p.height = (int) (d.getHeight() * 0.2);
        dialogWindow.setAttributes(p);
    }

    private class HomeCall implements DataCall<Result<List<HomeAll>>> {
        @Override
        public void success(Result<List<HomeAll>> data) {
            result = data.getResult();
            if (result.size() > 0) {
                homeAllAdapter.addAll(result);
                homeAllAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    //收藏
    private class MyCollect implements DataCall<Result> {
        @Override
        public void success(Result data) {
            homeAllAdapter.notifyItemChanged(homei);

            Toast.makeText(PlateActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    //取消收藏
    private class MyCancelCollect implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(PlateActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
            homeAllAdapter.notifyItemChanged(homei);

        }

        @Override
        public void fail(ApiException e) {

        }
    }


    //分享链接
    public void WeChatShare(int possion, int classify) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.hooxiao.com";

        initSend(webpage, possion, classify);
    }

    private void initSend(WXMediaMessage.IMediaObject webpage, int possion, int classify) {
        WXMediaMessage msg = new WXMediaMessage();
        msg.title = result.get(possion).getTitle();
        msg.description = result.get(possion).getSummary();
        msg.mediaObject = webpage;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        if (classify == 1) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;    //设置发送到朋友圈
        } else if (classify == 2) {
            req.scene = SendMessageToWX.Req.WXSceneSession;    //设置发送到朋友
        }
        wxapi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
