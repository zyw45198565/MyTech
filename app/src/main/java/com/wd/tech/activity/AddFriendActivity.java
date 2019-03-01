package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.FindGroup;
import com.wd.tech.bean.FindUser;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.FindGroupInfoPresenter;
import com.wd.tech.presenter.FindUserByPhonePresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;
import com.wd.tech.utils.util.WDActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * date:2019/2/20 14:26
 * author:陈国星(陈国星)
 * function:
 */
public class AddFriendActivity extends WDActivity {
    @BindView(R.id.add_friend_back)
    ImageView addFriendBack;
    @BindView(R.id.find_r)
    TextView findR;
    @BindView(R.id.find_r_ok)
    View findROk;
    @BindView(R.id.find_q)
    TextView findQ;
    @BindView(R.id.find_q_ok)
    View findQOk;
    @BindView(R.id.find_search_edit)
    EditText findSearchEdit;
    @BindView(R.id.find_r_icon)
    SimpleDraweeView findRIcon;
    @BindView(R.id.find_r_name)
    TextView findRName;
    @BindView(R.id.find_r_next)
    ImageView findRNext;
    @BindView(R.id.find_r_relative)
    RelativeLayout findRRelative;
    @BindView(R.id.find_q_icon)
    SimpleDraweeView findQIcon;
    @BindView(R.id.find_q_name)
    TextView findQName;
    @BindView(R.id.find_q_next)
    ImageView findQNext;
    @BindView(R.id.find_q_relative)
    RelativeLayout findQRelative;
    @BindView(R.id.find_not)
    TextView findNot;
    @BindView(R.id.find_search)
    ImageView findSearch;
    private int start = 1;//判断找人1还是找群2
    private FindUserByPhonePresenter findUserByPhonePresenter;
    private int userId;
    private String sessionId;
    private FindUser findUser;
    private FindGroup findGroup=new FindGroup();
    private FindGroupInfoPresenter findGroupInfoPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void initView() {
        findUserByPhonePresenter = new FindUserByPhonePresenter(new FindUserByPhone());
        findGroupInfoPresenter = new FindGroupInfoPresenter(new FindGroupInfo());
        findSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    String content = findSearchEdit.getText().toString();
                    findSearchEdit.setText(content);
                    if (TextUtils.isEmpty(content)) {
                        UIUtils.showToastSafe("不能为空");
                    } else {
                        if (start == 1) {
                            findUserByPhonePresenter.reqeust(userId, sessionId, content);

                        } else {
                            findGroupInfoPresenter.reqeust(userId, sessionId, Integer.parseInt(content));
                        }
                    }
                }
                return false;
            }
        });

    }

    @Override
    protected void destoryData() {
        findUserByPhonePresenter.unBind();
        findGroupInfoPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.add_friend_back, R.id.find_r, R.id.find_q,R.id.find_search,R.id.find_q_relative,R.id.find_r_relative})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_friend_back:
                finish();
                break;
            case R.id.find_r:
                start = 1;
                findROk.setVisibility(View.VISIBLE);
                findQOk.setVisibility(View.GONE);
                break;
            case R.id.find_q:
                start = 2;
                findQOk.setVisibility(View.VISIBLE);
                findROk.setVisibility(View.GONE);
                break;
            case R.id.find_search:
                /*String content = findSearchEdit.getText().toString();
                //findSearchEdit.setText(content);
                if (TextUtils.isEmpty(content)) {
                    UIUtils.showToastSafe("不能为空");
                } else {
                    if (start == 1) {
                        findUserByPhonePresenter.reqeust(userId, sessionId, content);

                    } else {

                    }
                }*/
                break;
            case R.id.find_q_relative:
                Intent intent_q = new Intent(AddFriendActivity.this,FindGroupDetailsActivity.class);
                intent_q.putExtra("findGroup",findGroup);
                startActivity(intent_q);
                break;
            case R.id.find_r_relative:
                Intent intent = new Intent(AddFriendActivity.this,FindUserDetailsActivity.class);
                intent.putExtra("findUser",findUser);
                startActivity(intent);

                break;
        }
    }


    class FindGroupInfo implements DataCall<Result<FindGroup>> {

        @Override
        public void success(Result<FindGroup> data) {
            if (data.getStatus().equals("0000")) {
                //Log.i("abc", "success: " + new Gson().toJson(data.getResult()));
                findGroup = data.getResult();
                findNot.setVisibility(View.VISIBLE);
                findRRelative.setVisibility(View.GONE);
                findQRelative.setVisibility(View.GONE);
                if (findGroup.equals(null)){

                }else {
                    findRRelative.setVisibility(View.GONE);
                    findQRelative.setVisibility(View.VISIBLE);
                    findQIcon.setImageURI(Uri.parse(findGroup.getGroupImage()));
                    findQName.setText(findGroup.getGroupName());
                    findNot.setVisibility(View.GONE);
                }
            }

        }

        @Override
        public void fail(ApiException e) {

        }
    }
        class FindUserByPhone implements DataCall<Result<FindUser>> {

            @Override
            public void success(Result<FindUser> data) {
                if (data.getStatus().equals("0000")) {
                    //Log.i("abc", "success: " + new Gson().toJson(data.getResult()));
                    findUser = data.getResult();
                    findQRelative.setVisibility(View.GONE);
                    findRRelative.setVisibility(View.VISIBLE);

                    findRIcon.setImageURI(Uri.parse(findUser.getHeadPic()));
                    findRName.setText(findUser.getNickName());
                    findNot.setVisibility(View.GONE);
                }
                if (data.getStatus().equals("1001")) {
                    findNot.setVisibility(View.VISIBLE);
                    findRRelative.setVisibility(View.GONE);
                    findQRelative.setVisibility(View.GONE);
                }

            }

            @Override
            public void fail(ApiException e) {

            }
        }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences share = WDApp.getShare();
        userId = share.getInt("userid", 0);
        sessionId = share.getString("sessionid", "");

    }
}
