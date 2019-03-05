package com.wd.tech.presenter;

import com.wd.tech.bean.Result;
import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * Description:<br>
 * Author:GJ<br>
 * Date:2019/2/25 9:36
 */
public class VipCommodityListPresenter extends WDPresenter{

    public VipCommodityListPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.findVipCommodityList();
    }
}
