package com.wd.tech.presenter;


import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * Created by ${LinJiangtao}
 * on 2019/2/21
 */
public class FindConversationListPresenter extends WDPresenter {
    public FindConversationListPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        Interfacea iRequest = NetWorkManager.getInstance().create(Interfacea.class);
        return iRequest.findConversationList((int)args[0],(String)args[1],(String) args[2]);
    }
}
