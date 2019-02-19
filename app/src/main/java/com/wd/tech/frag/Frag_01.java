package com.wd.tech.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wd.tech.R;
import com.wd.tech.utils.util.WDFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tech
 * @date 2019/2/18 20:28
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class Frag_01 extends WDFragment implements View.OnClickListener {


    @BindView(R.id.one_menu)
    ImageView oneMenu;
    @BindView(R.id.one_search)
    ImageView oneSearch;

    @Override
    public String getPageName() {
        return "Frag_资讯" +
                "";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_01;
    }

    @Override
    protected void initView() {
        oneMenu.setOnClickListener(this);
        oneSearch.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.one_menu:
                break;


        }
    }
}
