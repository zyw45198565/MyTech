package com.wd.tech.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.FinUserSignPresenter;
import com.wd.tech.presenter.FinUserSignRecordingPresenter;
import com.wd.tech.presenter.TheTaskPresenter;
import com.wd.tech.presenter.UserSignPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.view.SignCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SignCalendarActivity extends BaseActivity {

    private SignCalendar calendar;
    private String date;
    private TextView tv_sign_year_month;
    List<String> list = new ArrayList<String>();
    private int month;
    private int year;
    private Button rlBtnSign;
    private UserSignPresenter userSignPresenter;
    private FinUserSignPresenter findUserSignPresenter;
    private FinUserSignRecordingPresenter findUserSignRecordingPresenter;
    private int userid;
    private String sessionid;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_calendar;
    }

    @Override
    protected void initView() {
        findUserSignRecordingPresenter = new FinUserSignRecordingPresenter(new FinduserRecordResult());

        userid = WDApp.getShare().getInt("userid", 1);
        sessionid = WDApp.getShare().getString("sessionid", "");
        //接收传递过来的初始化数据
//        SignCalendarReq signCalendarReq = (SignCalendarReq) getIntent().getSerializableExtra("userInfos");

        //用户签到
        userSignPresenter = new UserSignPresenter(new UserSignResult());
        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);
        //当天签到状态
        findUserSignPresenter = new FinUserSignPresenter(new FindUserResult());
        findUserSignPresenter.reqeust(userid,sessionid);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        date = formatter.format(curDate);
        calendar = (SignCalendar) findViewById(R.id.sc_main);
        tv_sign_year_month = (TextView) findViewById(R.id.tv_sign_year_month);

        rlBtnSign = (Button) findViewById(R.id.rl_btn_sign);
        calendar.setOnCalendarDateChangedListener(new SignCalendar.OnCalendarDateChangedListener() {
            @Override
            public void onCalendarDateChanged(int year, int month) {
                tv_sign_year_month.setText(year + "年" + (month) + "月");//设置日期
            }
        });
        tv_sign_year_month.setText(year + "年" + (month + 1) + "月");//设置日期
        Log.i("GT", "month：" + month);
//            if (signCalendarReq.getState().getCode() == 1) {
//                dataBean = signCalendarReq.getData();
//                String signDay = dataBean.getSignDay();
//                String[] splitDay = signDay.split(",");
//                for (int i = 0; i < splitDay.length; i++) {
//                    if (Integer.parseInt(splitDay[i]) < 10) {
//                        list.add(year + "-" + (month + 1) + "-0" + splitDay[i]);
//                    } else {
//                        list.add(year + "-" + (month + 1) + "-" + splitDay[i]);
//                    }
//                }
//                calendar.addMarks(list, 0);

        rlBtnSign.setBackgroundResource(R.drawable.click_btn_shapert);
        rlBtnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignPresenter.reqeust(userid, sessionid);
            }
        });

       /* rlQuedingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlGetGiftData.setVisibility(View.GONE);
                rlBtnSign.setVisibility(View.VISIBLE);
            }
        });*/
        calendar.setCalendarDayBgColor(date, R.drawable.jintian);
        //查询用户当月所有签到的日期
        findUserSignRecordingPresenter.reqeust(userid, sessionid);

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void destoryData() {

    }

    /**
     * 签到回调接口
     */
    private class UserSignResult implements DataCall<Result> {
        @Override
        public void success(Result result) {
            if (result.getStatus().equals("0000")) {
                calendar.setCalendarDayBgColor(date, R.drawable.yiqiandao);
                calendar.textColor();
                rlBtnSign.setText("已签到");
                rlBtnSign.setClickable(false);
                TheTaskPresenter theTaskPresenter = new TheTaskPresenter(new TheTaskCall());
                theTaskPresenter.reqeust(userid,sessionid,1001);
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    /**
     * 查看当天签到状态
     */
    private class FindUserResult implements DataCall<Result> {
        private Double resultInt;

        @Override
        public void success(Result result) {
            resultInt = (Double) result.getResult();
            if (resultInt == 1) {
                calendar.setCalendarDayBgColor(date, R.drawable.yiqiandao);
                calendar.textColor();
                rlBtnSign.setText("已签到");
                rlBtnSign.setClickable(false);
            }else{
                calendar.textColor();
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    /**
     * 当月签到日
     */
    private class FinduserRecordResult implements DataCall<Result> {
        @Override
        public void success(Result result) {
            List<String> SignCalendList = (List<String>) result.getResult();
            for (int i = 0; i < SignCalendList.size(); i++) {
                calendar.setCalendarDayBgColor(SignCalendList.get(i), R.drawable.yiqiandao);
            }
            calendar.textColor();
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
