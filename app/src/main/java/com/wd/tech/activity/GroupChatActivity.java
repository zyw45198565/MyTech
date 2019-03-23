package com.wd.tech.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.FindGroupNoticePageListAdapter;
import com.wd.tech.bean.FindGroupNoticePageList;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.FindGroupNoticePageListPresenter;
import com.wd.tech.presenter.ReviewGroupApplyPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupChatActivity extends BaseActivity implements XRecyclerView.LoadingListener{


    @BindView(R.id.group_chat_xrecyclerview)
    XRecyclerView groupChatXrecyclerview;
    FindGroupNoticePageListPresenter presenter = new FindGroupNoticePageListPresenter(new GroupChatData());
    private FindGroupNoticePageListAdapter adapter;
    private int userid;
    private String session1d;
    private ReviewGroupApplyPresenter reviewGroupApplyPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_chat;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        reviewGroupApplyPresenter = new ReviewGroupApplyPresenter(new ReviewGroupApply());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        groupChatXrecyclerview.setLayoutManager(linearLayoutManager);
        adapter = new FindGroupNoticePageListAdapter(this);
        groupChatXrecyclerview.setAdapter(adapter);
        groupChatXrecyclerview.setLoadingListener(this);
        SharedPreferences share = WDApp.getShare();
        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");
        presenter.reqeust(userid,session1d,true,5);
        adapter.setOnClickListener(new FindGroupNoticePageListAdapter.ClickListener() {
            @Override
            public void clickOk(int id) {
                reviewGroupApplyPresenter.reqeust(userid,session1d,id,1);
            }

            @Override
            public void clickNo(int id) {
                reviewGroupApplyPresenter.reqeust(userid,session1d,id,2);
            }
        });
    }

    @Override
    protected void destoryData() {
        presenter.unBind();
        reviewGroupApplyPresenter.unBind();
    }


    @OnClick(R.id.group_chat_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onRefresh() {
        if (presenter.isRunning()){
            groupChatXrecyclerview.refreshComplete();
            return;
        }
        presenter.reqeust(userid,session1d,true,5);

    }

    @Override
    public void onLoadMore() {
        if (presenter.isRunning()){
            groupChatXrecyclerview.loadMoreComplete();
            return;
        }
        presenter.reqeust(userid,session1d,true,5);

    }

    class GroupChatData implements DataCall<Result<List<FindGroupNoticePageList>>> {

        @Override
        public void success(Result<List<FindGroupNoticePageList>> result) {
            groupChatXrecyclerview.refreshComplete();
            groupChatXrecyclerview.loadMoreComplete();
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
    class ReviewGroupApply implements DataCall<Result>{

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
