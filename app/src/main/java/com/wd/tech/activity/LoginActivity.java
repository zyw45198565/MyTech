package com.wd.tech.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.LoginBean;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.LoginPresenter;
import com.wd.tech.presenter.UserByUserIdPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.RsaCoder;
import com.wd.tech.utils.util.WDActivity;

/**
 * @author Tech
 * @date 2019/2/19 11:52
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class LoginActivity extends WDActivity implements View.OnClickListener,DataCall<Result<LoginBean>> {


    private EditText phone;
    private EditText pass;
    private Button login;
    private String passss;
    private String trim;
    private String trim1;
    private ImageView xian;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {


        TextView txt_registration =  findViewById(R.id.registration);
        txt_registration.setOnClickListener(this);

        phone = findViewById(R.id.phone);
        pass =  findViewById(R.id.pass);
        login =  findViewById(R.id.login);
        login.setOnClickListener(this);
        xian = findViewById(R.id.xian);
        xian.setOnClickListener(this);

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
            edit.putBoolean("zai",true);
            edit.commit();
            finish();
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
}
