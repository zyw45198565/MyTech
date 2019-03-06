package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.CommentShowItemAdapter;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.UserComment;
import com.wd.tech.presenter.FindCommunityUserCommentListPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;
import com.wd.tech.utils.util.WDActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentListShowActivity extends WDActivity implements XRecyclerView.LoadingListener {

    @BindView(R.id.comment_head)
    SimpleDraweeView commentHead;
    @BindView(R.id.comment_nick)
    TextView commentNick;
    @BindView(R.id.comment_item_num)
    TextView commentItemNum;
    @BindView(R.id.comment_item_xlv)
    XRecyclerView commentItemXlv;
    private FindCommunityUserCommentListPresenter mFindCommunityUserCommentListPresenter;
    private CommentShowItemAdapter mCommentItemAdapter;
    private SharedPreferences sp;
    private int userid;
    private String sessionid;
    private int parseInt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment_list_show;
    }

    @Override
    protected void initView() {
        sp = WDApp.getShare();//获取userId sessionId
        userid = sp.getInt("userid", 0);
        sessionid = sp.getString("sessionid", "");

        Intent intent = getIntent();
        parseInt = intent.getIntExtra("id", 0);//帖子id
        int num = intent.getIntExtra("num", 0);//评论数量
        String headPic = intent.getStringExtra("headPic");//头像
        String nickName = intent.getStringExtra("nickName");//昵称

        mFindCommunityUserCommentListPresenter = new FindCommunityUserCommentListPresenter(new userCommentLisrCall());

        commentHead.setImageURI(headPic);//设置头像
        commentNick.setText(nickName);//昵称
        commentItemNum.setText(num + "条评论");

        mCommentItemAdapter = new CommentShowItemAdapter();
        commentItemXlv.setLoadingListener(this);
        commentItemXlv.setLoadingMoreEnabled(true);
        commentItemXlv.setPullRefreshEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentItemXlv.setLayoutManager(linearLayoutManager);
        commentItemXlv.setAdapter(mCommentItemAdapter);

        mFindCommunityUserCommentListPresenter.reqeust(userid,sessionid, parseInt,false,10);
        //todo 缺少在评论列表里评论
    }

    @OnClick(R.id.comment_back)
    public void clickListener(View view) {
        switch (view.getId()) {
            case R.id.comment_back:
                finish();
                break;
        }
    }

    @Override
    protected void destoryData() {
        mFindCommunityUserCommentListPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void onRefresh() {
        commentItemXlv.refreshComplete();
        mCommentItemAdapter.remove();
        mCommentItemAdapter.notifyDataSetChanged();
        mFindCommunityUserCommentListPresenter.reqeust(userid,sessionid,parseInt,false,10);
    }

    @Override
    public void onLoadMore() {
        commentItemXlv.loadMoreComplete();
        mFindCommunityUserCommentListPresenter.reqeust(userid,sessionid,parseInt,true,10);
    }

    private class userCommentLisrCall implements DataCall<Result<List<UserComment>>> {
        @Override
        public void success(Result<List<UserComment>> data) {
            if(data.getStatus().equals("0000")){
                List<UserComment> userComments = data.getResult();
                for (int i = 0; i <userComments.size() ; i++) {
                    UserComment userComment = userComments.get(i);
                    mCommentItemAdapter.addList(userComment);
                    mCommentItemAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("评论列表：  " + e.getMessage());
        }
    }
}
