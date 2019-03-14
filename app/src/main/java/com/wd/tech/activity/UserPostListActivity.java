package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.UserPostAdatper;
import com.wd.tech.bean.FindUser;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.UserPost;
import com.wd.tech.picter.PhotoViewActivity;
import com.wd.tech.presenter.AddCommunityGreatPresenter;
import com.wd.tech.presenter.AddFollowPresenter;
import com.wd.tech.presenter.CancelCommunityGreatPresenter;
import com.wd.tech.presenter.CancelFollowPresenter;
import com.wd.tech.presenter.FindUserPostPresenter;
import com.wd.tech.presenter.QueryFriendInformationPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;
import com.wd.tech.utils.util.WDActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserPostListActivity extends WDActivity {

    @BindView(R.id.user_post_background)
    SimpleDraweeView userPostBackground;
    @BindView(R.id.user_post_head)
    SimpleDraweeView userPostHead;
    @BindView(R.id.user_post_nick)
    TextView userPostNick;
    @BindView(R.id.user_post_signature)
    TextView userPostSignature;
    @BindView(R.id.user_post_more)
    ImageView userPostMore;
    @BindView(R.id.user_post_xlv)
    RecyclerView mUserPostXlv;
    @BindView(R.id.refreshLayout_x)
    SmartRefreshLayout refreshLayoutX;
    @BindView(R.id.add_friend)
    TextView addFriend;
    @BindView(R.id.add_attention)
    TextView addAttention;
    @BindView(R.id.layout_hide)
    LinearLayout layoutHide;
    private SharedPreferences sp;
    private int userid;
    private String sessionid;
    private UserPostAdatper mUserPostAdatper;
    private AddCommunityGreatPresenter mAddUserGreatPresenter;
    private FindUserPostPresenter mFindUserPostPresenter;
    private CancelCommunityGreatPresenter mCancelCommunityGreatPresenter;
    private int userId;
    private int mmid;
    private List<UserPost.CommunityUserPostVoListBean> communityUserPostVoList;
    private AddFollowPresenter mAddFollowPresenter;
    private UserPost userPost1;
    private CancelFollowPresenter mCancelFollowPresenter;
    int flag1 = 2;//默认为不关注
    int flag2 = 2;
    private QueryFriendInformationPresenter mQueryFriendInformationPresenter;
    private Intent intent;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_post_list;
    }

    @OnClick({R.id.user_post_more,R.id.add_attention,R.id.add_friend})
    public void setClick(View view) {
        switch (view.getId()) {
            case R.id.user_post_more://点击+关注 +好友
                layoutHide.setVisibility(View.VISIBLE);
                userPostMore.setVisibility(View.GONE);
                break;

            case R.id.add_friend://加好友
                mQueryFriendInformationPresenter = new QueryFriendInformationPresenter(new mQueryFriendInformationCall());//查询好友信息
                mQueryFriendInformationPresenter.reqeust(userid,sessionid,userId);//查询好友信息
                break;

            case R.id.add_attention://加关注
                if(flag2==1){
                    mCancelFollowPresenter.reqeust(userid,sessionid,userId);
                }else{
                    mAddFollowPresenter.reqeust(userid,sessionid,userId);
                }
                break;
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//创建布局管理器
        mUserPostXlv.setLayoutManager(linearLayoutManager);

        sp = WDApp.getShare();//获取userId sessionId
        userid = sp.getInt("userid", 0);
        sessionid = sp.getString("sessionid", "");

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);//获取用户id/发布人id
        final String head = intent.getStringExtra("head");
        final ArrayList<String> mList = new ArrayList<>();
        mList.add(head);
        String nick = intent.getStringExtra("nick");
        String text = intent.getStringExtra("text");
        myrefreshLayout();
        userPostBackground.setImageURI(head);//背景设置
        userPostHead.setImageURI(head);//头像
        userPostHead.setOnClickListener(new View.OnClickListener() {//点击图片放大
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPostListActivity.this, PhotoViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("imageArray", mList);
                intent.putExtras(bundle);
                intent.putExtra("position", 0);//下标
                v.getContext().startActivity(intent);
            }
        });

        userPostNick.setText(nick);//昵称
        userPostSignature.setText(text);//签名


        mFindUserPostPresenter = new FindUserPostPresenter(new userPostCall());//用户发布帖子列表展示
        mAddUserGreatPresenter = new AddCommunityGreatPresenter(new userGreatCall());//点赞
        mCancelCommunityGreatPresenter = new CancelCommunityGreatPresenter(new mCancelCommunityGreatCall());//取消点赞
        mAddFollowPresenter = new AddFollowPresenter(new mAddFollowCall());//关注用户
        mCancelFollowPresenter = new CancelFollowPresenter(new mCancelFollowCall());//取消关注

        mUserPostAdatper = new UserPostAdatper(this);

        mUserPostAdatper.setClickLike(new UserPostAdatper.ClickLike() {//点赞
            @Override
            public void clickLikeChange(int possion) {
                mAddUserGreatPresenter.reqeust(userid, sessionid,possion);
            }

            @Override
            public void clickLikeChange2(int possion) {//取消点赞
                mCancelCommunityGreatPresenter.reqeust(userid, sessionid, possion);
            }

        });
        mUserPostAdatper.setClickComment(new UserPostAdatper.ClickComment() {
            @Override
            public void clickStart(int id, int number) {
                Intent intent1 = new Intent(UserPostListActivity.this, CommentListShowActivity.class);
                intent1.putExtra("id", id);
                intent1.putExtra("num", number);
                startActivity(intent1);
            }
        });

        mUserPostXlv.setAdapter(mUserPostAdatper);//设置适配器

        mFindUserPostPresenter.reqeust(userid, sessionid, userId, false, 99);
    }

    @Override
    protected void destoryData() {
        mAddUserGreatPresenter.unBind();
        mFindUserPostPresenter.unBind();
        mCancelCommunityGreatPresenter.unBind();
        mAddFollowPresenter.unBind();
        mCancelFollowPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    private void myrefreshLayout() {//刷新加载更多定义
        //刷新
        refreshLayoutX.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh();
                mUserPostAdatper.remove();
                mUserPostAdatper.notifyDataSetChanged();
                mFindUserPostPresenter.reqeust(userid, sessionid, userId, false, 10);
            }
        });
        //加载更多
        refreshLayoutX.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                mFindUserPostPresenter.reqeust(userid, sessionid, userId, true, 10);
            }
        });

