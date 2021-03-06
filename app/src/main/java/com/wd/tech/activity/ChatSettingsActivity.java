package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Conversation;
import com.wd.tech.bean.FriendInfoList;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.DeleteChatRecordPresenter;
import com.wd.tech.presenter.DeleteFriendRelationPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatSettingsActivity extends BaseActivity {
    @BindView(R.id.chat_settings_icon)
    SimpleDraweeView chatSettingsIcon;
    @BindView(R.id.chat_settings_nickname)
    TextView chatSettingsNickname;
    @BindView(R.id.chat_settings_name)
    TextView chatSettingsName;
    @BindView(R.id.chat_settings_group)
    RelativeLayout chatSettingsGroup;
    @BindView(R.id.chat_settings_chatting_records)
    RelativeLayout chatSettingsChattingRecords;
    @BindView(R.id.chat_settings_clear_records)
    RelativeLayout chatSettingsClearRecords;
    @BindView(R.id.chat_settings_delete)
    Button chatSettingsDelete;
    private PopupWindow window;
    private PopupWindow clearWindow;
    private View inflate;
    private DeleteFriendRelationPresenter deleteFriendRelationPresenter;
    private int userid;
    private String session1d;
    private View parent;
    private DeleteChatRecordPresenter deleteChatRecordPresenter;
    private int friendId;
    private String headPic;
    private String nickName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_settings;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        parent = View.inflate(this,R.layout.activity_chat_settings,null);
        deleteChatRecordPresenter = new DeleteChatRecordPresenter(new DeleteChatRecord());
        Intent intent = getIntent();
        friendId = intent.getIntExtra("userId", 0);
        headPic = intent.getStringExtra("headPic");
        nickName = intent.getStringExtra("nickName");
        chatSettingsIcon.setImageURI(headPic);
        chatSettingsName.setText(nickName);
        chatSettingsNickname.setText(nickName);
        deleteFriendRelationPresenter = new DeleteFriendRelationPresenter(new DeleteFriendRelation());
        SharedPreferences share = WDApp.getShare();
        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");

        getShow();

    }

    @Override
    protected void destoryData() {
        deleteFriendRelationPresenter.unBind();
        deleteChatRecordPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.chat_settings_back, R.id.chat_settings_icon, R.id.chat_settings_group, R.id.chat_settings_chatting_records, R.id.chat_settings_clear_records, R.id.chat_settings_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chat_settings_back:
                finish();
                break;
            case R.id.chat_settings_icon:

                break;
            case R.id.chat_settings_group:
                Intent intent = new Intent(ChatSettingsActivity.this,CheckGroupActivity.class);
                intent.putExtra("friendId",friendId);
                startActivity(intent);
                break;
            case R.id.chat_settings_chatting_records:

                break;
            case R.id.chat_settings_clear_records:
                clearWindow.showAtLocation(parent, Gravity.BOTTOM,0,0);
                break;
            case R.id.chat_settings_delete:
                window.showAtLocation(parent, Gravity.BOTTOM,0,0);
                break;
        }

        }

    private void getShow(){
        inflate = View.inflate(this, R.layout.popu_delete_friend, null);
        window = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setTouchable(true);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        getWindow(inflate);
        View clearView = View.inflate(this, R.layout.popu_clear_chat, null);
        clearWindow = new PopupWindow(clearView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        clearWindow.setTouchable(true);
        clearWindow.setFocusable(true);
        clearWindow.setOutsideTouchable(true);
        clearWindow.setBackgroundDrawable(new BitmapDrawable());
        getClearWindow(clearView);
    }

    private void getClearWindow(View clearView) {
        LinearLayout popuClearChatOk= clearView.findViewById(R.id.popu_clear_chat_ok);
        LinearLayout popuClearChatNo= clearView.findViewById(R.id.popu_clear_chat_no);
        popuClearChatOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteChatRecordPresenter.reqeust(userid,session1d,friendId);

            }
        });
        popuClearChatNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clearWindow.dismiss();
            }
        });
    }

    private void getWindow(View inflate) {
        TextView popuDeleteMessage= inflate.findViewById(R.id.popu_delete_message);
        LinearLayout popuDeleteOk= inflate.findViewById(R.id.popu_delete_ok);
        LinearLayout popuDeleteNo= inflate.findViewById(R.id.popu_delete_no);
        popuDeleteMessage.setText("将联系人 "+nickName+" 删除，同时删除与该联系人的聊天记录" );
        popuDeleteOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFriendRelationPresenter.reqeust(userid,session1d,friendId);
            }
        });
        popuDeleteNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                window.dismiss();
            }
        });
    }
    class DeleteFriendRelation implements DataCall<Result>{

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                window.dismiss();
                UIUtils.showToastSafe(data.getMessage());
                finish();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    class DeleteChatRecord implements DataCall<Result>{

        @Override
        public void success(Result data) {
            clearWindow.dismiss();
            if (data.getStatus().equals("0000")){

            }
            UIUtils.showToastSafe(data.getMessage());
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
