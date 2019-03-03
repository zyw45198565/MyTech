package com.wd.tech.wxapi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI wxAPI;
    private static final String APP_ID = "wx4c96b6b8da494224";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wxAPI = WXAPIFactory.createWXAPI(this,"wx4c96b6b8da494224",true);
        wxAPI.registerApp("wx4c96b6b8da494224");
        wxAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        wxAPI.handleIntent(getIntent(),this);
        Log.i("ansen","WXEntryActivity onNewIntent");
    }

    @Override
    public void onReq(BaseReq arg0) {
        Log.i("ansen","WXEntryActivity onReq:"+arg0);
    }

    @Override
    public void onResp(BaseResp resp){
        if(resp.getType()== ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){//分享
            Log.i("ansen","微信分享操作.....");
//            WeiXin weiXin=new WeiXin(2,resp.errCode,"");
//            EventBus.getDefault().post(weiXin);
        }else if(resp.getType()==ConstantsAPI.COMMAND_SENDAUTH){//登陆
            Log.i("ansen", "微信登录操作.....");
            SendAuth.Resp authResp = (SendAuth.Resp) resp;
//            WeiXin weiXin=new WeiXin(1,resp.errCode,authResp.code);
//            EventBus.getDefault().post(weiXin);
        }
        finish();
    }

}
