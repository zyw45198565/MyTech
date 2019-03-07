package com.wd.tech.presenter;

import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * 添加收藏
 */
public class ModifyGroupDescriptionPresenter extends WDPresenter{
    public ModifyGroupDescriptionPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.modifyGroupDescription((int)args[0],(String)args[1],(int) args[2],(String) args[3]);
    }
}
