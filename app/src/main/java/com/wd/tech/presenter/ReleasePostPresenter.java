package com.wd.tech.presenter;

import com.wd.tech.bean.Result;
import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import java.io.File;

import io.reactivex.Observable;

/**
 * Description:<br>
 * Author:GJ<br>
 * Date:2019/2/22 21:16
 */
public class ReleasePostPresenter extends WDPresenter{

    public ReleasePostPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable<Result> observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.releasePost((int)args[0],(String) args[1],(String) args[2],(File) args[3]);
    }
}
