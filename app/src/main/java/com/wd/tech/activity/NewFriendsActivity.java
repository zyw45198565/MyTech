package com.wd.tech.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.FindFriendNoticePageList;
import com.wd.tech.bean.Result;
import com.wd.tech.adapter.FindFriendNoticePageListAdapter;
import com.wd.tech.presenter.FindFriendNoticePageListPresenter;
import com.wd.tech.presenter.ReviewFriendApplyPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NewFriendsActivity extends BaseActivity implements XRecyclerView.LoadingListener{


    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerviewFriend;

    private FindFriendNoticePageListAdapter adapter;
    FindFriendNoticePageListPresenter presenter = new FindFriendNoticePageListPresenter(new FriendData());
    private int userid;
    private String session1d;
    private ReviewFriendApplyPresenter reviewFriendApplyPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_friends;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        reviewFriendApplyPresenter = new ReviewFriendApplyPresenter(new ReviewFriendApply());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        xrecyclerviewFriend.setLayoutManager(linearLayoutManager);
        adapter = new FindFriendNoticePageListAdapter(this);
        xrecyclerviewFriend.setAdapter(adapter);
        xrecyclerviewFriend.setLoadingListener(this);
        SharedPreferences share = WDApp.getShare();
        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");
        presenter.reqeust(userid,session1d,true,5);
        adapter.setOnClickListener(new FindFriendNoticePageListAdapter.ClickListener() {
            @Override
            public void clickOk(int id) {
            reviewFriendApplyPresenter.reqeust(userid,session1d,id,2);
            }

            @Override
            public void clickNo(int id) {
                reviewFriendApplyPresenter.reqeust(userid,session1d,id,3);

            }
        });
    }

    @Override
    protected void destoryData() {
        reviewFriendApplyPresenter.unBind();
        presenter.unBind();
    }

    @OnClick(R.id.new_firend_back)
    public void onViewClicked() {
        finish();
    }


    @Override
    public void onRefresh() {
        if (presenter.isRunning()){
            xrecyclerviewFriend.refreshComplete();
            return;
        }
        presenter.reqeust(userid,session1d,true,5);
    }

    @Override
    public void onLoadMore() {
        if (presenter.isRunning()){
            xrecyclerviewFriend.loadMoreComplete();
            return;
        }
        presenter.reqeust(userid,session1d,true,5);
    }

    class FriendData implements DataCall<Result<List<FindFriendNoticePageList>>> {
        @Override
        public void success(Result<List<FindFriendNoticePageList>> result) {
            xrecyclerviewFriend.refreshComplete();
            xrecyclerviewFriend.loadMoreComplete();
            adapter.clear();
            if (result.getStatus().equals("0000")){
                adapter.addAll(result.getResult());
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void fail(ApiException e) {

        }


    }
    class ReviewFriendApply implements DataCall<Result>{

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                UIUtils.showToastSafe(data.getMessage());
                presenter.reqeust(userid,session1d,true,5);
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
