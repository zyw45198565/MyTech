package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.ui.EaseChatFragment;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.GroupByUser;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.FindUserJoinedGroupPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WantGroupChatActivity extends BaseActivity {


    @BindView(R.id.group_chat_name)
    TextView groupChatName;
    @BindView(R.id.group_chat_setting)
    ImageView groupChatSetting;
    private int userid;
    private String userNames;
    private String session1d;
    private FindUserJoinedGroupPresenter findUserJoinedGroupPresenter;
    private GroupByUser groupByUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_want_group_chat;
    }

    @Override
    protected void initView() {
        findUserJoinedGroupPresenter = new FindUserJoinedGroupPresenter(new FindGroupsByUserId());
        Intent intent = getIntent();
        userNames = intent.getStringExtra("userNames");

        EaseChatFragment chatFragment = new EaseChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.group_ok, chatFragment).commit();

    }

    @Override
    protected void destoryData() {
        findUserJoinedGroupPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.group_chat_back, R.id.group_chat_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.group_chat_back:
                finish();
                break;
            case R.id.group_chat_setting:
                Intent intent = new Intent(WantGroupChatActivity.this,GroupDetailsSettingsActivity.class);
                intent.putExtra("groupId",groupByUser.getGroupId());
                intent.putExtra("groupName",groupByUser.getGroupName());
                intent.putExtra("icon",groupByUser.getGroupImage());
                startActivity(intent);
                break;
        }
    }
    class FindGroupsByUserId implements DataCall<Result<List<GroupByUser>>> {

        @Override
        public void success(Result<List<GroupByUser>> data) {
            if (data.getStatus().equals("0000")){
                List<GroupByUser> result = data.getResult();
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).getHxGroupId().equals(userNames)){
                        groupByUser = result.get(i);
                        groupChatName.setText(groupByUser.getGroupName());
                    }else {

                    }
                }

            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences share = WDApp.getShare();
        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");
        findUserJoinedGroupPresenter.reqeust(userid,session1d);
    }

}
