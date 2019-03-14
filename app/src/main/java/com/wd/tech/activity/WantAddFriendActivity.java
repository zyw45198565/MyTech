package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.FindUser;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.AddFriendPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WantAddFriendActivity extends BaseActivity {
    @BindView(R.id.want_add_friend_back)
    ImageView wantAddFriendBack;
    @BindView(R.id.want_add_friend_send)
    TextView wantAddFriendSend;
    @BindView(R.id.want_add_friend_icon)
    SimpleDraweeView wantAddFriendIcon;
    @BindView(R.id.want_add_friend_name)
    TextView wantAddFriendName;
    @BindView(R.id.want_add_friend_signature)
    TextView wantAddFriendSignature;
    @BindView(R.id.want_add_friend_message)
    EditText wantAddFriendMessage;
    private AddFriendPresenter addFriendPresenter;
    private int userId;
    private String sessionId;
    private FindUser findUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_want_add_frient;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        findUser = (FindUser) intent.getSerializableExtra("findUser");
        wantAddFriendIcon.setImageURI(Uri.parse(findUser.getHeadPic()));
        wantAddFriendName.setText(findUser.getNickName());

        if (TextUtils.isEmpty(findUser.getSignature())){
            wantAddFriendSignature.setText("暂无个人签名");
        }else {
            wantAddFriendSignature.setText(findUser.getSignature());
        }
        SharedPreferences share = WDApp.getShare();
        userId = share.getInt("userid", 0);
        sessionId = share.getString("sessionid", "");
        addFriendPresenter = new AddFriendPresenter(new AddFriends());

    }

    @Override
    protected void destoryData() {
        addFriendPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.want_add_friend_back, R.id.want_add_friend_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.want_add_friend_back:
                finish();
                break;
            case R.id.want_add_friend_send:
                String remark = wantAddFriendMessage.getText().toString();
                addFriendPresenter.reqeust(userId,sessionId,findUser.getUserId(),remark);
                break;
        }
    }
    class AddFriends implements DataCall<Result>{

        @Override
        public void success(Result data) {
            UIUtils.showToastSafe(data.getMessage());
            /*if (data.getStatus().equals("0000")){
                finish();
            }*/
            finish();
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
