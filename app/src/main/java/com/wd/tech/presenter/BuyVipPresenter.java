package com.wd.tech.presenter;



import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * 用户购买VIP
 */

public class BuyVipPresenter extends WDPresenter {

    public BuyVipPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea iRequest = NetWorkManager.getInstance().create(Interfacea.class);
        return iRequest.buyVip((int)args[0],(String)args[1],(int)args[2],(String) args[3]);
    }


}

