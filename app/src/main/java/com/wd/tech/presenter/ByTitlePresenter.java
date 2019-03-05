package com.wd.tech.presenter;

import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * 根据标题模糊查询
 */
public class ByTitlePresenter extends WDPresenter{
    public ByTitlePresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.findInformationByTitle((String) args[0],(int)args[1],(int)args[2]);
    }
}
