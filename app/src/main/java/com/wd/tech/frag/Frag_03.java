package com.wd.tech.frag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.activity.PublishMyInvitationActivity;
import com.wd.tech.adapter.CommunityListAdapter;
import com.wd.tech.bean.FindCommunityList;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.AddCommunityGreatPresenter;
import com.wd.tech.presenter.FindCommunityListPresenter;
import com.wd.tech.presenter.WDPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;
import com.wd.tech.utils.util.WDFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Tech
 * @date 2019/2/18 20:28
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class Frag_03 extends WDFragment implements XRecyclerView.LoadingListener {

    @BindView(R.id.frag03xlv)
    XRecyclerView mFrag03xlv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout  refreshLayout;
    Unbinder unbinder;
    private FindCommunityListPresenter mFindCommunityListPresenter;
    private CommunityListAdapter mCommunityListAdapter;
    private SharedPreferences sp;
    private int userid;
    private String sessionid;
    private AddCommunityGreatPresenter addCommunityGreatPresenter;

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
        myrefreshLayout();
        sp = WDApp.getShare();//获取userId sessionId
        userid = sp.getInt("userid", 0);
        sessionid = sp.getString("sessionid", "");

        mFindCommunityListPresenter = new FindCommunityListPresenter(new CommunityListCall());//社区列表
        addCommunityGreatPresenter = new AddCommunityGreatPresenter(new CommunityGreatCall());//点赞

        mCommunityListAdapter = new CommunityListAdapter(getContext());

        mFrag03xlv.setLoadingListener(this);
        mFrag03xlv.setLoadingMoreEnabled(true);
        mFrag03xlv.setPullRefreshEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());//创建布局管理器
        mFrag03xlv.setLayoutManager(linearLayoutManager);
        mFrag03xlv.setAdapter(mCommunityListAdapter);//设置适配器

        mCommunityListAdapter.setClickOkListener(new CommunityListAdapter.ClickOkListener() {//点赞自定义接口回调
            @Override
            public void ClickOk(int id) {
                addCommunityGreatPresenter.reqeust(userid,sessionid,id);
            }
        });

        mFindCommunityListPresenter.reqeust(userid, sessionid, false, 5);
    }

    @OnClick(R.id.frag03_write_fresco)
    public void setFrag03Click(View view){
        switch (view.getId()){
            case R.id.frag03_write_fresco://点击跳转到发布贴子页面
                startActivity(new Intent(getContext(), PublishMyInvitationActivity.class));
                break;
        }
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
        mFindCommunityListPresenter.unBind();
        addCommunityGreatPresenter.unBind();
    }

    private void myrefreshLayout() {//刷新加载更多定义
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


    @Override
    public void onRefresh() {
        mFrag03xlv.refreshComplete();
        mCommunityListAdapter.remove();
        mCommunityListAdapter.notifyDataSetChanged();
        mFindCommunityListPresenter.reqeust(userid, sessionid, false, 5);
    }

    @Override
    public void onLoadMore() {
        mFrag03xlv.loadMoreComplete();
        mFindCommunityListPresenter.reqeust(userid, sessionid, true, 5);
    }

    private class CommunityListCall implements DataCall<Result<List<FindCommunityList>>> {//社区列表展示
        @Override
        public void success(Result<List<FindCommunityList>> data) {
            if (data.getStatus().equals("0000")) {
                List<FindCommunityList> result = data.getResult();
                for (int i = 0; i < result.size(); i++) {
                    FindCommunityList findCommunityList = result.get(i);
                    mCommunityListAdapter.addList(findCommunityList);//添加数据到适配器中
                    mCommunityListAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("社区列表展示：  " + e.getMessage());
        }
    }

    private class CommunityGreatCall implements DataCall<Result> {//点赞
        @Override
        public void success(Result data) {
            if(data.getStatus().equals("0000")){
                UIUtils.showToastSafe("社区点赞：  " + data.getMessage());
                mFindCommunityListPresenter.reqeust(userid, sessionid, false, 5);
            }
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("社区点赞：  " + e.getMessage());
        }
    }
}
