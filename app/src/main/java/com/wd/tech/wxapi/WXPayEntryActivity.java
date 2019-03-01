package com.wd.tech.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, "wx4c96b6b8da494224");//第二个参数为APPID
        api.handleIntent(getIntent(), this);
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
                    //支付成功后的逻辑
                    result = "微信支付成功";
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    finish();
                    result = "微信支付失败：" + resp.errCode + "，" + resp.errStr;
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    finish();
                    result = "微信支付取消：" + resp.errCode + "，" + resp.errStr;
                    break;
                default:
                    finish();
                    result = "微信支付未知异常：" + resp.errCode + "，" + resp.errStr;
                    break;
            }
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

}
