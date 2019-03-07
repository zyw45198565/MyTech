package com.wd.tech.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.LoginBean;
import com.wd.tech.bean.Result;
import com.wd.tech.hractivity.DetecterActivity;
import com.wd.tech.presenter.LoginPresenter;
import com.wd.tech.presenter.UserByUserIdPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.RsaCoder;
import com.wd.tech.utils.util.UIUtils;
import com.wd.tech.utils.util.WDActivity;

/**
 * @author Tech
 * @date 2019/2/19 11:52
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener,DataCall<Result<LoginBean>> {


    private static final int REQUEST_CODE_OP = 3;
    private EditText phone;
    private EditText pass;
    private Button login;
    private String passss;
    private String trim;
    private String trim1;
    private ImageView xian;
    private IWXAPI api;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this,"wx4c96b6b8da494224",true);
        //将应用的appid注册到微信
        api.registerApp("wx4c96b6b8da494224");


        TextView txt_registration = (TextView) findViewById(R.id.registration);
        txt_registration.setOnClickListener(this);



        phone = (EditText) findViewById(R.id.phone);
        pass = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        xian = (ImageView) findViewById(R.id.xian);
        xian.setOnClickListener(this);

        String phone111 = WDApp.getShare().getString("phone", "");
        String pass111 = WDApp.getShare().getString("pass", "");
        phone.setText(phone111);
        pass.setText(pass111);

        ImageView weixin = (ImageView) findViewById(R.id.weixin);
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_微信登录"; // 自行填写
                api.sendReq(req);
               finish();
            }
        });
        ImageView face = (ImageView) findViewById(R.id.face);
        face.setOnClickListener(this);
    }

    @Override
    protected void destoryData() {

    }

    @Override
    public void onClick(View v) {
                switch (v.getId()){
                    case R.id.registration:
                        Intent intent = new Intent(LoginActivity.this,RegistrActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.login:
                        trim = phone.getText().toString().trim();
                        trim1 = pass.getText().toString().trim();
                        if(trim.equals("")|| trim1.equals("")){
                            Toast.makeText(this, "请输入手机号或密码……", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean b = validatePhonePass(trim1);
                        if(!b){
                            UIUtils.showToastSafe("密码最少8位包含字母和数字");
                            return;
                        }
                        try {
                            passss = RsaCoder.encryptByPublicKey(trim1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LoginPresenter loginPresenter = new LoginPresenter(this);
                        loginPresenter.reqeust(trim,passss);
                        mLoadDialog.show();
                        break;
                    case R.id.xian:
                        showOrHide(pass);
                        break;
                    case R.id.face:
                        if( ((WDApp)getApplicationContext()).mFaceDB.mRegister.isEmpty() ) {
                            Toast.makeText(this, "没有注册人脸，请先注册！", Toast.LENGTH_SHORT).show();
                        } else {
                            new AlertDialog.Builder(this)
                                    .setTitle("请选择相机")
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setItems(new String[]{"后置相机", "前置相机"}, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startDetector(which);
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                        break;
                }
    }

    @Override
    public void success(Result<LoginBean> data) {
        mLoadDialog.dismiss();
        Toast.makeText(this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
        if(data.getStatus().equals("0000")){
            SharedPreferences share = WDApp.getShare();
            SharedPreferences.Editor edit = share.edit();
            edit.putString("phone",trim);
            edit.putString("pass",trim1);
            edit.putInt("userid",data.getResult().getUserId());
            edit.putString("sessionid",data.getResult().getSessionId());
            edit.putString("userName",data.getResult().getUserName());
            edit.putString("headPic",data.getResult().getHeadPic());
            edit.putString("nickName",data.getResult().getNickName());
            edit.putBoolean("zai",true);
            edit.commit();
            EMClient.getInstance().login(data.getResult().getUserName(),data.getResult().getPwd(),new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    Log.d("main", "登录聊天服务器成功！");
                    finish();
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    Log.d("main", "登录聊天服务器失败！");
                    finish();
                }
            });
           // finish();
        }
    }

    @Override
    public void fail(ApiException e) {

    }

    /**
     * 密码显示或隐藏 （切换）
     */
    private void showOrHide(EditText etPassword){
        //记住光标开始的位置
        int pos = etPassword.getSelectionStart();
        if(etPassword.getInputType()!= (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){//隐藏密码
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }else{//显示密码
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        etPassword.setSelection(pos);

    }

    private void startDetector(int camera) {
        Intent it = new Intent(LoginActivity.this, DetecterActivity.class);
        it.putExtra("Camera", camera);
        startActivityForResult(it, REQUEST_CODE_OP);
    }

    public static boolean validatePhonePass(String pass) {
        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        return !TextUtils.isEmpty(pass) && pass.matches(passRegex);
    }
}
