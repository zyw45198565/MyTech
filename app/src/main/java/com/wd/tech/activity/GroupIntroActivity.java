package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.ModifyGroupDescriptionPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupIntroActivity extends BaseActivity {
    @BindView(R.id.group_intro_message)
    EditText groupIntroMessage;
    private int userId;
    private int groupId;
    private String sessionId;
    private ModifyGroupDescriptionPresenter modifyGroupDescriptionPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_intro;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupId", 0);
        SharedPreferences share = WDApp.getShare();
        userId = share.getInt("userid", 0);
        sessionId = share.getString("sessionid", "");
        modifyGroupDescriptionPresenter = new ModifyGroupDescriptionPresenter(new ModifyGroupDescription());

    }

    @Override
    protected void destoryData() {
        modifyGroupDescriptionPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.group_intro_ok)
    public void onViewClicked() {
        String s = groupIntroMessage.getText().toString();
        if (TextUtils.isEmpty(s)){
            UIUtils.showToastSafe("简介不能为空");
            return;
        }
        modifyGroupDescriptionPresenter.reqeust(userId,sessionId,groupId,s);
    }
    class ModifyGroupDescription implements DataCall<Result>{

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                UIUtils.showToastSafe(data.getMessage());
                finish();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
