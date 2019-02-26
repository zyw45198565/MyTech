package com.wd.tech.presenter;

import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * 资讯点赞
 */
public class AddGreatRecordPresenter extends WDPresenter{
    public AddGreatRecordPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.addGreatRecord((int)args[0],(String)args[1],(int)args[2]);
    }
}
