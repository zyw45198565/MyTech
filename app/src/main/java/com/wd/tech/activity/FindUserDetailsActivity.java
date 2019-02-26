package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.FindUser;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.CheckMyFriendPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.DateUtils;
import com.wd.tech.utils.util.UIUtils;
import com.wd.tech.utils.util.WDActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * date:2019/2/20 18:50
 * author:陈国星(陈国星)
 * function:
 */
public class FindUserDetailsActivity extends WDActivity {
    @BindView(R.id.user_details_back)
    ImageView userDetailsBack;
    @BindView(R.id.user_details_icon)
    SimpleDraweeView userDetailsIcon;
    @BindView(R.id.user_details_name)
    TextView userDetailsName;
    @BindView(R.id.user_details_integral)
    TextView userDetailsIntegral;
    @BindView(R.id.user_details_whetherVip)
    ImageView userDetailsWhetherVip;
    @BindView(R.id.user_details_signature)
    TextView userDetailsSignature;
    @BindView(R.id.user_details_sex)
    TextView userDetailsSex;
    @BindView(R.id.user_details_phone)
    TextView userDetailsPhone;
    @BindView(R.id.user_details_email)
    TextView userDetailsEmail;
    @BindView(R.id.user_details_yes)
    Button userDetailsYes;
    private int flag=2;//判断是否是好友 默认不是
    private CheckMyFriendPresenter checkMyFriendPresenter;
    private int userid;
    private String session1d;
    private FindUser findUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_user_details;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        findUser = (FindUser) intent.getSerializableExtra("findUser");
        if (findUser.getWhetherVip()!=1){
            userDetailsWhetherVip.setVisibility(View.GONE);
        }
        userDetailsIcon.setImageURI(Uri.parse(findUser.getHeadPic()));
        userDetailsName.setText(findUser.getNickName());

        userDetailsPhone.setText(findUser.getPhone());
        userDetailsIntegral.setText("("+ findUser.getIntegral()+"积分)");

        if (TextUtils.isEmpty(findUser.getSignature())){
            userDetailsSignature.setText("暂无个人签名");
        }else {
            userDetailsSignature.setText(findUser.getSignature());
        }
        if (TextUtils.isEmpty(findUser.getSignature())){
            userDetailsEmail.setText("暂无邮箱");
        }else {
            userDetailsEmail.setText(findUser.getEmail());
        }
        if (findUser.getSex()==1){
            try {
                userDetailsSex.setText("男");//+"("+ DateUtils.dateFormat(findUser.),DateUtils.MINUTE_PATTERN));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else {
            userDetailsSex.setText("女");
        }
        SharedPreferences share = WDApp.getShare();
        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");
        checkMyFriendPresenter = new CheckMyFriendPresenter(new CheckMyFriend());
        checkMyFriendPresenter.reqeust(userid,session1d, findUser.getUserId());

    }

    @Override
    protected void destoryData() {
        checkMyFriendPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.user_details_back, R.id.user_details_yes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_details_back:
                finish();
                break;
            case R.id.user_details_yes:
                if (flag==1){
                    UIUtils.showToastSafe("发消息");
                }else {
                    Intent intent = new Intent(FindUserDetailsActivity.this,WantAddFriendActivity.class);
                    intent.putExtra("findUser",findUser);
                    startActivity(intent);
                }
                break;
        }

    }
    class CheckMyFriend implements DataCall<Result>{

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){
                flag=data.getFlag();
                if (data.getFlag()==1){
                    userDetailsYes.setText("发消息");
                }else {
                    userDetailsYes.setText("添加好友");
                }
            }

        }

        @Override
        public void fail(ApiException e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}

