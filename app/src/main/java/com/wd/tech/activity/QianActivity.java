package com.wd.tech.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.tech.R;

public class QianActivity extends BaseActivity {


    private EditText mEditQianSig;
    private int num = 40;
    private int nownum;
    private TextView mTextNumSig;
    private String ming;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_qian;
    }

    @Override
    protected void initView() {

        Intent intent = getIntent();
        ming = intent.getStringExtra("ming");

        mEditQianSig = (EditText) findViewById(R.id.medit_qian_sig);
        mTextNumSig = (TextView) findViewById(R.id.mtext_num_sig);
        ImageView mreturn = (ImageView) findViewById(R.id.mreturn);
        mreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button mbutton_sig = (Button) findViewById(R.id.mbutton_sig);
        mbutton_sig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = mEditQianSig.getText().toString().trim();
                if(trim.equals("")){
                    Toast.makeText(QianActivity.this, "请输入内容……", Toast.LENGTH_SHORT).show();
                    return;
                }
                ming=trim;
                Intent intent1 = new Intent();
                intent1.putExtra("ming1",ming);
                setResult(1,intent1);
                finish();
            }
        });

        mEditQianSig.setText(ming);
        //设置监听
        mEditQianSig.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum= s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                nownum = mEditQianSig.length();
                //TextView显示剩余字数
                mTextNumSig.setText("" +nownum+"/"+num);
                selectionStart = mEditQianSig.getSelectionStart();
                selectionEnd = mEditQianSig.getSelectionEnd();
                if (wordNum.length() > num) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    mEditQianSig.setText(s);
                    mEditQianSig.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

    }

    @Override
    protected void destoryData() {

    }
}
