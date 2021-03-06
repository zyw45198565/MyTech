package com.wd.tech.presenter;

import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;


public class BatchInviteAddGroupPresenter extends WDPresenter {
    public BatchInviteAddGroupPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea iRequest = NetWorkManager.getInstance().create(Interfacea.class);
        return iRequest.batchInviteAddGroup((int)args[0],(String) args[1],(int)args[2],(Object[]) args[3]);
    }
}
