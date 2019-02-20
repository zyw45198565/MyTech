package com.wd.tech.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.RegistrPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.RsaCoder;
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

    @Override
    protected int getLayoutId() {
        return R.layout.registr_activity;
    }

    @Override
    protected void initView() {

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
                String trim2 = phone.getText().toString().trim();
                if(trim1.equals("")||trim2.equals("")){
                    Toast.makeText(this, "请输入手机号或密码……", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    passss = RsaCoder.encryptByPublicKey(trim1);
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
            finish();
        }
    }

    @Override
    public void fail(ApiException e) {

    }
}
