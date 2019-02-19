package com.wd.tech.core;

import com.wd.tech.bean.HomeAll;
import com.wd.tech.bean.LoginBean;
import com.wd.tech.bean.MenusBean;
import com.wd.tech.bean.MyBanner;
import com.wd.tech.bean.FindCommunityList;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.UserInfoBean;

import java.util.List;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Tech
 * @date 2019/2/18 16:20
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public interface Interfacea {


    /**
     * 注册
     *
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
     *
     * @param phone
     * @param pwd
     * @return
     */
    @FormUrlEncoded
    @POST("user/v1/login")
    Observable<Result<LoginBean>> login(@Field("phone") String phone,
                                        @Field("pwd") String pwd);

    /**
     * banner展示列表
     *
     * @return
     */
    @GET("information/v1/bannerShow")
    Observable<Result<List<MyBanner>>> mybanner();

    /**
     * 所有板块查询
     *
     * @return
     */
    @GET("information/v1/findAllInfoPlate")
    Observable<Result<List<MenusBean>>> mymenus();

    Observable<Result> login();


    /**
     * 根据用户ID查询用户信息
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("user/verify/v1/getUserInfoByUserId")
    Observable<Result<UserInfoBean>> getUserInfoByUserId(@Header("userId") int userId,
                                                         @Header("sessionId") String sessionId);

    /**
     * 资讯推荐展示列表(包含单独板块列表展示)
     * @param userId
     * @param sessionId
     * @param plateId
     * @param page
     * @param count
     * @return
     */
    @GET("information/v1/infoRecommendList")
    Observable<Result<List<HomeAll>>> infoRecommendList(@Header("userId") int userId,
                                                        @Header("sessionId") String sessionId,
                                                        @Query("plateId") int plateId,
                                                        @Query("page") int page,
                                                        @Query("count") int count);


    /**
     * 社区列表展示
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("community/v1/findCommunityList")
    Observable<Result<List<FindCommunityList>>> findCommunityList(@Header("userId")int userId,
                                                                  @Header("sessionId")String sessionId,
                                                                  @Query("page")int page,
                                                                  @Query("count")int count);

}
