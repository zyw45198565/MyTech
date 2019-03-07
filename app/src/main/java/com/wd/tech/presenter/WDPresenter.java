package com.wd.tech.presenter;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.wd.tech.bean.Result;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.CustomException;
import com.wd.tech.utils.exception.ResponseTransformer;
import com.wd.tech.utils.util.WDActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;



public abstract class WDPresenter {
    private boolean running;
    private DataCall dataCall;

    public WDPresenter(DataCall dataCall) {
        this.dataCall = dataCall;
    }

    protected abstract Observable<Result> observable(Object... args);

    public void reqeust(Object... args) {
        if (running)
            return;
        running = true;
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
                        running = false;
                        /*if (result.getStatus().equals("9999")){
                            Dialog dialog = new AlertDialog.Builder(WDActivity.getForegroundActivity()).setMessage("请登录")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            WDActivity.getForegroundActivity().startActivity(new Intent(WDActivity.getForegroundActivity(), LoginActivity.class));
                                        }
                                    })
                                    .setNegativeButton("取消",null)
                                    .show();

                        }else {*/
                            if (dataCall!=null){
                                dataCall.success(result);
                            }
                       // }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        running = false;
                        // 处理异常
//                        UIUtils.showToastSafe("请求失败");
                        if (dataCall!=null) {
                            dataCall.fail(CustomException.handleException(throwable));
                        }
                    }
                });

    }
    public boolean isRunning() {
        return running;
    }

    public void unBind() {
        dataCall = null;
    }
}
