package com.wd.tech.frag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.activity.CommentListShowActivity;
import com.wd.tech.activity.PublishMyInvitationActivity;
import com.wd.tech.activity.UserPostListActivity;
import com.wd.tech.adapter.CommunityListAdapter;
import com.wd.tech.bean.FindCommunityList;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.AddCommunityCommentPresenter;
import com.wd.tech.presenter.AddCommunityGreatPresenter;
import com.wd.tech.presenter.CancelCommunityGreatPresenter;
import com.wd.tech.presenter.FindCommunityListPresenter;
import com.wd.tech.presenter.TheTaskPresenter;
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
 * @author Tech
 * @date 2019/2/18 20:28
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class Frag_03 extends WDFragment {

    @BindView(R.id.frag03xlv)
    RecyclerView mFrag03xlv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private FindCommunityListPresenter mFindCommunityListPresenter;
    private CommunityListAdapter mCommunityListAdapter;
    private SharedPreferences sp;
    private int userid;
    private String sessionid;
    private AddCommunityGreatPresenter addCommunityGreatPresenter;
    private AddCommunityCommentPresenter addCommunityCommentPresenter;
    private int ids;
    private View parent;
    private TextView frag03WriteCommentSend;
    private EditText frag03WriteCommentEdit;
    private PopupWindow mPop;
    private CancelCommunityGreatPresenter cancelCommunityGreatPresenter;
    private boolean zai;
    private TheTaskPresenter theTaskPresenter;

    @Override
    public String getPageName() {
        return "Frag_社区";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_03;
    }

    @Override
    protected void initView() {
        parent = View.inflate(getContext(),R.layout.frag_03,null);

        sp = WDApp.getShare();//获取userId sessionId
        userid = sp.getInt("userid", 0);
        sessionid = sp.getString("sessionid", "");
        zai = sp.getBoolean("zai", false);
        myrefreshLayout();

        mFindCommunityListPresenter = new FindCommunityListPresenter(new communityListCall());//社区列表
        addCommunityGreatPresenter = new AddCommunityGreatPresenter(new communityGreatCall());//点赞
        addCommunityCommentPresenter = new AddCommunityCommentPresenter(new communityCommentCall());//评论
        cancelCommunityGreatPresenter = new CancelCommunityGreatPresenter(new cancelCommunityGreatCall());//取消点赞
        //做任务
        theTaskPresenter = new TheTaskPresenter(new TheTaskCall());


        mCommunityListAdapter = new CommunityListAdapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());//创建布局管理器
        mFrag03xlv.setLayoutManager(linearLayoutManager);
        mFrag03xlv.setAdapter(mCommunityListAdapter);//设置适配器

        mCommunityListAdapter.setClickOkListener(new CommunityListAdapter.ClickOkListener() {//点赞/取消点赞
            @Override
            public void clickOk(int id, int greatStyle) {
                if(!zai){
                    UIUtils.showToastSafe("请先登录哦~");
                    return;
                }
                if(greatStyle==2){
                    addCommunityGreatPresenter.reqeust(userid, sessionid, id);
                }else{
                    cancelCommunityGreatPresenter.reqeust(userid,sessionid,id);
                }
            }
        });

        mCommunityListAdapter.setClickHeadLinstener(new CommunityListAdapter.ClickHeadLinstener() {
            @Override
            public void clickHead(int userId, String head, String nick, String text) {
                if(!zai){
                    UIUtils.showToastSafe("请先登录哦~");
                    return;
                }
                Intent intent = new Intent(getContext(), UserPostListActivity.class);
                intent.putExtra("userId",userId);
                intent.putExtra("head",head);
                intent.putExtra("nick",nick);
                intent.putExtra("text",text);
                startActivity(intent);
            }
        });
        mCommunityListAdapter.setMyTalkBack(new CommunityListAdapter.TalkBack() {//评论自定义回调接口
            @Override
            public void talkBacks(int id) {
                if(!zai){
                    UIUtils.showToastSafe("请先登录哦~");
                    return;
                }
                ids = id;
                View contentView = View.inflate(getContext(),R.layout.popupwindow_edit,null);
                mPop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                mPop.setFocusable(true);
                mPop.setTouchable(true);
                mPop.setOutsideTouchable(true);
                mPop.setBackgroundDrawable(new BitmapDrawable());
                mPop.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
                mPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                frag03WriteCommentEdit = contentView.findViewById(R.id.frag03_write_comment_edit);
                frag03WriteCommentSend = contentView.findViewById(R.id.frag03_write_comment_send);

                frag03WriteCommentSend.setOnClickListener(new View.OnClickListener() {//评论的发送
                    @Override
                    public void onClick(View v) {
                        String s = frag03WriteCommentEdit.getText().toString();
                        if(TextUtils.isEmpty(s)){
                            UIUtils.showToastSafe("评论不能为空");
                            return;
                        }
                        addCommunityCommentPresenter.reqeust(userid,sessionid,ids,s);
                        frag03WriteCommentEdit.setText("");
                        mPop.dismiss();
                    }
                });

                mPop.showAtLocation(parent, Gravity.BOTTOM,0,0);
            }
        });

        mCommunityListAdapter.setClickTextStart(new CommunityListAdapter.ClickTextStart() {
            @Override
            public void clickShow(int id, int num, String headPic, String nickName) {
                if(!zai){
                    UIUtils.showToastSafe("请先登录哦~");
                    return;
                }
                Intent intent = new Intent(getContext(), CommentListShowActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("num",num);
                intent.putExtra("headPic",headPic);
                intent.putExtra("nickName",nickName);
                startActivity(intent);
            }
        });
        mFindCommunityListPresenter.reqeust(userid, sessionid, false, 5);
    }

    @OnClick(R.id.frag03_write_fresco)
    public void setFrag03Click(View view) {
        switch (view.getId()) {
            case R.id.frag03_write_fresco://点击跳转到发布贴子页面
                if(!zai){
                    UIUtils.showToastSafe("请先登录哦~");
                    return;
                }
                startActivity(new Intent(getContext(), PublishMyInvitationActivity.class));
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sp = WDApp.getShare();//获取userId sessionId
        userid = sp.getInt("userid", 0);
        sessionid = sp.getString("sessionid", "");
        zai = sp.getBoolean("zai", false);

        mCommunityListAdapter.remove();
        mFindCommunityListPresenter.reqeust(userid, sessionid, false, 5);//重新请求列表
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mFindCommunityListPresenter.unBind();
        addCommunityGreatPresenter.unBind();
        addCommunityCommentPresenter.unBind();
        cancelCommunityGreatPresenter.unBind();
    }

    private void myrefreshLayout() {//刷新加载更多定义
        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh();
                mCommunityListAdapter.remove();
                mCommunityListAdapter.notifyDataSetChanged();
                mFindCommunityListPresenter.reqeust(userid, sessionid, false, 5);
            }
        });
        //加载更多
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                mFindCommunityListPresenter.reqeust(userid, sessionid, true, 5);
            }
        });

