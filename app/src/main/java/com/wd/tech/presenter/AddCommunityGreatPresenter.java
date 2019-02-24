package com.wd.tech.presenter;

import com.wd.tech.bean.Result;
import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * Description:<br>
 * Author:GJ<br>
 * Date:2019/2/24 19:28
 * 帖子点赞
 */
public class AddCommunityGreatPresenter extends WDPresenter{

    public AddCommunityGreatPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.addCommunityGreat((int)args[0],(String) args[1],(int)args[2]);
    }
}
