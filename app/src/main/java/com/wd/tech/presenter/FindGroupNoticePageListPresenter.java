package com.wd.tech.presenter;

import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;


public class FindGroupNoticePageListPresenter extends WDPresenter {
    private int page = 1;
    public FindGroupNoticePageListPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    public Observable observable(Object... args) {
        boolean isRefresh = (boolean)args[2];
        if (isRefresh){
            page=1;
        }else{
            page++;
        }
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.findGroupNoticePageList((int) args[0],(String)args[1],page,(int)args[3]);
    }
}
