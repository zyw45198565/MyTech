package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.FindUser;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.ApplyAddGroupPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WantAddGroupActivity extends BaseActivity {

    @BindView(R.id.want_add_group_back)
    ImageView wantAddGroupBack;
    @BindView(R.id.want_add_group_send)
    TextView wantAddGroupSend;
    @BindView(R.id.want_add_group_message)
    EditText wantAddGroupMessage;
    @BindView(R.id.want_add_group_num)
    TextView wantAddGroupNum;
    private int userId;
    private String sessionId;
    private ApplyAddGroupPresenter applyAddGroupPresenter;
    private int groupId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_want_add_group;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupId",0);

        SharedPreferences share = WDApp.getShare();
        userId = share.getInt("userid", 0);
        sessionId = share.getString("sessionid", "");
        applyAddGroupPresenter = new ApplyAddGroupPresenter(new ApplyAddGroup());
        wantAddGroupMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                wantAddGroupNum.setText(i+"字");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void destoryData() {
        applyAddGroupPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.want_add_group_back, R.id.want_add_group_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.want_add_group_back:
                finish();
                break;
            case R.id.want_add_group_send:
                String remark = wantAddGroupMessage.getText().toString();
                applyAddGroupPresenter.reqeust(userId,sessionId,groupId,remark);
                break;
        }
    }

    class ApplyAddGroup implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                UIUtils.showToastSafe(data.getMessage());
                finish();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
