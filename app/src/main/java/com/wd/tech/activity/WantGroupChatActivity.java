package com.wd.tech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.ui.EaseChatFragment;
import com.wd.tech.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WantGroupChatActivity extends BaseActivity {


    @BindView(R.id.group_chat_name)
    TextView groupChatName;
    @BindView(R.id.group_chat_setting)
    ImageView groupChatSetting;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_want_group_chat;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("groupName");
        int groupId = intent.getIntExtra("groupId",0);
        groupChatName.setText(name);
        EaseChatFragment chatFragment = new EaseChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.group_ok, chatFragment).commit();
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


    @OnClick({R.id.group_chat_back, R.id.group_chat_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.group_chat_back:
                finish();
                break;
            case R.id.group_chat_setting:

                break;
        }
    }
}
