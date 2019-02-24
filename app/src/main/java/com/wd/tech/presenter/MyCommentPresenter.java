package com.wd.tech.presenter;

import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * Description:查询资讯评论列表
 * Author:GJ<br>
 * Date:2019/2/19 11:38
 */
public class MyCommentPresenter extends WDPresenter{

    public MyCommentPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.myComment((int)args[0],(String) args[1],(int)args[2],(int)args[3],(int)args[4]);
    }
}
