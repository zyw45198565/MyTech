package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.FindGroup;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.WhetherInGroupPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * date:2019/2/20 18:50
 * author:陈国星(陈国星)
 * function:
 */
public class FindGroupDetailsActivity extends BaseActivity {

    @BindView(R.id.find_group_details_back)
    ImageView findGroupDetailsBack;
    @BindView(R.id.find_group_details_name)
    TextView findGroupDetailsName;
    @BindView(R.id.find_group_details_icon)
    SimpleDraweeView findGroupDetailsIcon;
    @BindView(R.id.find_group_details_num)
    TextView findGroupDetailsNum;
    @BindView(R.id.find_group_details_group)
    RelativeLayout findGroupDetailsGroup;
    @BindView(R.id.find_group_details_description)
    TextView findGroupDetailsDescription;
    @BindView(R.id.find_group_details_yes)
    Button findGroupDetailsYes;
    private int flag = 2;//判断是否是群成员 默认不是
    private int userid;
    private String session1d;
    private FindGroup findGroup;
    private WhetherInGroupPresenter whetherInGroupPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_group_details;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        findGroup = (FindGroup) intent.getSerializableExtra("findGroup");

        findGroupDetailsIcon.setImageURI(Uri.parse(findGroup.getGroupImage()));
        findGroupDetailsName.setText(findGroup.getGroupName());

        findGroupDetailsNum.setText("共"+ findGroup.getCurrentCount()+"人");

        if (TextUtils.isEmpty(findGroup.getDescription())){
            findGroupDetailsDescription.setText("暂无简介");
        }else {
            findGroupDetailsDescription.setText(findGroup.getDescription());
        }

        SharedPreferences share = WDApp.getShare();
        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");
        whetherInGroupPresenter = new WhetherInGroupPresenter(new WhetherInGroup());
        whetherInGroupPresenter.reqeust(userid,session1d,findGroup.getGroupId());

    }

    @Override
    protected void destoryData() {
        whetherInGroupPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.find_group_details_back, R.id.find_group_details_group, R.id.find_group_details_yes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.find_group_details_back:
                finish();
                break;
            case R.id.find_group_details_group:

                break;
            case R.id.find_group_details_yes:
                if (flag == 1) {
                    UIUtils.showToastSafe("发消息");
                } else {
                    Intent intent = new Intent(FindGroupDetailsActivity.this,WantAddGroupActivity.class);
                    intent.putExtra("groupId",findGroup.getGroupId());
                    startActivity(intent);
                }
                break;
        }
    }

    class WhetherInGroup implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                flag = data.getFlag();
                if (data.getFlag() == 1) {
                    findGroupDetailsYes.setText("发消息");
                } else {
                    findGroupDetailsYes.setText("申请加群");
                }
            }

        }

        @Override
        public void fail(ApiException e) {

        }
    }


}

