package com.wd.tech.frag;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.activity.MenuActivity;
import com.wd.tech.bean.HomeAll;
import com.wd.tech.bean.MyBanner;
import com.wd.tech.bean.Result;
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
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    private List<String> bannerList;
    private MyBannerPresenter myBannerPresenter;

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
        //刷新
        myrefreshLayout();
        HomeAllPresenter homeAllPresenter=new HomeAllPresenter(new HomeCall());

    }

    private void myrefreshLayout() {
        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                /*mData.clear();
                mNameAdapter.notifyDataSetChanged();*/
                refreshlayout.finishRefresh();
            }
        });
        //加载更多
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                /*for(int i=0;i<30;i++){
                    mData.add("小明"+i);
                }
                mNameAdapter.notifyDataSetChanged();*/
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

    public static class BannerViewHolder implements MZViewHolder<String> {
        private SimpleDraweeView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (SimpleDraweeView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
            // 数据绑定
            mImageView.setImageURI(Uri.parse(data));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one_menu:
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
                break;


        }
    }

    private class MyBannerCall implements DataCall<Result<List<MyBanner>>> {

        @Override
        public void success(Result<List<MyBanner>> data) {
            Toast.makeText(getContext(), data.getStatus(), Toast.LENGTH_SHORT).show();
            List<MyBanner> result = data.getResult();
            bannerList = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                String imageUrl = result.get(i).getImageUrl();
                String[] split = imageUrl.split("\\?");

                bannerList.add(split[0]);
            }
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
            Toast.makeText(getContext(), e.getCode(), Toast.LENGTH_SHORT).show();
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

        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
