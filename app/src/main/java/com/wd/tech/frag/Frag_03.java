package com.wd.tech.frag;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wd.tech.R;
import com.wd.tech.adapter.CommunityListAdapter;
import com.wd.tech.bean.FindCommunityList;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.FindCommunityListPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.WDFragment;

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
public class Frag_03 extends WDFragment implements XRecyclerView.LoadingListener {

    @BindView(R.id.frag03xlv)
    XRecyclerView frag03xlv;
    @BindView(R.id.frag03_write_fresco)
    SimpleDraweeView frag03WriteFresco;
    Unbinder unbinder;
    private FindCommunityListPresenter findCommunityListPresenter;
    private CommunityListAdapter communityListAdapter;

    @Override
    public String getPageName() {
        return "Frag_社区";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_03;
    }

    @Override
    protected void initView() {
        //todo 缺少获取userId sessionId
        findCommunityListPresenter = new FindCommunityListPresenter(new CommunityListCall());
        communityListAdapter = new CommunityListAdapter(getContext());

        frag03xlv.setLoadingListener(this);
        frag03xlv.setLoadingMoreEnabled(true);
        frag03xlv.setPullRefreshEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());//创建布局管理器
        frag03xlv.setLayoutManager(linearLayoutManager);
        frag03xlv.setAdapter(communityListAdapter);//设置适配器

//        findCommunityListPresenter.reqeust();//todo 没有请求userId,sessionId,false,5
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        frag03xlv.refreshComplete();
        communityListAdapter.remove();
        communityListAdapter.notifyDataSetChanged();
//        findCommunityListPresenter.reqeust();//todo 没有请求userId,sessionId,false,5
    }

    @Override
    public void onLoadMore() {
        frag03xlv.loadMoreComplete();
//        findCommunityListPresenter.reqeust();//todo 没有请求userId,sessionId,true,5
    }

    private class CommunityListCall implements DataCall<Result<List<FindCommunityList>>> {
        @Override
        public void success(Result<List<FindCommunityList>> data) {
            if(data.getStatus().equals("0000")){

            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
