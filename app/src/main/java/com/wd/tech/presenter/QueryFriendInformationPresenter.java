package com.wd.tech.presenter;

import com.wd.tech.bean.Result;
import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * Description:查询好友信息
 * Author:GJ<br>
 * Date:2019/3/6 15:29
 */
public class QueryFriendInformationPresenter extends WDPresenter{

    public QueryFriendInformationPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.queryFriendInformation((int)args[0],(String) args[1],(int)args[2]);
    }
}
