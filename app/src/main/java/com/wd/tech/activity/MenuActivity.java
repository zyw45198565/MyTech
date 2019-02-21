package com.wd.tech.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.adapter.MenusAdapter;
import com.wd.tech.bean.MenusBean;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.MenusrPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.WDActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends BaseActivity {

    @BindView(R.id.menus_recycler)
    RecyclerView menusRecycler;
    private MenusAdapter menusAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_menu;
    }

    @Override
    protected void initView() {
        MenusrPresenter menusrPresenter=new MenusrPresenter(new MenusCall());
        menusrPresenter.reqeust();

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        menusRecycler.setLayoutManager(gridLayoutManager);
        menusAdapter = new MenusAdapter(this);
        menusRecycler.setAdapter(menusAdapter);
    }

    @Override
    protected void destoryData() {

    }

    private class MenusCall implements DataCall<Result<List<MenusBean>>> {
        @Override
        public void success(Result<List<MenusBean>> data) {
//            Toast.makeText(getBaseContext(),data.getStatus()+data.getMessage(),Toast.LENGTH_SHORT).show();

            List<MenusBean> result = data.getResult();
            menusAdapter.addAll(result);
            menusAdapter.notifyDataSetChanged();
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}
