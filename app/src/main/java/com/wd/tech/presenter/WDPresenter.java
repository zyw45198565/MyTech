package com.wd.tech.presenter;


import com.wd.tech.bean.Result;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.CustomException;
import com.wd.tech.utils.exception.ResponseTransformer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;



public abstract class WDPresenter {
    private DataCall dataCall;

    public WDPresenter(DataCall dataCall) {
        this.dataCall = dataCall;
    }

    protected abstract Observable<Result> observable(Object... args);

    public void reqeust(Object... args) {
        observable(args)
                .compose(ResponseTransformer.handleResult())
                .compose(new ObservableTransformer() {
                    @Override
                    public ObservableSource apply(Observable upstream) {
                        return upstream.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        dataCall.success(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 处理异常
//                        UIUtils.showToastSafe("请求失败");
                        dataCall.fail(CustomException.handleException(throwable));
                    }
                });

    }

    public void unBind() {
        dataCall = null;
    }
}
