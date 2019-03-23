package com.wd.tech.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.PayBean;
import com.wd.tech.bean.PayResult;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.VIPList;
import com.wd.tech.core.Interfacea;
import com.wd.tech.presenter.BuyVipPresenter;
import com.wd.tech.presenter.PayPresenter;
import com.wd.tech.presenter.VipCommodityListPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.exception.ResponseTransformer;
import com.wd.tech.utils.util.MD5Utils;
import com.wd.tech.wxapi.WXPayEntryActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    int wxozfb=1;

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String,String>)msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(VipActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(VipActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(VipActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(VipActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };
    private PayPresenter payPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        userid = WDApp.getShare().getInt("userid", 0);
        sessionid = WDApp.getShare().getString("sessionid", "");
        VipCommodityListPresenter vipCommodityListPresenter=new VipCommodityListPresenter(new VIPListcall());
        vipCommodityListPresenter.reqeust();

        //金额
        mymoney();
        Intent intent = getIntent();
        did = intent.getIntExtra("did", 0);
        dialog = new Dialog(VipActivity.this, R.style.DialogTheme);
        btnWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wxozfb=1;
            }
        });
        btnZhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wxozfb=2;
            }
        });
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
        private String payInfo;

        @Override
        public void success(Result data) {
            Toast.makeText(VipActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                String orderId = data.getOrderId();
            Log.i("buyorderid",orderId);

                Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
            payPresenter = new PayPresenter(new PayCall());
            payPresenter.reqeust(userid,sessionid,orderId,wxozfb);
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

    private class PayCall implements DataCall<Result<String>> {
        private String payInfo;

        @Override
        public void success(Result<String> data) {
            if (wxozfb==1){
            //应用ID 即微信开放平台审核通过的应用APPID
            wxapi = WXAPIFactory.createWXAPI(VipActivity.this,"wx4c96b6b8da494224");
            wxapi.registerApp("wx4c96b6b8da494224");    //应用ID
            PayReq payReq = new PayReq();
            payReq.appId =data.getAppId();        //应用ID
            payReq.partnerId = data.getPartnerId();      //商户号 即微信支付分配的商户号
            payReq.prepayId = data.getPrepayId();        //预支付交易会话ID
            payReq.packageValue = data.getPackageValue();    //扩展字段
            payReq.nonceStr = data.getNonceStr();        //随机字符串不长于32位。
            payReq.timeStamp = "" + data.getTimeStamp(); //时间戳
            payReq.sign = data.getSign();             //签名
            wxapi.sendReq(payReq);

            }else if(wxozfb==2){
                payInfo = data.getResult();
                //final String payInfo = mOrderId;   // 订单信息


                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(VipActivity.this);
                        // 调用支付接口，获取支付结果
                        Map<String, String> result = alipay.payV2(payInfo,true);

                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();

            }

        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
