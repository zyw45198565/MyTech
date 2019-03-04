package com.wd.tech.wxapi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;
import com.wd.tech.activity.DetailsActivity;
import com.wd.tech.activity.VipActivity;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    private Dialog dialog;
    private TextView or;
    private TextView message;
    private TextView read;
    private ImageView close;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
//        setContentView(R.layout.activity_vip);

        api = WXAPIFactory.createWXAPI(this, "wx4c96b6b8da494224");//第二个参数为APPID
        api.handleIntent(getIntent(), this);
        message = findViewById(R.id.message);
        close = findViewById(R.id.close);
        read = findViewById(R.id.read);
        or = findViewById(R.id.or);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WXPayEntryActivity.this,DetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    finish();
                    //支付成功后的逻辑
                    result = "微信支付成功";
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    finish();

                    result = "微信支付失败";
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    finish();

                    result = "微信支付取消";
                    break;
                default:
                    finish();

                    result = "微信支付未知异常：";
                    break;
            }
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        if (result.equals("微信支付成功")){

            message.setText("您已成功购买会员，会员期间可以免费阅读所有付费资讯");
            read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(1, new Intent());
                    finish();

                }
            });
        }else{
            or.setText("购买失败");
        }
    }

}
