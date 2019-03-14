package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.FriendInfoList;
import com.wd.tech.bean.InitFriendlist;
import com.wd.tech.bean.LoginBean;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.AllFriendsListPresenter;
import com.wd.tech.presenter.BatchInviteAddGroupPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteFriendsActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.text_yaoqing)
    TextView textYaoqing;
    @BindView(R.id.tou)
    RelativeLayout tou;
    @BindView(R.id.erji_liebiao)
    ExpandableListView erjiLiebiao;
    private AllFriendsListPresenter allFriendsListPresenter;
    //Model：定义的数据
    private List<InitFriendlist> groupS;
    private String sessionId;
    private int userId;
    private String xuan = "";
    private List<Integer> sum;
    private BatchInviteAddGroupPresenter presenter;
    private int groupId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite_friends;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        allFriendsListPresenter = new AllFriendsListPresenter(new InitFr());
        presenter = new BatchInviteAddGroupPresenter(new AA());
        Intent intent = getIntent();

        groupId = intent.getIntExtra("groupId", 0);
        SharedPreferences share = WDApp.getShare();
        userId = share.getInt("userid", 0);
        sessionId = share.getString("sessionid", "");
        allFriendsListPresenter.reqeust(userId, sessionId);
    }



    @Override
    protected void destoryData() {
        allFriendsListPresenter.unBind();
        presenter.unBind();
    }

    @OnClick({R.id.back, R.id.text_yaoqing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.text_yaoqing:
                int size = sum.size();
                Object[] array = new Object[0];
                for(int i=0;i<size;i++){
                    array =sum.toArray();

                }

                presenter.reqeust(userId,sessionId,groupId,array);

                break;
        }
    }

    class MyExpandableListView extends BaseExpandableListAdapter {


        @Override
        public int getGroupCount() {
            return groupS.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groupS.get(groupPosition).getFriendInfoList().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupS.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return groupS.get(groupPosition).getFriendInfoList().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        //父布局
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHodler hodler;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.expandablelistview_one_item, null);
                hodler = new GroupHodler();
                hodler.groupname = convertView.findViewById(R.id.tv_group);
                convertView.setTag(hodler);
            } else {
                hodler = (GroupHodler) convertView.getTag();
            }
            InitFriendlist initFriendlist = groupS.get(groupPosition);

            hodler.groupname.setText(initFriendlist.getGroupName());

            return convertView;
        }


        //子布局
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            MyHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.expandablelistview_two_item_layout, null);
                holder = new MyHolder();
                holder.headric = convertView.findViewById(R.id.iv_child);
                holder.qianming = convertView.findViewById(R.id.tv_child);
                holder.name = convertView.findViewById(R.id.tv_qian);
                holder.fuxuankuang = convertView.findViewById(R.id.fuxuankuang);
                convertView.setTag(holder);
            } else {
                holder = (MyHolder) convertView.getTag();
            }
            sum = new ArrayList<>();
            final FriendInfoList friendInfoList = groupS.get(groupPosition).getFriendInfoList().get(childPosition);
            holder.headric.setImageURI(friendInfoList.getHeadPic());
            holder.qianming.setText(friendInfoList.getNickName());
            holder.name.setText(friendInfoList.getSignature());
            holder.fuxuankuang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sum.add(friendInfoList.getFriendUid());
                    }else {
                        sum.clear();
                    }
                }
            });
            return convertView;
        }


        //父框件
        class GroupHodler {
            TextView groupname;
        }

        //子框件 (一个复选框 ,, 文字 ,, 价格 ,, 图片 ,, 还有自定义一个类)
        class MyHolder {
            SimpleDraweeView headric;
            TextView qianming;
            TextView name;
            CheckBox fuxuankuang;
        }

    }

    private class InitFr implements DataCall<Result<List<InitFriendlist>>> {
        @Override
        public void success(Result<List<InitFriendlist>> data) {
            if (data.getStatus().equals("0000")) {
                groupS = data.getResult();
                erjiLiebiao.setAdapter(new MyExpandableListView());

            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }


    private class AA implements DataCall<Result> {
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){

            }
            UIUtils.showToastSafe(data.getMessage());
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
