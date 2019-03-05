package com.wd.tech.wxapi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.WDApp;
import com.wd.tech.bean.LoginBean;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.WxLoginPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;

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
            finish();
        }else if(resp.getType()==ConstantsAPI.COMMAND_SENDAUTH){//登陆
            Log.i("ansen", "微信登录操作.....");
//


            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Log.e("flag", "-----code:ok");
                    if (resp instanceof SendAuth.Resp) {
                        SendAuth.Resp sendAuthResp = (SendAuth.Resp) resp;
                        String code = sendAuthResp.code;
                        getAccessToken(code);
                        // 发起登录请求
                        Log.e("flag", "-----code:" + sendAuthResp.code);
                        WxLoginPresenter wxLoginPresenter = new WxLoginPresenter(new Wxlogin());
                        String versionName = "";
                        try {
                            versionName = getVersionName(WXEntryActivity.this);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        wxLoginPresenter.reqeust(versionName,sendAuthResp.code);
                    }
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    if (resp instanceof SendAuth.Resp) {}
                    Log.e("flag", "-----授权取消:");
                    Toast.makeText(this, "授权取消:", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    if (resp instanceof SendAuth.Resp) {}
                    Log.e("flag", "-----授权失败:");
                    Toast.makeText(this, "授权失败:", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;
            }
        }

    }

    // 根据授权code  获取AccessToken
    public void getAccessToken(String code){

    }

    private class Wxlogin implements DataCall<Result<LoginBean>> {
        @Override
        public void success(Result<LoginBean> data) {
            Toast.makeText(WXEntryActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
            if (data.getStatus().equals("0000")){
                SharedPreferences share = WDApp.getShare();
                SharedPreferences.Editor edit = share.edit();
                edit.putInt("userid",data.getResult().getUserId());
                edit.putString("sessionid",data.getResult().getSessionId());
                edit.putString("userName",data.getResult().getUserName());
                edit.putString("pwd",data.getResult().getPwd());
                edit.putBoolean("zai",true);
                edit.commit();

            }
            finish();
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    /**
     * 获取版本号
     */
    public static String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

}
