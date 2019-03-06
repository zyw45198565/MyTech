package com.wd.tech.presenter;

import com.wd.tech.bean.Result;
import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import io.reactivex.Observable;

/**
 * Description:<br>
 * Author:GJ<br>
 * Date:2019/2/26 10:17
 */
public class FindCommunityUserCommentListPresenter extends WDPresenter{

    private int page = 1;
    public FindCommunityUserCommentListPresenter(DataCall dataCall) {
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
        return interfacea.findCommunityUserCommentList((int)args[0],(String) args[1],(int)args[2],page,(int)args[4]);
    }
}
