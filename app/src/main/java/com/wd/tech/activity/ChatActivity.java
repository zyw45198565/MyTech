package com.wd.tech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.easeui.ui.EaseChatFragment;
import com.wd.tech.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChatActivity extends BaseActivity {


    @BindView(R.id.chat_name)
    TextView chatName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        int friendUid = intent.getIntExtra("friendUid",0);
        chatName.setText(name);
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

                break;
        }
    }
}
