package com.wd.tech.presenter;



import com.wd.tech.core.Interfacea;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.NetWorkManager;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author dingtao
 * @date 2018/12/28 11:23
 * qq:1940870847
 */

public class ModifyHeadPicPresenter extends WDPresenter {

    private File file;

    public ModifyHeadPicPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable observable(Object... args) {
        if((int)args[3]==1){
            file = new File((String) args[2]);
        }else {
            file= (File) args[2];
        }

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("multipart/octet-stream"), file));

        Interfacea iRequest = NetWorkManager.getInstance().create(Interfacea.class);
        return iRequest.modifyHeadPic((int)args[0],(String)args[1],builder.build());
    }


}

