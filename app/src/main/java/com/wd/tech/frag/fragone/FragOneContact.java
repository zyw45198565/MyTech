package com.wd.tech.frag.fragone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.activity.FindGroupsByUserIdActivity;
import com.wd.tech.bean.FriendInfoList;
import com.wd.tech.bean.InitFriendlist;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.AllFriendsListPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.WDFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * date:2019/2/19 16:47
 * author:陈国星(陈国星)
 * function:
 */
public class FragOneContact extends WDFragment {
    @BindView(R.id.frag_contact_search_edit)
    EditText fragContactSearchEdit;
    @BindView(R.id.frag_contact_list)
    ExpandableListView fragContactList;
    @BindView(R.id.frag_one_contact_smart)
    SmartRefreshLayout fragOneContactSmart;
    @BindView(R.id.frag_contact_find)
    LinearLayout fragContactFind;
    private List<InitFriendlist> groups;
    Unbinder unbinder;
    private AllFriendsListPresenter allFriendsListPresenter;
    private int userid;
    private String session1d;

    @Override
    public String getPageName() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_one_contact;
    }

    @Override
    protected void initView() {

        allFriendsListPresenter = new AllFriendsListPresenter(new InitListData());
        //开始下拉
        fragOneContactSmart.setEnableRefresh(true);//启用刷新
        fragOneContactSmart.setEnableLoadmore(false);//启用加载
        //刷新
        fragOneContactSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                allFriendsListPresenter.reqeust(userid, session1d);
            }
        });
       /* //加载更多
        fragOneContactSmart.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                allFriendsListPresenter.reqeust(userid, session1d);
                //refreshlayout.finishLoadmore();
            }
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        allFriendsListPresenter.unBind();
    }

    @OnClick(R.id.frag_contact_find)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), FindGroupsByUserIdActivity.class);
        startActivity(intent);
    }

    class MyExpandableListView extends BaseExpandableListAdapter {


        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groups.get(groupPosition).getFriendInfoList().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return groups.get(groupPosition).getFriendInfoList().get(childPosition);
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
            return false;
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
            InitFriendlist initFriendlist = groups.get(groupPosition);

            hodler.groupname.setText(initFriendlist.getGroupName());

            return convertView;
        }

        //子布局
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            MyHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.expandablelistview_two_item, null);
                holder = new MyHolder();
                holder.headric = convertView.findViewById(R.id.sdv_child);
                holder.qianming = convertView.findViewById(R.id.tv_child);

                convertView.setTag(holder);
            } else {
                holder = (MyHolder) convertView.getTag();
            }
            FriendInfoList friendInfoList = groups.get(groupPosition).getFriendInfoList().get(childPosition);
            holder.headric.setImageURI(friendInfoList.getHeadPic());
            holder.qianming.setText(friendInfoList.getSignature());//单价
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

        }
    }

    //初始化好友列表
    class InitListData implements DataCall<Result<List<InitFriendlist>>> {
        @Override
        public void success(Result<List<InitFriendlist>> result) {
            if (result.getStatus().equals("0000")) {
                groups = result.getResult();
                fragContactList.setAdapter(new MyExpandableListView());
                fragOneContactSmart.finishRefresh();
            }
        }

        @Override
        public void fail(ApiException e) {

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences share = WDApp.getShare();
        userid = share.getInt("userid", 0);
        session1d = share.getString("sessionid", "");
        allFriendsListPresenter.reqeust(userid, session1d);
    }
}
