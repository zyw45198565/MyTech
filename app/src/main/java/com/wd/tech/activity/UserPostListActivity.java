package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.wd.tech.bean.Result;
import com.wd.tech.bean.UserPost;
import com.wd.tech.frag.Frag_03;
import com.wd.tech.presenter.AddCommunityGreatPresenter;
import com.wd.tech.presenter.CancelCommunityGreatPresenter;
import com.wd.tech.presenter.FindUserPostPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;
import com.wd.tech.utils.util.WDActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.layout)
    LinearLayout layout;
    private SharedPreferences sp;
    private int userid;
    private String sessionid;
    private UserPostAdatper mUserPostAdatper;
    private AddCommunityGreatPresenter mAddUserGreatPresenter;
    private FindUserPostPresenter mFindUserPostPresenter;
    private CancelCommunityGreatPresenter mCancelCommunityGreatPresenter;
    private int userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_post_list;
    }

    @Override
    protected void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//创建布局管理器
        mUserPostXlv.setLayoutManager(linearLayoutManager);

        sp = WDApp.getShare();//获取userId sessionId
        userid = sp.getInt("userid", 0);
        sessionid = sp.getString("sessionid", "");

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);//获取用户id/发布人id
        String head = intent.getStringExtra("head");
        String nick = intent.getStringExtra("nick");
        String text = intent.getStringExtra("text");
        myrefreshLayout();
        userPostBackground.setImageURI(head);//背景设置
        userPostHead.setImageURI(head);//头像
        userPostNick.setText(nick);//昵称
        userPostSignature.setText(text);//签名


        mFindUserPostPresenter = new FindUserPostPresenter(new userPostCall());//用户发布帖子列表展示

        mUserPostAdatper = new UserPostAdatper(this);

        mUserPostAdatper.setClickLike(new UserPostAdatper.ClickLike() {
                                          @Override
                                          public void clickLikeChange(int id) {
                                              mAddUserGreatPresenter = new AddCommunityGreatPresenter(new userGreatCall());//点赞
                                              mAddUserGreatPresenter.reqeust(userid, sessionid, id);
                                          }

                                          @Override
                                          public void clickLikefail(int id) {
                                              mCancelCommunityGreatPresenter = new CancelCommunityGreatPresenter(new mCancelCommunityGreatCall());//取消点赞
                                              mCancelCommunityGreatPresenter.reqeust(userid, sessionid, id);
                                          }
                                      }
        );

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
//            mUserPostAdatper.remove();
//            mUserPostAdatper.notifyDataSetChanged();

            mFindUserPostPresenter.reqeust(userid, sessionid, userId, false, 10);//列表展示

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
                UserPost userPost1 = result.get(0);
                List<UserPost.CommunityUserPostVoListBean> communityUserPostVoList = userPost1.getCommunityUserPostVoList();
                mUserPostAdatper.addList(communityUserPostVoList);
                mUserPostAdatper.notifyDataSetChanged();
            }
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("用户发布帖子的列表展示：  " + e.getMessage());
        }
    }

    //取消点赞
    private class mCancelCommunityGreatCall implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(UserPostListActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
//            mUserPostAdatper.remove();
//            mUserPostAdatper.notifyDataSetChanged();

            mFindUserPostPresenter.reqeust(userid, sessionid, userId, false, 10);//列表展示

        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("取消点赞：  " + e.getMessage());
        }
    }
}
