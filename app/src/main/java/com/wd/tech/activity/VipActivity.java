package com.wd.tech.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.PayBean;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.VIPList;
import com.wd.tech.core.Interfacea;
import com.wd.tech.presenter.BuyVipPresenter;
import com.wd.tech.presenter.VipCommodityListPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.exception.ResponseTransformer;
import com.wd.tech.utils.util.MD5Utils;
import com.wd.tech.wxapi.WXPayEntryActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VipActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.year)
    RadioButton year;
    @BindView(R.id.quarter)
    RadioButton quarter;
    @BindView(R.id.month)
    RadioButton month;
    @BindView(R.id.week)
    RadioButton week;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.t_integral)
    TextView tIntegral;
    @BindView(R.id.myweixin)
    LinearLayout myweixin;
    @BindView(R.id.btn_weixin)
    RadioButton btnWeixin;
    @BindView(R.id.btn_zhifubao)
    RadioButton btnZhifubao;
    @BindView(R.id.convert)
    TextView convert;
    private int userid;
    private String sessionid;
    private int did;
    private IWXAPI wxapi;
    private int mycommodityId;
    private List<VIPList> result;
    private Dialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    protected void initView() {
        userid = WDApp.getShare().getInt("userid", 0);
        sessionid = WDApp.getShare().getString("sessionid", "");
        VipCommodityListPresenter vipCommodityListPresenter=new VipCommodityListPresenter(new VIPListcall());
        vipCommodityListPresenter.reqeust();

        //金额
        mymoney();
        Intent intent = getIntent();
        did = intent.getIntExtra("did", 0);
        dialog = new Dialog(VipActivity.this, R.style.DialogTheme);

    }

    private void mymoney() {
        year.setOnClickListener(this);
        quarter.setOnClickListener(this);
        month.setOnClickListener(this);
        week.setOnClickListener(this);
        back.setOnClickListener(this);
        convert.setOnClickListener(this);
    }

    @Override
    protected void destoryData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.year:
                for (int i = 0; i < result.size(); i++) {
                    int commodityId = result.get(i).getCommodityId();
                    if (commodityId==1004){
                        money.setText(result.get(i).getPrice()+ "");
                        mycommodityId=result.get(i).getCommodityId();

                    }
                }
                quarter.setChecked(false);
                month.setChecked(false);
                week.setChecked(false);
                break;
            case R.id.quarter:

                for (int i = 0; i < result.size(); i++) {
                    int commodityId = result.get(i).getCommodityId();
                    if (commodityId==1003){
                        money.setText(result.get(i).getPrice()+ "");
                        mycommodityId=result.get(i).getCommodityId();

                    }
                }

                year.setChecked(false);
                month.setChecked(false);
                week.setChecked(false);
                break;
            case R.id.month:
                for (int i = 0; i < result.size(); i++) {
                    int commodityId = result.get(i).getCommodityId();
                    if (commodityId==1002){
                        money.setText(result.get(i).getPrice()+ "");
                        mycommodityId=result.get(i).getCommodityId();

                    }
                }

                quarter.setChecked(false);
                year.setChecked(false);
                week.setChecked(false);
                break;
            case R.id.week:
                for (int i = 0; i < result.size(); i++) {
                    int commodityId = result.get(i).getCommodityId();
                    if (commodityId==1001){
                        money.setText(result.get(i).getPrice()+ "");
                        mycommodityId=result.get(i).getCommodityId();

                    }
                }

                quarter.setChecked(false);
                month.setChecked(false);
                year.setChecked(false);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.convert:
                BuyVipPresenter buyVipPresenter = new BuyVipPresenter(new BuyVip());
                String md5 = userid + "" + mycommodityId + "" + "tech";
                String s = MD5Utils.md5(md5);
                buyVipPresenter.reqeust(userid, sessionid, mycommodityId, s);
                break;
        }
    }

    private class BuyVip implements DataCall<Result> {
        @SuppressLint("CheckResult")
        @Override
        public void success(Result data) {
            Toast.makeText(VipActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                String orderId = data.getOrderId();
            Log.i("buyorderid",orderId);
                Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
                interfacea.pay(userid, sessionid, orderId, 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<PayBean>() {
                            @Override
                            public void accept(PayBean payBean){
                                //应用ID 即微信开放平台审核通过的应用APPID
                                wxapi = WXAPIFactory.createWXAPI(VipActivity.this,"wx4c96b6b8da494224");
                                wxapi.registerApp("wx4c96b6b8da494224");    //应用ID
                                PayReq payReq = new PayReq();
                                payReq.appId =payBean.getAppId();        //应用ID
                                payReq.partnerId = payBean.getPartnerId();      //商户号 即微信支付分配的商户号
                                payReq.prepayId = payBean.getPrepayId();        //预支付交易会话ID
                                payReq.packageValue = payBean.getPackageValue();    //扩展字段
                                payReq.nonceStr = payBean.getNonceStr();        //随机字符串不长于32位。
                                payReq.timeStamp = "" + payBean.getTimeStamp(); //时间戳
                                payReq.sign = payBean.getSign();             //签名
                                wxapi.sendReq(payReq);
//                                Intent intent = getIntent();
//                                String state = intent.getStringExtra("state");


                            }
                        }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        throwable.printStackTrace();
                                        Toast.makeText(VipActivity.this,"失败了",Toast.LENGTH_SHORT).show();

                                    }
                                });
            /*if (data.getStatus().equals("0000")){


            }*/

        }

        @Override
        public void fail(ApiException e) {

        }
    }

    private class VIPListcall implements DataCall<Result<List<VIPList>>> {
        @Override
        public void success(Result<List<VIPList>> data) {
            result = data.getResult();


            for (int i = 0; i < result.size(); i++) {
                int commodityId = result.get(i).getCommodityId();
                if (commodityId==1004){
                    money.setText(result.get(i).getPrice()+ "");
                    mycommodityId=result.get(i).getCommodityId();
                }
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
