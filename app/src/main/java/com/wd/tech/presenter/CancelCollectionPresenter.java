package com.wd.tech.presenter;


import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * 取消收藏（支持批量操作）
 */
public class CancelCollectionPresenter extends WDPresenter{
    public CancelCollectionPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.cancelCollection((int)args[0],(String)args[1],(String)args[2]);
    }
}
