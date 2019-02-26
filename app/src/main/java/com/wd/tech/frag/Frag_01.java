package com.wd.tech.frag;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.WDFragment;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

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
    int page=1;
    int count =5;
    int plateId=0;
    private List<HomeAll> result;

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
        userid = WDApp.getShare().getInt("userid", 0);
        sessionid = WDApp.getShare().getString("sessionid", "");

        oneMenu.setOnClickListener(this);
        oneSearch.setOnClickListener(this);
        myBannerPresenter = new MyBannerPresenter(new MyBannerCall());
        myBannerPresenter.reqeust();

        homeAllPresenter = new HomeAllPresenter(new HomeCall());

        homeAllPresenter.reqeust(userid, sessionid, plateId, page, count);

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
            public void win(int possion) {
                int id = result.get(possion).getId();
                AddCollectionPresenter addCollectionPresenter=new AddCollectionPresenter(new MyCollect());
                addCollectionPresenter.reqeust(userid,sessionid,id);
            }

            @Override
            public void abolish(int possion) {
                int id = result.get(possion).getId();
                String eid=id+"";
                CancelCollectionPresenter cancelCollectionPresenter=new CancelCollectionPresenter(new MyCancelCollect());
                cancelCollectionPresenter.reqeust(userid,sessionid,eid);
            }
        });
    }

    private void homeallre() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        oneHomeall.setLayoutManager(linearLayoutManager);
        homeAllAdapter = new HomeAllAdapter(getActivity());
        oneHomeall.setAdapter(homeAllAdapter);
    }

    private void myrefreshLayout() {
        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                homeAllAdapter.clearAll();
                page=1;
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
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,AdvertiseActivity.class);
                    intent.putExtra("zurl",data.getJumpUrl());
                    context.startActivity(intent);
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
                Intent intent1=new Intent(getActivity(),SearchActivity.class);
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
    }

    private class HomeCall implements DataCall<Result<List<HomeAll>>> {
        @Override
        public void success(Result<List<HomeAll>> data) {
            result = data.getResult();
            if(result.size()>0){
                homeAllAdapter.addAll(result);
                homeAllAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    private class MyCollect implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(getActivity(),data.getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    private class MyCancelCollect implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(getActivity(),data.getMessage(),Toast.LENGTH_SHORT).show();

        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
