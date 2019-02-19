package com.wd.tech.presenter;

import com.wd.tech.bean.Result;
import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * Description:社区列表展示
 * Author:GJ<br>
 * Date:2019/2/19 11:38
 */
public class FindCommunityListPresenter extends WDPresenter{

    public FindCommunityListPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.findCommunityList((int)args[0],(String) args[1],(int)args[2],(int)args[3]);
    }
}