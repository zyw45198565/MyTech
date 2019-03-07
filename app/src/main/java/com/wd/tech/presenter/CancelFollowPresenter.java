package com.wd.tech.presenter;

import com.baidu.platform.comapi.map.N;
import com.wd.tech.bean.Result;
import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * Description:取消关注
 * Author:GJ<br>
 * Date:2019/3/6 14:16
 */
public class CancelFollowPresenter extends WDPresenter{

    public CancelFollowPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable<Result> observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.cancelFollow((int)args[0],(String) args[1],(int)args[2]);
    }
}
