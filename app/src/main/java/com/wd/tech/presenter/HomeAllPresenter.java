package com.wd.tech.presenter;

import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/*
资讯推荐展示列表(包含单独板块列表展示)
 */
public class HomeAllPresenter extends WDPresenter{
    public HomeAllPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.infoRecommendList((int)args[0],(String)args[1],(int)args[2],(int)args[3],(int)args[4]);
    }


}
