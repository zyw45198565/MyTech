package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.FriendInfoList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hyphenate.easeui.utils.EaseUserUtils.getUserInfo;


public class ChatActivity extends BaseActivity {


    @BindView(R.id.chat_name)
    TextView chatName;
    private FriendInfoList friendInfoList;
    private String userName;
    private String headPic;
    private String nickName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        SharedPreferences share = WDApp.getShare();
        userName = share.getString("userName", "");
        headPic = share.getString("headPic", "");
        nickName = share.getString("nickName", "");
        Intent intent = getIntent();
        friendInfoList = (FriendInfoList) intent.getSerializableExtra("friendInfoList");
        //chatName.setText(friendInfoList.getNickName());

        EaseChatFragment chatFragment = new EaseChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.hx_ok, chatFragment).commit();

    }

    @Override
    protected void destoryData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.chat_back, R.id.chat_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chat_back:
                finish();
                break;
            case R.id.chat_setting:
                Intent intent = new Intent(ChatActivity.this, ChatSettingsActivity.class);
                intent.putExtra("friendInfoList",friendInfoList);
                startActivity(intent);
                break;
        }
    }
    private void setEaseUser() {
        EaseUI easeUI = EaseUI.getInstance();
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });
    }

    private EaseUser getUserInfo(String username) {
        EaseUser easeUser = new EaseUser(username);
        if (username.equals(userName)){
            easeUser.setNickname(nickName);
            easeUser.setAvatar(headPic);
        }else {

            easeUser.setNickname(friendInfoList.getNickName());
            easeUser.setAvatar(friendInfoList.getHeadPic());
        }
        return easeUser;
    }//即可正常显示头像昵称
}
