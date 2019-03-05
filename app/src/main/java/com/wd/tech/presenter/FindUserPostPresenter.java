package com.wd.tech.presenter;

import com.wd.tech.bean.Result;
import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * 用户发布的帖子
 * Description:<br>
 * Author:GJ<br>
 * Date:2019/2/28 9:59
 */
public class FindUserPostPresenter extends WDPresenter{

    private int page = 1;
    public FindUserPostPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        boolean flag = (boolean) args[3];//获取刷新加载更多状态
        if(flag){
            page++;
        }else{
            page = 1;
        }
        Interfacea interfacea = NetWorkManager.getInstance().create(Interfacea.class);
        return interfacea.findUserPostById((int)args[0],(String) args[1],(int)args[2],page,(int)args[4]);
    }
}
