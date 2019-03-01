package com.wd.tech.activity;

import android.content.Intent;

import com.wd.tech.R;
import com.wd.tech.bean.FriendInfoList;

public class ChatSettingsActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_settings;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        FriendInfoList  friendInfoList = (FriendInfoList) intent.getSerializableExtra("friendInfoList");
    }

    @Override
    protected void destoryData() {

    }
}
