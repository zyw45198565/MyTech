package com.wd.tech.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.UserInfoBean;
import com.wd.tech.presenter.ModifyEmailPresenter;
import com.wd.tech.presenter.PerfectUserInfoPresenter;
import com.wd.tech.presenter.TheTaskPresenter;
import com.wd.tech.presenter.UserByUserIdPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.UIUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.wd.tech.activity.MySettingActivity.isEmail;

public class MyWanshanActivity extends BaseActivity {


    private int userid;
    private String sessionid;
    private TextView name;
    private TextView sex;
    private TextView date1;
    private TextView emaile;
    String qian111 = "";
    String emaile111 = "";
    private String name111;
    private TheTaskPresenter theTaskPresenter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        if(resultCode==1){
            qian111=data.getStringExtra("ming1");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_wanshan;
    }

    @Override
    protected void initView() {


        userid = WDApp.getShare().getInt("userid", 1);
        sessionid = WDApp.getShare().getString("sessionid", "");

        theTaskPresenter = new TheTaskPresenter(new TheTaskCall());

        UserByUserIdPresenter userByUserIdPresenter = new UserByUserIdPresenter(new UserByIdClass());
        userByUserIdPresenter.reqeust(userid, sessionid);

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        date1 = (TextView) findViewById(R.id.date);
        emaile = (TextView) findViewById(R.id.emaile);
        final LinearLayout qianll = (LinearLayout) findViewById(R.id.qianll);
        qianll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyWanshanActivity.this,QianActivity.class);
                intent.putExtra("ming",qian111);
                startActivityForResult(intent,11);
            }
        });

        TextView wancheng = (TextView) findViewById(R.id.wancheng);
        wancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = name.getText().toString().trim();
                String trim1 = sex.getText().toString().trim();
                String trim2 = date1.getText().toString().trim();
                String trim3 = emaile.getText().toString().trim();
                PerfectUserInfoPresenter perfectUserInfoPresenter = new PerfectUserInfoPresenter(new PerfectCall());
                int sex11=1;
                if (trim1.equals("男")){
                    sex11=1;
                }else {
                    sex11=2;
                }
                perfectUserInfoPresenter.reqeust(userid,sessionid,trim,sex11,qian111,trim2,trim3);
            }
        });

        LinearLayout chu = (LinearLayout) findViewById(R.id.chu);
        chu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerView pvTime = new TimePickerView.Builder(MyWanshanActivity.this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

                        date1.setText(sf.format(date)+"");
                    }
                }).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示.setCancelText("取消")
                        .setSubmitText("确定").build();
                pvTime.show();

            }
        });

        LinearLayout sexll = (LinearLayout) findViewById(R.id.sexll);
        sexll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String items[] = {"男", "女"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MyWanshanActivity.this,0);
                builder.setSingleChoiceItems(items, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sex.setText(items[which]);
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
        });

        LinearLayout youll = (LinearLayout) findViewById(R.id.youll);
        youll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editemail = new EditText(MyWanshanActivity.this);
                editemail.setText(emaile111);
                AlertDialog builder3 = new AlertDialog.Builder(MyWanshanActivity.this)
                        .setTitle("修改邮箱")
                        .setView(editemail)//设置输入框
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String trim = editemail.getText().toString().trim();
                                boolean email1 = isEmail(trim + "");
                                if (email1) {
                                    emaile.setText(trim);
                                } else {
                                    UIUtils.showToastSafe("请输入正确的邮箱");
                                    return;
                                }
                            }
                        }).setNegativeButton("取消", null).create();
                builder3.show();
            }
        });

        LinearLayout nichengll = (LinearLayout) findViewById(R.id.nichengll);
        nichengll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText editemail = new EditText(MyWanshanActivity.this);
                editemail.setText(name111);
                AlertDialog builder3 = new AlertDialog.Builder(MyWanshanActivity.this)
                        .setTitle("修改昵称")
                        .setView(editemail)//设置输入框
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String trim = editemail.getText().toString().trim();
                                if(trim.equals("")){
                                    Toast.makeText(MyWanshanActivity.this, "请输入昵称……", Toast.LENGTH_SHORT).show();
                                return;
                                }
                                    name.setText(trim);
                            }
                        }).setNegativeButton("取消", null).create();
                builder3.show();

            }
        });
    }

    @Override
    protected void destoryData() {

    }

    private class UserByIdClass implements DataCall<Result<UserInfoBean>> {



        @Override
        public void success(Result<UserInfoBean> data) {
            UserInfoBean result = data.getResult();
            name.setText(result.getNickName());
            sex.setText(result.getSex()==1?"男":"女");
            if(result.getBirthday()!=-28800000){
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                Date date2 = new Date(result.getBirthday());
                date1.setText(sf.format(date2)+"");
            }else {
                date1.setText("");
            }

            emaile.setText(result.getEmail());
            qian111=result.getSignature();
            emaile111 = result.getEmail();
            name111 = result.getNickName();
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    private class PerfectCall implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(MyWanshanActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
        if(data.getStatus().equals("0000")){
            finish();
            theTaskPresenter.reqeust(userid,sessionid,1006);
        }
        }

        @Override
        public void fail(ApiException e) {

        }
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
