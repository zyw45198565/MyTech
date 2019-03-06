package com.wd.tech.presenter;


import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;


public class ModifyPermissionPresenter extends WDPresenter {
    public ModifyPermissionPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        Interfacea iRequest = NetWorkManager.getInstance().create(Interfacea.class);
        return iRequest.modifyPermission((int)args[0],(String)args[1],(int)args[2],(int)args[3],(int)args[4]);
    }
}
