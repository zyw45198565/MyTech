package com.wd.tech.utils;


import com.wd.tech.utils.exception.ApiException;

/**
 * @author dingtao
 * @date 2018/12/30 10:30
 * qq:1940870847
 */
public interface DataCall<T> {

    void success(T data);
    void fail(ApiException e);

}
