package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.hyphenate.easeui.EaseConstant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.FindGroupsByUserIdAdapter;
import com.wd.tech.bean.Conversation;
import com.wd.tech.bean.GroupByUser;
import com.wd.tech.bean.Result;
import com.wd.tech.greendao.DaoUtils;
import com.wd.tech.presenter.FindUserJoinedGroupPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.SpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindGroupsByUserIdActivity extends BaseActivity {
    @BindView(R.id.find_groups_back)
    ImageView findGroupsBack;
    @BindView(R.id.find_groups_recycle)
    RecyclerView findGroupsRecycle;
    @BindView(R.id.find_groups_smart)
    SmartRefreshLayout findGroupsSmart;
    private int userid;
    private String session1d;
    private FindUserJoinedGroupPresenter findGroupsByUserIdPresenter;
    private FindGroupsByUserIdAdapter findGroupsByUserIdAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_findgroups_byuser;

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findGroupsByUserIdPresenter = new FindUserJoinedGroupPresenter(new FindGroupByUserId());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        findGroupsRecycle.addItemDecoration(new SpacingItemDecoration(20));
        findGroupsRecycle.setLayoutManager(linearLayoutManager);
        findGroupsByUserIdAdapter = new FindGroupsByUserIdAdapter();
        findGroupsRecycle.setAdapter(findGroupsByUserIdAdapter);
        //开始下拉
        findGroupsSmart.setEnableRefresh(true);//启用刷新
        findGroupsSmart.setEnableLoadmore(false);//启用加载
        //刷新
        findGroupsSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                findGroupsByUserIdPresenter.reqeust(userid, session1d);
            }
        });
        findGroupsByUserIdAdapter.setOnItemClickListener(new FindGroupsByUserIdAdapter.ClickListener() {
            @Override
            public void click(int id, String name,String xh,String icon) {
                Intent intent = new Intent(FindGroupsByUserIdActivity.this, WantGroupChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,xh);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                intent.putExtra("userNames",xh);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void destoryData() {
        findGroupsByUserIdPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.find_groups_back)
    public void onViewClicked() {
        finish();
    }
    //初始化好友列表
    class FindGroupByUserId implements DataCall<Result<List<GroupByUser>>> {

        @Override
        public void success(Result<List<GroupByUser>> data) {
            if (data.getStatus().equals("0000")){
                findGroupsByUserIdAdapter.setList(data.getResult());
                findGroupsByUserIdAdapter.notifyDataSetChanged();
/*
                List<Conversation> conversationList = new ArrayList<>();
                for (int i = 0; i < data.getResult().size(); i++) {
                    Conversation conversation = new Conversation();
                    conversation.setUserName(data.getResult().get(i).getHxGroupId().toLowerCase());
                    conversation.setNickName(data.getResult().get(i).getGroupName());
                    conversation.setHeadPic(data.getResult().get(i).getGroupImage());
                    conversationList.add(conversation);
                }

                for(Conversation conversation:conversationList){
                    conversation.setUserName(conversation.getUserName().toLowerCase());
                }
                DaoUtils.getInstance().getConversationDao().insertOrReplaceInTx(conversationList);*/

                findGroupsSmart.finishRefresh();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences share = WDApp.getShare();
        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");
        findGroupsByUserIdPresenter.reqeust(userid, session1d);
    }
}