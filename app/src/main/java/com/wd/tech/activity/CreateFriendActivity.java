package com.wd.tech.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.CreateGroupPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;
import com.wd.tech.utils.util.WDActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateFriendActivity extends WDActivity {


    @BindView(R.id.add_group_back)
    ImageView addGroupBack;
    @BindView(R.id.create_friend_name)
    EditText createFriendName;
    @BindView(R.id.create_friend_details)
    EditText createFriendDetails;
    @BindView(R.id.create_friend_ok)
    Button createFriendOk;
    private CreateGroupPresenter createGroupPresenter;
    private int userid;
    private String session1d;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_friend;
    }

    @Override
    protected void initView() {
        createGroupPresenter = new CreateGroupPresenter(new CreateGroup());
        SharedPreferences share = WDApp.getShare();
        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");

    }

    @Override
    protected void destoryData() {
        createGroupPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.add_group_back, R.id.create_friend_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_group_back:
                finish();
                break;
            case R.id.create_friend_ok:
                String name = createFriendName.getText().toString();
                String details = createFriendDetails.getText().toString();
                Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5]{2,20}$");

                Matcher matcher = pattern.matcher(name);

             if (!matcher.find()) {
                 UIUtils.showToastSafe("群名2-20汉字");
                 return;
        //不匹配
                }
                if (details.length()>100){
                    UIUtils.showToastSafe("100字以内");
                    return;
                }
            createGroupPresenter.reqeust(userid,session1d,name,details);
                break;
        }
    }
    class CreateGroup implements DataCall<Result> {

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
