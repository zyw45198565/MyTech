package com.wd.tech.activity;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.publish.ImageAdapter2;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.ReleasePostPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.StringUtils;
import com.wd.tech.utils.util.UIUtils;
import com.wd.tech.utils.util.WDActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublishMyInvitationActivity extends WDActivity {

    @BindView(R.id.publish_myinvitation_publish)
    TextView mPublishMyinvitationPublish;
    @BindView(R.id.publish_myinvitation_edit)
    EditText mPublishMyinvitationEdit;//输入的内容
    @BindView(R.id.publish_myinvitation_number)
    TextView mPublishMyinvitationNumber;//输入的字数
    @BindView(R.id.publish_myinvitation_picter)
    RecyclerView mPublishMyinvitationPicter;//要上传的图片
    @BindView(R.id.publish_myinvitation_picter_add)
    LinearLayout mPublishMyinvitationPicterAdd;//点击加号
    private PopupWindow mPop;
    private View mParent;
    private ImageAdapter2 mImageAdapter2;
    private ReleasePostPresenter releasePostPresenter;
    private SharedPreferences sp;
    private int userid;
    private String sessionid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_my_invitation;
    }

    @OnClick({R.id.publish_myinvitation_cancel,R.id.publish_myinvitation_publish,R.id.publish_myinvitation_picter_add})
    public void PublishClick(View view){
        switch (view.getId()){
            case R.id.publish_myinvitation_cancel:
                finish();
                break;
            case R.id.publish_myinvitation_publish://点击发表
                releasePostPresenter.reqeust(userid,sessionid,mPublishMyinvitationEdit.getText().toString(),mImageAdapter2.getList());//todo 调用发表帖子接口
                break;
            case R.id.publish_myinvitation_picter_add://点击加号弹出弹框
                View popShow = View.inflate(PublishMyInvitationActivity.this,R.layout.layout_popupwindow_btn,null);
                hideInput();//隐藏软键盘
                mPop = new PopupWindow(popShow, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                mPop.setFocusable(true);//获取焦点
                mPop.setTouchable(true);
                mPop.setOutsideTouchable(true);//点击外部窗口可消失
                mPop.setBackgroundDrawable(new BitmapDrawable());//设置背景 必须写

                LinearLayout camera = popShow.findViewById(R.id.publish_pop_camera);//找弹框组件
                LinearLayout picture = popShow.findViewById(R.id.publish_pop_picture);
                Button cancel = popShow.findViewById(R.id.publish_pop_cancel);

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击跳转摄像头
                        UIUtils.showToastSafe("相机");
                    }
                });
                picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //todo 点击相册上传头像
                        /*UIUtils.showToastSafe("相册");*/
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPop.dismiss();//点击弹框消失
                    }
                });
                mPop.showAtLocation(mParent, Gravity.BOTTOM,0,0);//父类底部显示
                break;
        }
    }

    @Override
    protected void initView() {
        mParent = View.inflate(PublishMyInvitationActivity.this, R.layout.activity_publish_my_invitation, null);

        sp = WDApp.getShare();
        userid = sp.getInt("userid", 0);
        sessionid = sp.getString("sessionid", "");

        //相册设置图片
        mImageAdapter2 = new ImageAdapter2();
        mImageAdapter2.setSign(1);
        mImageAdapter2.add(R.layout.layout_publish_add);//todo 添加+号布局
       /* mPublishMyinvitationPicter.setVisibility(View.VISIBLE);*/
        mPublishMyinvitationPicter.setLayoutManager(new GridLayoutManager(this,3));
        mPublishMyinvitationPicter.setAdapter(mImageAdapter2);

        releasePostPresenter = new ReleasePostPresenter(new myReleasePostCall());
    }

    @Override//相册回调
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
            String filePath = getFilePath(null,requestCode,data);
            if (!StringUtils.isEmpty(filePath)) {
                mImageAdapter2.add(filePath);
                mImageAdapter2.notifyDataSetChanged();
            }
        }

    }

    @Override
    protected void destoryData() {
        releasePostPresenter.unBind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private class myReleasePostCall implements DataCall<Result> {
        @Override
        public void success(Result data) {
            if(data.getStatus().equals("0000")){
                UIUtils.showToastSafe("发布帖子： " + data.getMessage());
            }
        }

        @Override
        public void fail(ApiException e) {
            UIUtils.showToastSafe("发布帖子：  "+e.getMessage());
        }
    }
}
