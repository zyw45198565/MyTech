package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.GroupByUser;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.DisbandGroupPresenter;
import com.wd.tech.presenter.FindGroupsByUserIdPresenter;
import com.wd.tech.presenter.RetreatPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * date:2019/2/20 18:50
 * author:陈国星(陈国星)
 * function:
 */
public class GroupDetailsSettingsActivity extends BaseActivity {


    @BindView(R.id.group_details_setting_back)
    ImageView groupDetailsSettingBack;
    @BindView(R.id.group_details_setting_name)
    TextView groupDetailsSettingName;
    @BindView(R.id.group_details_setting_icon)
    SimpleDraweeView groupDetailsSettingIcon;
    @BindView(R.id.group_details_setting_yes)
    Button groupDetailsSettingYes;
    private int flag = 2;//判断是否是群主 默认不是
    private int userid;
    private String session1d;
    private int groupId;
    private String name;
    private String icon;
    private FindGroupsByUserIdPresenter findGroupsByUserIdPresenter;
    private DisbandGroupPresenter disbandGroupPresenter;
    private RetreatPresenter retreatPresenter;
    private PopupWindow window;
    private PopupWindow clearWindow;
    private View parent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_details_settings;
    }

    @Override
    protected void initView() {
        parent = View.inflate(this,R.layout.activity_group_details_settings,null);
        disbandGroupPresenter = new DisbandGroupPresenter(new DisbandGroup());
        retreatPresenter = new RetreatPresenter(new DisbandGroup());
        findGroupsByUserIdPresenter = new FindGroupsByUserIdPresenter(new FindGroupsByUserId());
        Intent intent = getIntent();
        name = intent.getStringExtra("groupName");
        groupId = intent.getIntExtra("groupId",0);
        icon = intent.getStringExtra("icon");
        SharedPreferences share = WDApp.getShare();
        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");
        groupDetailsSettingIcon.setImageURI(Uri.parse(icon));
        groupDetailsSettingName.setText(name);
        getShow();
    }

    @Override
    protected void destoryData() {
        findGroupsByUserIdPresenter.unBind();
        disbandGroupPresenter.unBind();
        retreatPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.group_details_setting_back, R.id.group_details_setting_icon, R.id.group_details_setting_member, R.id.group_details_setting_intro, R.id.group_details_setting_message, R.id.group_details_setting_manage, R.id.group_details_setting_chat, R.id.group_details_setting_yes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.group_details_setting_back:
                finish();
                break;
            case R.id.group_details_setting_icon:

                break;
            case R.id.group_details_setting_member:

                break;
            case R.id.group_details_setting_intro:
                Intent intent_i = new Intent(GroupDetailsSettingsActivity.this,GroupIntroActivity.class);
                intent_i.putExtra("groupId",groupId);
                startActivity(intent_i);
                break;
            case R.id.group_details_setting_message:
                Intent intent_g = new Intent(GroupDetailsSettingsActivity.this, GroupChatActivity.class);
                startActivity(intent_g);
                break;
            case R.id.group_details_setting_manage:
                Intent intent = new Intent(GroupDetailsSettingsActivity.this,GroupMemberActivity.class);
                intent.putExtra("groupId",groupId);
                startActivity(intent);
                break;
            case R.id.group_details_setting_chat:

                break;
            case R.id.group_details_setting_yes:
                if(flag==1){
                   // UIUtils.showToastSafe("解散群组");
                    window.showAtLocation(parent, Gravity.BOTTOM,0,0);
                }else {
                    //UIUtils.showToastSafe("退群");
                    clearWindow.showAtLocation(parent, Gravity.BOTTOM,0,0);
                }
                break;
        }
    }


    class DisbandGroup implements DataCall<Result> {

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                window.dismiss();
                clearWindow.dismiss();
            finish();
            }
            UIUtils.showToastSafe(data.getMessage());
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    class FindGroupsByUserId implements DataCall<Result<List<GroupByUser>>> {

        @Override
        public void success(Result<List<GroupByUser>> data) {
            if (data.getStatus().equals("0000")){
                List<GroupByUser> result = data.getResult();
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).getGroupId()==groupId){
                        flag=1;
                    }
                }
                if (flag==1){
                    groupDetailsSettingYes.setText("解散群组");
                }else {
                    groupDetailsSettingYes.setText("退群");
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
        findGroupsByUserIdPresenter.reqeust(userid,session1d);
    }
    private void getShow(){
        View inflate = View.inflate(this, R.layout.popu_delete_group, null);
        window = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setTouchable(true);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        getWindow(inflate);
        View clearView = View.inflate(this, R.layout.popu_retreat_group, null);
        clearWindow = new PopupWindow(clearView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        clearWindow.setTouchable(true);
        clearWindow.setFocusable(true);
        clearWindow.setOutsideTouchable(true);
        clearWindow.setBackgroundDrawable(new BitmapDrawable());
        getClearWindow(clearView);
    }

    private void getClearWindow(View clearView) {
        LinearLayout popuRetreatGroupOk = clearView.findViewById(R.id.popu_retreat_group_ok);
        LinearLayout popuRetreatGroupNo = clearView.findViewById(R.id.popu_retreat_group_no);
        popuRetreatGroupOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //UIUtils.showToastSafe("退群");
               retreatPresenter.reqeust(userid,session1d,groupId);
            }
        });
        popuRetreatGroupNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearWindow.dismiss();
            }
        });
    }

    private void getWindow(View inflate) {
        LinearLayout popuDeleteGroupOk = inflate.findViewById(R.id.popu_delete_group_ok);
        LinearLayout popuDeleteGroupNo = inflate.findViewById(R.id.popu_delete_group_no);
        popuDeleteGroupOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //UIUtils.showToastSafe("解散群组");
                disbandGroupPresenter.reqeust(userid,session1d,groupId);
            }
        });
        popuDeleteGroupNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
    }
}