//        开始下拉
        refreshLayoutX.setEnableRefresh(true);//启用刷新
        refreshLayoutX.setEnableLoadmore(true);//启用加载
//        关闭下拉
        refreshLayoutX.finishRefresh();
        refreshLayoutX.finishLoadmore();
    }

    //点赞成功
    private class userGreatCall implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(UserPostListActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
            mUserPostAdatper.notifyItemChanged(mmid);//当前条目刷新
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("点赞 ：  " + e.getMessage());
        }
    }

    private class userPostCall implements DataCall<Result<List<UserPost>>> {

        @Override
        public void success(Result<List<UserPost>> data) {
            if (data.getStatus().equals("0000")) {
                List<UserPost> result = data.getResult();
                userPost1 = result.get(0);
                communityUserPostVoList = userPost1.getCommunityUserPostVoList();
                flag1 = userPost1.getCommunityUserVo().getWhetherMyFriend();//+好友状态
                flag2 = userPost1.getCommunityUserVo().getWhetherFollow();//+关注状态
                if(userPost1.getCommunityUserVo().getWhetherFollow()==1){
                    addAttention.setText("取消关注");
                }else{
                    addAttention.setText("+关注");
                }
                if(userPost1.getCommunityUserVo().getWhetherMyFriend()==1){
                    addFriend.setText("好友");
                }else{
                    addFriend.setText("+好友");
                }
                mUserPostAdatper.addList(communityUserPostVoList);
                mUserPostAdatper.notifyDataSetChanged();
            }
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("用户发布帖子的列表展示：  " + e.getMessage());
        }
    }

    private class mCancelCommunityGreatCall implements DataCall<Result> {//取消点赞
        @Override
        public void success(Result data) {
            Toast.makeText(UserPostListActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
            mUserPostAdatper.notifyItemChanged(mmid);

        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("取消点赞：  " + e.getMessage());
        }
    }

    private class mAddFollowCall implements DataCall<Result> {//关注用户
        @Override
        public void success(Result data) {
            if(data.getStatus().equals("0000")){
                addAttention.setText("取消关注");
                flag2=1;
                UIUtils.showToastSafe("关注用户  ：  " + data.getMessage());
            }
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("关注用户  ：  " + e.getMessage());
        }
    }

    private class mCancelFollowCall implements DataCall<Result> {//取消关注
        @Override
        public void success(Result data) {
            if(data.getStatus().equals("0000")){
                addAttention.setText("+关注");
                flag2=2;
                UIUtils.showToastSafe("取消关注  ：  " + data.getMessage());
            }
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("取消关注用户  ：  " + e.getMessage());
        }
    }

    private class mQueryFriendInformationCall implements DataCall<Result<FindUser>>{//查询好友信息
        @Override
        public void success(Result<FindUser> data) {
            if(data.getStatus().equals("0000")){

                FindUser result = data.getResult();
                intent = new Intent(UserPostListActivity.this, FindUserDetailsActivity.class);
                intent.putExtra("findUser",result);
                startActivity(intent);
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
