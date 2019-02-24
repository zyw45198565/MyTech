package com.wd.tech.presenter;

import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * Description:资讯用户评论
 * Author:GJ<br>
 * Date:2019/2/19 11:38
 */
public class AddInfoCommentPresenter extends WDPresenter{

    public AddInfoCommentPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.addInfoComment((int)args[0],(String) args[1],(String)args[2],(int)args[3]);
    }
}
