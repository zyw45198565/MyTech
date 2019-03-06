package com.wd.tech.frag.fragone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.activity.AddFriendActivity;
import com.wd.tech.activity.ChatActivity;
import com.wd.tech.activity.CreateFriendActivity;
import com.wd.tech.activity.FindGroupsByUserIdActivity;
import com.wd.tech.activity.GroupChatActivity;
import com.wd.tech.activity.NewFriendsActivity;
import com.wd.tech.bean.FriendInfoList;
import com.wd.tech.bean.InitFriendlist;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.AllFriendsListPresenter;
import com.wd.tech.presenter.DeleteFriendRelationPresenter;
import com.wd.tech.presenter.TransferFriendGroupPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;
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
    @BindView(R.id.frag_contact_friend)
    LinearLayout fragContactFriend;
    @BindView(R.id.frag_contact_group)
    LinearLayout fragContactGroup;
    private List<InitFriendlist> groups;
    Unbinder unbinder;
    private AllFriendsListPresenter allFriendsListPresenter;
    private int userid;
    private String session1d;
    private PopupWindow window;
    private View inflate;
    private String groupName;
    private int black;
    private DeleteFriendRelationPresenter deleteFriendRelationPresenter;
    private TransferFriendGroupPresenter transferFriendGroupPresenter;
    private FriendInfoList friendInfoList;

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
        deleteFriendRelationPresenter = new DeleteFriendRelationPresenter(new DeleteFriend());
        transferFriendGroupPresenter = new TransferFriendGroupPresenter(new DeleteFriend());
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
       getShow();
        fragContactList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                //UIUtils.showToastSafe(groups.get(i).getFriendInfoList().get(i1).getFriendUid()+"");
                friendInfoList = groups.get(i).getFriendInfoList().get(i1);

                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,friendInfoList.getUserName());
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                intent.putExtra("userNames",friendInfoList.getUserName());
                startActivity(intent);
                return true;
            }
        });
        fragContactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View childView, int flatPos, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    long packedPos = ((ExpandableListView) parent).getExpandableListPosition(flatPos);
                    int groupPosition = ExpandableListView.getPackedPositionGroup(packedPos);
                    int childPosition = ExpandableListView.getPackedPositionChild(packedPos);
                    FriendInfoList friendInfoList = groups.get(groupPosition).getFriendInfoList().get(childPosition);
                    int friendUid = friendInfoList.getFriendUid();

                    window.showAsDropDown(childView.findViewById(R.id.item_pop_show),-80,15);
                    for (int i = 0; i < groups.size(); i++) {
                        if (groups.get(i).getGroupName().equals("黑名单")){
                           black=groups.get(i).getGroupId();
                        }
                    }

                    getWindow(inflate,friendUid);
                    return true;
                }

                return false;
            }

        });


}

    private void getShow() {
        inflate = View.inflate(getContext(), R.layout.popu_item_long_layout, null);
        window = new PopupWindow(inflate, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setTouchable(true);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());

    }

    private void getWindow(View inflate, final int id) {
        TextView popuItemLongDelete= inflate.findViewById(R.id.popu_item_long_delete);
        TextView popuItemLongBad= inflate.findViewById(R.id.popu_item_long_bad);
        popuItemLongDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFriendRelationPresenter.reqeust(userid,session1d,id);
                window.dismiss();
            }
        });
        popuItemLongBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (black==0){
                    window.dismiss();
                    UIUtils.showToastSafe("没有黑名单");
                    return;
                }
                transferFriendGroupPresenter.reqeust(userid,session1d,black,id);
                window.dismiss();
            }
        });
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
        deleteFriendRelationPresenter.unBind();
        transferFriendGroupPresenter.unBind();
    }

    @OnClick({R.id.frag_contact_friend, R.id.frag_contact_group, R.id.frag_contact_find})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.frag_contact_friend:
                Intent intent_f = new Intent(getContext(), NewFriendsActivity.class);
                startActivity(intent_f);
                break;
            case R.id.frag_contact_group:
                Intent intent_g = new Intent(getContext(), GroupChatActivity.class);
                startActivity(intent_g);
                break;
            case R.id.frag_contact_find:
                Intent intent = new Intent(getContext(), FindGroupsByUserIdActivity.class);
                startActivity(intent);
                break;
        }
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
            InitFriendlist initFriendlist = groups.get(groupPosition);
            groupName = initFriendlist.getGroupName();
            hodler.groupname.setText(groupName);

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
                holder.qianming = convertView.findViewById(R.id.tv_child_name);
                holder.tvChildSignature = convertView.findViewById(R.id.tv_child_signature);

                convertView.setTag(holder);
            } else {
                holder = (MyHolder) convertView.getTag();
            }
            FriendInfoList friendInfoList = groups.get(groupPosition).getFriendInfoList().get(childPosition);
            holder.headric.setImageURI(friendInfoList.getHeadPic());
            holder.qianming.setText(friendInfoList.getNickName());
            if (TextUtils.isEmpty(friendInfoList.getSignature())){
                holder.tvChildSignature.setText("您的好友还没有签名!");
            }else {
                holder.tvChildSignature.setText(friendInfoList.getSignature());
            }

            return convertView;
        }


        //父框件
        class GroupHodler {
            TextView groupname;
        }

        //子框件 (一个复选框 ,, 文字 ,, 价格 ,, 图片 ,, 还有自定义一个类)
        class MyHolder {
            SimpleDraweeView headric;
            TextView qianming,tvChildSignature;

        }
    }

    //初始化好友列表
    class InitListData implements DataCall<Result<List<InitFriendlist>>> {
        @Override
        public void success(Result<List<InitFriendlist>> result) {
            if (result.getStatus().equals("0000")) {
                groups = result.getResult();
                fragContactList.setAdapter(new MyExpandableListView());
                StringBuffer userHxIds=new StringBuffer();
                for (int i = 0; i < groups.size(); i++) {
                    for (int j = 0; j < groups.get(i).getFriendInfoList().size(); j++) {
                        userHxIds.append(groups.get(i).getFriendInfoList().get(j).getUserName()+",");
                    }
                }
                fragOneContactSmart.finishRefresh();
            }
        }

        @Override
        public void fail(ApiException e) {

        }


    }
    class DeleteFriend implements DataCall<Result> {

        @Override
        public void success(Result data) {
            UIUtils.showToastSafe(data.getMessage());
            if (data.getStatus().equals("0000")) {
                allFriendsListPresenter.reqeust(userid, session1d);
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
