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
import com.wd.tech.presenter.FindConversationListPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChatActivity extends BaseActivity {


    @BindView(R.id.chat_name)
    TextView chatName;
    private String myHxId;
    private String headPic;
    private String nickName;
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
        SharedPreferences share = WDApp.getShare();
        myHxId = share.getString("userName", "");
        headPic = share.getString("headPic", "");
        nickName = share.getString("nickName", "");
        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");
        Intent intent = getIntent();
        userNames = intent.getStringExtra("userNames");

        //Log.d("123", "initView: "+userid+"/"+session1d);
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
                intent.putExtra("conversation",conversation);
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
        if (username.equals(myHxId.toLowerCase())){
            easeUser.setNickname(nickName);
            easeUser.setAvatar(headPic);
        }else {
            //根据username查询数据库，设置数据
            easeUser.setNickname(conversation.getNickName());
            easeUser.setAvatar(conversation.getHeadPic());
        }
        return easeUser;
    }//即可正常显示头像昵称
    class FindConversationList implements DataCall<Result<List<Conversation>>>{

        @Override
        public void success(Result<List<Conversation>> data) {
            if (data.getStatus().equals("0000")){
                        conversation = data.getResult().get(0);
                 chatName.setText(conversation.getNickName());
                setEaseUser();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
