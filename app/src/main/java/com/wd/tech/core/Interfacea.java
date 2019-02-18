package com.wd.tech.core;

import com.wd.tech.bean.Result;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author Tech
 * @date 2019/2/18 16:20
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public interface Interfacea {


    /**
     * 注册
     * @param phone
     * @param nickName
     * @param pwd
     * @return
     */
    @FormUrlEncoded
    @POST("user/v1/register")
    Observable<Result> getRegisterData(@Field("phone") String phone,
                                       @Field("nickName") String nickName,
                                       @Field("pwd") String pwd);


    /**
     * 登陆
     * @param phone
     * @param pwd
     * @return
     */
    @FormUrlEncoded
    @POST("user/v1/login")
    Observable<Result> login(@Field("phone") String phone,
                                       @Field("pwd") String pwd);

}
