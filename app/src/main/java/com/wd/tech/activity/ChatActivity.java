package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Conversation;
import com.wd.tech.bean.Result;
import com.wd.tech.greendao.ConversationDao;
import com.wd.tech.greendao.DaoUtils;
import com.wd.tech.presenter.FindConversationListPresenter;
import com.wd.tech.utils.AndroidBug5497Workaround;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChatActivity extends BaseActivity {


    @BindView(R.id.chat_name)
    TextView chatName;
    private int userid;
    private String session1d;
    private FindConversationListPresenter findConversationListPresenter;
    private String userNames;
    private Conversation conversation;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        AndroidBug5497Workaround.assistActivity(this);
        SharedPreferences share = WDApp.getShare();

        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");
        Intent intent = getIntent();
        userNames = intent.getStringExtra("userNames");

        //Log.d("123", "initView: "+userid+"/"+substring);
        findConversationListPresenter = new FindConversationListPresenter(new FindConversationList());
        findConversationListPresenter.reqeust(userid,session1d,userNames);
        //friendInfoList = (FriendInfoList) intent.getSerializableExtra("friendInfoList");


        EaseChatFragment chatFragment = new EaseChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.hx_ok, chatFragment).commit();

    }

    @Override
    protected void destoryData() {
        findConversationListPresenter.unBind();
    }

    @OnClick({R.id.chat_back, R.id.chat_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chat_back:
                finish();
                break;
            case R.id.chat_setting:
                Intent intent = new Intent(ChatActivity.this, ChatSettingsActivity.class);
                intent.putExtra("userId",conversation.getUserId());
                intent.putExtra("nickName",conversation.getNickName());
                intent.putExtra("headPic",conversation.getHeadPic());
                startActivity(intent);
                break;
        }
    }

    class FindConversationList implements DataCall<Result<List<Conversation>>>{

        @Override
        public void success(Result<List<Conversation>> data) {
            if (data.getStatus().equals("0000")){
                        conversation = data.getResult().get(0);
                 chatName.setText(conversation.getNickName());
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent!=null){
            setIntent(intent);
        }
    }
}