//        开始下拉
        refreshLayout.setEnableRefresh(true);//启用刷新
        refreshLayout.setEnableLoadmore(true);//启用加载
//        关闭下拉
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();
    }

    private class communityListCall implements DataCall<Result<List<FindCommunityList>>> {//社区列表展示

        @Override
        public void success(Result<List<FindCommunityList>> data) {
            if (data.getStatus().equals("0000")) {
                List<FindCommunityList> result = data.getResult();
                for (int i = 0; i < result.size(); i++) {
                    FindCommunityList findCommunityList = result.get(i);
                    mCommunityListAdapter.addList(findCommunityList);//添加数据到适配器中
                    mCommunityListAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("社区列表展示：  " + e.getMessage());
        }
    }

    private class communityGreatCall implements DataCall<Result> {//点赞

        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                mCommunityListAdapter.remove();
                mFindCommunityListPresenter.reqeust(userid, sessionid, false, 5);
            }
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("社区点赞：  " + e.getMessage());
        }
    }

    private class communityCommentCall implements DataCall<Result> {//评论
        @Override
        public void success(Result data) {
            if(data.getStatus().equals("0000")){
                UIUtils.showToastSafe("社区评论：   "+data.getMessage());
                mCommunityListAdapter.remove();
                mFindCommunityListPresenter.reqeust(userid, sessionid, false, 5);//重新请求列表
                theTaskPresenter.reqeust(userid,sessionid,1002);
            }
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("社区评论：   "+e.getMessage());
        }
    }

    private class cancelCommunityGreatCall implements DataCall<Result> {
        @Override
        public void success(Result data) {
            if (data.getStatus().equals("0000")) {
                mCommunityListAdapter.remove();
                mFindCommunityListPresenter.reqeust(userid, sessionid, false, 5);
            }
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("取消点赞：  " + e.getMessage());
        }//取消点赞
    }

    private class TheTaskCall implements DataCall<Result> {
        @Override
        public void success(Result data) {

        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
