package com.wd.tech.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.RegistrPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.RsaCoder;
import com.wd.tech.utils.util.UIUtils;
import com.wd.tech.utils.util.WDActivity;

/**
 * @author Tech
 * @date 2019/2/19 15:29
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class RegistrActivity extends BaseActivity implements View.OnClickListener,DataCall<Result> {

    private EditText phone;
    private EditText name;
    private EditText pass;
    private String passss;
    private String trim2;

    @Override
    protected int getLayoutId() {
        return R.layout.registr_activity;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        pass = (EditText) findViewById(R.id.pass);
        Button regis = (Button) findViewById(R.id.regis);
        regis.setOnClickListener(this);

    }

    @Override
    protected void destoryData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regis:
                String trim = name.getText().toString().trim();
                if(trim.equals("")){
                    Toast.makeText(this, "请输入昵称……", Toast.LENGTH_SHORT).show();
                    return;
                }
                String trim1 = pass.getText().toString().trim();
                trim2 = phone.getText().toString().trim();
                if(trim1.equals("")|| trim2.equals("")){
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
                    Log.i("abc", "onClick: "+passss);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RegistrPresenter registrPresenter = new RegistrPresenter(this);

                registrPresenter.reqeust(trim2,trim,passss);
                break;
        }
    }

    @Override
    public void success(Result data) {
        Toast.makeText(this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
        if(data.getStatus().equals("0000")){
           /* new Thread(new Runnable() {
                public void run() {
                    try {
                        // call method in SDK
                        EMClient.getInstance().createAccount(trim2, passss);
                        runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } catch (final HyphenateException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                int errorCode = e.getErrorCode();
                                if(errorCode== EMError.NETWORK_ERROR){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.USER_ALREADY_EXIST){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.EXCEED_SERVICE_LIMIT){
                                    Toast.makeText(RegistrActivity.this, getResources().getString(R.string.register_exceed_service_limit), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).start();*/
            finish();
        }
    }

    public static boolean validatePhonePass(String pass) {
        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        return !TextUtils.isEmpty(pass) && pass.matches(passRegex);
    }

    @Override
    public void fail(ApiException e) {

    }
}
