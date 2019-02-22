package com.wd.tech.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wd.tech.R;
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
    private PopupWindow pop;
    private View parent;

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

                break;
            case R.id.publish_myinvitation_picter_add://点击加号弹出弹框
                View popShow = View.inflate(PublishMyInvitationActivity.this,R.layout.layout_popupwindow_btn,null);
                pop = new PopupWindow(popShow, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                pop.setFocusable(true);//获取焦点
                pop.setTouchable(true);
                pop.setOutsideTouchable(true);//点击外部窗口可消失
                pop.setBackgroundDrawable(new BitmapDrawable());//设置背景 必须写

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
                        //点击跳转摄像头
                        UIUtils.showToastSafe("相册");
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop.dismiss();//点击弹框消失
                    }
                });
                pop.showAtLocation(parent, Gravity.BOTTOM,0,0);//父类底部显示
                break;
        }
    }

    @Override
    protected void initView() {
        parent = View.inflate(PublishMyInvitationActivity.this, R.layout.activity_publish_my_invitation, null);
    }

    @Override
    protected void destoryData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
