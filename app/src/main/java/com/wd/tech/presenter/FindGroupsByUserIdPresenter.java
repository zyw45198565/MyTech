package com.wd.tech.presenter;


import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * Created by ${LinJiangtao}
 * on 2019/2/21
 */
public class FindGroupsByUserIdPresenter extends WDPresenter {
    public FindGroupsByUserIdPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        Interfacea iRequest = NetWorkManager.getInstance().create(Interfacea.class);
        return iRequest.findGroupsByUserId((int)args[0],(String)args[1]);
    }
}
