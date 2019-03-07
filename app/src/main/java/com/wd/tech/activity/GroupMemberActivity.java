package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.GroupMemberAdapter;
import com.wd.tech.bean.GroupMember;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.FindGroupMemberListPresenter;
import com.wd.tech.presenter.ModifyPermissionPresenter;
import com.wd.tech.presenter.RemoveGroupMemberPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GroupMemberActivity extends BaseActivity {


    @BindView(R.id.group_member_back)
    ImageView groupMemberBack;
    @BindView(R.id.group_member_add)
    ImageView groupMemberAdd;
    @BindView(R.id.group_member_search_edit)
    EditText groupMemberSearchEdit;
    @BindView(R.id.group_member_swipe_one)
    SwipeMenuRecyclerView groupMemberSwipeOne;
    @BindView(R.id.group_member_swipe_two)
    SwipeMenuRecyclerView groupMemberSwipeTwo;
    @BindView(R.id.group_member_recycle)
    RecyclerView groupMemberRecycle;

    private int userId;
    private String sessionId;
    private List<GroupMember> result;
    private FindGroupMemberListPresenter findGroupMemberListPresenter;
    private RemoveGroupMemberPresenter removeGroupMemberPresenter;
    private int groupId;
    private List<GroupMember> groupTow;
    private List<GroupMember> groupOne;
    private GroupMemberAdapter groupMemberAdapterTwo;
    private GroupMemberAdapter groupMemberAdapterOne;
    private ModifyPermissionPresenter modifyPermissionPresenter;
    private int permission=1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_member;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupId", 0);
        SharedPreferences share = WDApp.getShare();
        userId = share.getInt("userid", 0);
        sessionId = share.getString("sessionid", "");
        findGroupMemberListPresenter = new FindGroupMemberListPresenter(new GroupMemberShow());
        findGroupMemberListPresenter.reqeust(userId,sessionId, groupId);
        removeGroupMemberPresenter = new RemoveGroupMemberPresenter(new GroupMemberManager());
        modifyPermissionPresenter = new ModifyPermissionPresenter(new GroupMemberManager());

    }



    private void getRecycle() {
        List<GroupMember> group=new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getRole()==3){
                group.add(result.get(i));
            }
        }
       GroupMemberAdapter groupMemberAdapter = new GroupMemberAdapter();

        groupMemberAdapter.setOnItemClickListener(new GroupMemberAdapter.ClickListener() {
            @Override
            public void click(int id) {

            }
        });
        groupMemberAdapter.setList(group);
        groupMemberRecycle.setLayoutManager(new LinearLayoutManager(this));
        groupMemberRecycle.setAdapter(groupMemberAdapter);
    }

    private void getOne() {
        groupOne = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getRole()==2){
                groupOne.add(result.get(i));
            }
        }
        groupMemberAdapterOne = new GroupMemberAdapter();

        groupMemberAdapterOne.setOnItemClickListener(new GroupMemberAdapter.ClickListener() {
            @Override
            public void click(int id) {

            }
        });
        groupMemberAdapterOne.setList(groupOne);
        groupMemberSwipeOne.setLayoutManager(new LinearLayoutManager(this));
        //groupMemberSwipeOne.addItemDecoration(new DefaultItemDecoration(Color.RED));

        //  设置侧滑
        // 设置监听器。
        groupMemberSwipeOne.setSwipeMenuCreator(mSwipeMenuCreator);
        groupMemberSwipeOne.setSwipeMenuItemClickListener(mMenuItemClickListener);

        groupMemberSwipeOne.setAdapter(groupMemberAdapterOne);
        //groupMemberAdapterOne.notifyDataSetChanged();
    }
    private void getTwo() {
        groupTow = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getRole()==1){
                groupTow.add(result.get(i));
            }
        }
        groupMemberAdapterTwo = new GroupMemberAdapter();

        groupMemberAdapterTwo.setOnItemClickListener(new GroupMemberAdapter.ClickListener() {
            @Override
            public void click(int id) {

            }
        });
        groupMemberAdapterTwo.setList(groupTow);
        groupMemberSwipeTwo.setLayoutManager(new LinearLayoutManager(this));
       // groupMemberSwipeTwo.addItemDecoration(new DefaultItemDecoration(Color.RED));

        //  设置侧滑
        // 设置监听器。
        groupMemberSwipeTwo.setSwipeMenuCreator(mSwipeMenuCreators);
        groupMemberSwipeTwo.setSwipeMenuItemClickListener(mMenuItemClickListeners);

        groupMemberSwipeTwo.setAdapter(groupMemberAdapterTwo);
        //groupMemberAdapterTwo.notifyDataSetChanged();
    }
    @Override
    protected void destoryData() {
        findGroupMemberListPresenter.unBind();
        removeGroupMemberPresenter.unBind();
        modifyPermissionPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.group_member_back, R.id.group_member_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.group_member_back:
                finish();
                break;
            case R.id.group_member_add:

                break;
        }
    }

    /**
     * 创建菜单
     */
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_46);
            int width1 = getResources().getDimensionPixelSize(R.dimen.dp_90);
            int width2 = getResources().getDimensionPixelSize(R.dimen.dp_60);

            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            SwipeMenuItem deleteItem = new SwipeMenuItem(GroupMemberActivity.this)
                    // .setBackground(R.drawable.selector_purple)
                    // .setImage(R.mipmap.ic_action_close)
                   // .setWidth(width)
                    .setHeight(height);
            // 各种文字和图标属性设置。
            rightMenu.addMenuItem(deleteItem); // 在Item右侧添加一个菜单。

            SwipeMenuItem completeItem = new SwipeMenuItem(GroupMemberActivity.this)
                    .setBackground(R.drawable.linear_one_shape)
                    .setText("撤销管理员")
                    .setTextColor(Color.WHITE)
                    .setWidth(width1)
                    .setHeight(height);
            rightMenu.addMenuItem(completeItem); // 添加菜单到右侧。

            SwipeMenuItem modifyItem = new SwipeMenuItem(GroupMemberActivity.this)
                    .setBackground(R.drawable.linear_one_shape)
                    .setText("移除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width2)
                    .setHeight(height);
            rightMenu.addMenuItem(modifyItem); // 添加菜单到右侧。


        }
    };
    /**
     * 菜单点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            //int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            switch (menuPosition) {
                case 1:
                    //  撤销管理员
                    if (direction==3){
                        modifyPermissionPresenter.reqeust(userId,sessionId,groupId,groupOne.get(position).getUserId(),1);
                        groupTow.add(groupOne.get(position));
                        groupOne.remove(position);
                        groupMemberAdapterOne.notifyDataSetChanged();
                        groupMemberAdapterTwo.notifyDataSetChanged();
                    }else {
                        UIUtils.showToastSafe("群主才有的权限");
                    }


                    break;
                    case 2:
                    //  移除
                        if (direction!=1){

                            removeGroupMemberPresenter.reqeust(userId,sessionId,groupId,groupOne.get(position).getUserId());
                            groupOne.remove(position);
                            groupMemberAdapterOne.notifyDataSetChanged();
                        }else {
                            UIUtils.showToastSafe("管理员与群主才有的权限");
                        }


                    break;
            }
        }

    };
    /**
     * 创建菜单
     */
    private SwipeMenuCreator mSwipeMenuCreators = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_46);
            int width1 = getResources().getDimensionPixelSize(R.dimen.dp_90);
            int width2 = getResources().getDimensionPixelSize(R.dimen.dp_60);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            SwipeMenuItem deleteItem = new SwipeMenuItem(GroupMemberActivity.this)
                    // .setBackground(R.drawable.selector_purple)
                    // .setImage(R.mipmap.ic_action_close)
                    // .setWidth(width)
                    .setHeight(height);
            // 各种文字和图标属性设置。
            rightMenu.addMenuItem(deleteItem); // 在Item右侧添加一个菜单。

            SwipeMenuItem completeItem = new SwipeMenuItem(GroupMemberActivity.this)
                    .setBackground(R.drawable.linear_one_shape)
                    .setText("设为管理员")
                    .setTextColor(Color.WHITE)
                    .setWidth(width1)
                    .setHeight(height);
            rightMenu.addMenuItem(completeItem); // 添加菜单到右侧。

            SwipeMenuItem modifyItem = new SwipeMenuItem(GroupMemberActivity.this)
                    .setBackground(R.drawable.linear_one_shape)
                    .setText("移除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width2)
                    .setHeight(height);
            rightMenu.addMenuItem(modifyItem); // 添加菜单到右侧。


        }
    };
    /**
     * 菜单点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListeners = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            //int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            switch (menuPosition) {

                    case 1:
                    //  设为管理员
                        if (permission==3){
                            modifyPermissionPresenter.reqeust(userId,sessionId,groupId,groupTow.get(position).getUserId(),2);
                            groupOne.add(groupTow.get(position));
                            groupMemberAdapterOne.notifyDataSetChanged();
                            groupTow.remove(position);
                            groupMemberAdapterTwo.notifyDataSetChanged();
                        }else {
                            UIUtils.showToastSafe("群主才有的权限");
                        }


                    break;
                    case 2:
                    //  移除
                        if (permission!=1){

                            removeGroupMemberPresenter.reqeust(userId,sessionId,groupId,groupTow.get(position).getUserId());
                            groupTow.remove(position);
                            groupMemberAdapterTwo.notifyDataSetChanged();
                        }else {
                            UIUtils.showToastSafe("管理员与群主才有的权限");
                        }


                    break;
            }
        }

    };
    class GroupMemberShow implements DataCall<Result<List<GroupMember>>>{

        @Override
        public void success(Result<List<GroupMember>> data) {
            if (data.getStatus().equals("0000")){
                 result = data.getResult();
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).getUserId()==userId){
                        permission=result.get(i).getRole();
                    }
                }
                getRecycle();
                getOne();
                getTwo();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
    class GroupMemberManager implements DataCall<Result>{

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")){

            }
            if (data.getStatus().equals("1001")){

            }
            UIUtils.showToastSafe(data.getMessage());
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
