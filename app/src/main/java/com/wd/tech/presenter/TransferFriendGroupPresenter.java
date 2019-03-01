package com.wd.tech.presenter;


import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;


public class TransferFriendGroupPresenter extends WDPresenter {
    public TransferFriendGroupPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        Interfacea iRequest = NetWorkManager.getInstance().create(Interfacea.class);
        return iRequest.transferFriendGroup((int)args[0],(String)args[1],(int)args[2],(int)args[3]);
    }
}
