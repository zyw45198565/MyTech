package com.wd.tech.presenter;

import com.wd.tech.bean.Result;
import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * Description:<br>
 * Author:GJ<br>
 * Date:2019/3/4 18:57
 */
public class CancelCommunityGreatPresenter extends WDPresenter{
    public CancelCommunityGreatPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable<Result> observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.cancelCommunityGreat((int)args[0],(String) args[1],(int)args[2]);
    }
}
