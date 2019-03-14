package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.QunChengYuanAdapter;
import com.wd.tech.bean.GroupMember;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.FindGroupMemberListPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupMemberShowActivity extends BaseActivity {
    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.x_recycler)
    RecyclerView xRecycler;
    private QunChengYuanAdapter qunChengYuanAdapter;
    private int userId;
    private int groupId;
    private String sessionId;
    private FindGroupMemberListPresenter findGroupMemberListPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_members;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findGroupMemberListPresenter = new FindGroupMemberListPresenter(new FindGroupMember());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        xRecycler.setLayoutManager(linearLayoutManager);
        qunChengYuanAdapter = new QunChengYuanAdapter(this);
        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupId", 0);
        SharedPreferences share = WDApp.getShare();
        userId = share.getInt("userid", 0);
        sessionId = share.getString("sessionid", "");
        findGroupMemberListPresenter.reqeust(userId, sessionId, groupId);
        xRecycler.setAdapter(qunChengYuanAdapter);
    }

    @Override
    protected void destoryData() {
        findGroupMemberListPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.group_members_back)
    public void onViewClicked() {
        finish();
    }
    class FindGroupMember implements DataCall<Result<List<GroupMember>>>{

        @Override
        public void success(Result<List<GroupMember>> data) {
            if (data.getStatus().equals("0000")){
                List<GroupMember> result = data.getResult();
                qunChengYuanAdapter.addItem(result);
                qunChengYuanAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
