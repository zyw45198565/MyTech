package com.wd.tech.frag;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.drawee.view.SimpleDraweeView;
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
import com.wd.tech.activity.AdvertiseActivity;
import com.wd.tech.activity.DetailsActivity;
import com.wd.tech.activity.MenuActivity;
import com.wd.tech.activity.SearchActivity;
import com.wd.tech.adapter.HomeAllAdapter;
import com.wd.tech.bean.HomeAll;
import com.wd.tech.bean.MyBanner;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.AddCollectionPresenter;
import com.wd.tech.presenter.CancelCollectionPresenter;
import com.wd.tech.presenter.HomeAllPresenter;
import com.wd.tech.presenter.MyBannerPresenter;
import com.wd.tech.presenter.WxSharePresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.WDFragment;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Tech
 * @date 2019/2/18 20:28
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class Frag_01 extends WDFragment implements View.OnClickListener {


    @BindView(R.id.one_menu)
    ImageView oneMenu;
    @BindView(R.id.one_search)
    ImageView oneSearch;
    @BindView(R.id.one_banner)
    MZBannerView oneBanner;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.one_homeall)
    RecyclerView oneHomeall;
    private List<MyBanner> bannerList;
    private MyBannerPresenter myBannerPresenter;
    private String sessionid;
    private int userid;
    private HomeAllPresenter homeAllPresenter;
    private HomeAllAdapter homeAllAdapter;
    int page = 1;
    int count = 5;
    int plateId = 0;
    private List<HomeAll> result;
    private Dialog dialog;
    public static IWXAPI iwxapi;
    private IWXAPI wxapi;
    private int homealli;

    @Override
    public String getPageName() {
        return "Frag_资讯" +
                "";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_01;
    }


    @Override
    protected void initView() {

        oneMenu.setOnClickListener(this);
        oneSearch.setOnClickListener(this);
        myBannerPresenter = new MyBannerPresenter(new MyBannerCall());
        myBannerPresenter.reqeust();

        // 通过WXAPIFactory工厂，获取IWXAPI的实例  APP_ID为微信的AppID
        wxapi = WXAPIFactory.createWXAPI(getActivity(), "wx4c96b6b8da494224", true);

        // 将应用的appId注册到微信
        wxapi.registerApp("wx4c96b6b8da494224");
        //刷新
        myrefreshLayout();


        //展示列表
        homeallre();

        //收藏喜欢
        collectlove();
    }

    private void collectlove() {
        homeAllAdapter.xihuan(new HomeAllAdapter.Mylove() {
            @Override
            public void win(int id, int whetherCollection, int possion) {
                homealli = possion;
                if (whetherCollection==2){
                AddCollectionPresenter addCollectionPresenter = new AddCollectionPresenter(new MyCollect());
                addCollectionPresenter.reqeust(userid, sessionid, id);
                }else{
                    String eid = id + "";
                    CancelCollectionPresenter cancelCollectionPresenter = new CancelCollectionPresenter(new MyCancelCollect());
                    cancelCollectionPresenter.reqeust(userid, sessionid, eid);
                }
            }
        });
        /*homeAllAdapter.xihuan(new HomeAllAdapter.Mylove() {
            @Override
            public void win(int possion) {
                int id = result.get(possion).getId();
                AddCollectionPresenter addCollectionPresenter = new AddCollectionPresenter(new MyCollect());
                addCollectionPresenter.reqeust(userid, sessionid, id);
            }

            @Override
            public void abolish(int possion) {
                int id = result.get(possion).getId();
                String eid = id + "";
                CancelCollectionPresenter cancelCollectionPresenter = new CancelCollectionPresenter(new MyCancelCollect());
                cancelCollectionPresenter.reqeust(userid, sessionid, eid);
            }
        });*/
    }

    private void homeallre() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        oneHomeall.setLayoutManager(linearLayoutManager);
        homeAllAdapter = new HomeAllAdapter(getActivity());
        oneHomeall.setAdapter(homeAllAdapter);
        homeAllAdapter.sharecircle(new HomeAllAdapter.MyShare() {
            @Override
            public void share(final int possion) {
                dialog = new Dialog(getActivity(), R.style.DialogTheme);

                View view = View.inflate(getActivity(), R.layout.twoshare, null);
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
        WindowManager m = getActivity().getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65，根据实际情况调整
        p.height = (int) (d.getHeight() * 0.2);
        dialogWindow.setAttributes(p);
    }

    private void myrefreshLayout() {
        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                homeAllAdapter.clearAll();
                page = 1;
                homeAllPresenter.reqeust(userid, sessionid, plateId, page, count);
                refreshlayout.finishRefresh();
            }
        });
        //加载更多
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                homeAllPresenter.reqeust(userid, sessionid, plateId, page, count);
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

    public static class BannerViewHolder implements MZViewHolder<MyBanner> {
        private SimpleDraweeView mImageView;
        private TextView title;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (SimpleDraweeView) view.findViewById(R.id.banner_image);
            title = view.findViewById(R.id.title);
            return view;
        }

        @Override
        public void onBind(final Context context, int position, final MyBanner data) {
            // 数据绑定
            mImageView.setImageURI(data.getImageUrl());
            title.setText(data.getTitle());
            //判断跳转的网页类型
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String jumpUrl = data.getJumpUrl();
                    String[] split = jumpUrl.split(":");
                    if (split[0].equals("wd")) {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("zurl", 1);
                        intent.putExtra("classify", 1);
                        context.startActivity(intent);

                    } else {
                        Intent intent = new Intent(context, AdvertiseActivity.class);
                        intent.putExtra("zurl", data.getJumpUrl());
                        context.startActivity(intent);
                    }

                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one_menu:
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
                break;
            case R.id.one_search:
                Intent intent1 = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent1);
                break;


        }
    }

    public class MyBannerCall implements DataCall<Result<List<MyBanner>>> {

        @Override
        public void success(Result<List<MyBanner>> data) {
//            Toast.makeText(getContext(), data.getStatus(), Toast.LENGTH_SHORT).show();
            List<MyBanner> result = data.getResult();
            bannerList = new ArrayList<>();
            bannerList.addAll(result);
            // 设置数据
            oneBanner.setIndicatorVisible(false);
            oneBanner.setPages(bannerList, new MZHolderCreator<BannerViewHolder>() {
                @Override
                public BannerViewHolder createViewHolder() {
                    return new BannerViewHolder();
                }
            });
        }

        @Override
        public void fail(ApiException e) {
//           Toast.makeText(getContext(), e.getCode(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        oneBanner.pause();//暂停轮播
    }

    @Override
    public void onResume() {
        super.onResume();
        oneBanner.start();//开始轮播
        userid = WDApp.getShare().getInt("userid", 0);
        sessionid = WDApp.getShare().getString("sessionid", "");
        homeAllPresenter = new HomeAllPresenter(new HomeCall());

        homeAllPresenter.reqeust(userid, sessionid, plateId, page, count);
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
            Toast.makeText(getActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
            homeAllAdapter.notifyItemChanged(homealli);
//            homeAllAdapter.notifyDataSetChanged(homealli);

        }

        @Override
        public void fail(ApiException e) {

        }
    }

    //取消收藏
    private class MyCancelCollect implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(getActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
//            homeAllAdapter.notifyDataSetChanged(homealli);
            homeAllAdapter.notifyItemChanged(homealli);

            Log.i("shahahaha",homealli+"-------------------------------");

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
