package com.wd.tech.presenter;



import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * 资讯积分兑换
 */

public class ByIntegralPresenter extends WDPresenter {

    public ByIntegralPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea iRequest = NetWorkManager.getInstance().create(Interfacea.class);
        return iRequest.infoPayByIntegral((int)args[0],(String)args[1],(int)args[2],(int)args[3]);
    }


}

