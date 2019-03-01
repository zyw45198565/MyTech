package com.wd.tech.presenter;



import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * @author dingtao
 * @date 2018/12/28 11:23
 * qq:1940870847
 */

public class PerfectUserInfoPresenter extends WDPresenter {

    public PerfectUserInfoPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea iRequest = NetWorkManager.getInstance().create(Interfacea.class);
        return iRequest.perfectUserInfo((int)args[0],(String)args[1],(String)args[2],(int)args[3],(String)args[4],(String)args[5],(String)args[6]);
    }


}

