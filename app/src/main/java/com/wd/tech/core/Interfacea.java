package com.wd.tech.core;

import com.wd.tech.bean.CollectionBean;
import com.wd.tech.bean.FindFriendNoticePageList;
import com.wd.tech.bean.FindGroup;
import com.wd.tech.bean.FindGroupNoticePageList;
import com.wd.tech.bean.FindUser;
import com.wd.tech.bean.DetailsBean;
import com.wd.tech.bean.GroupByUser;
import com.wd.tech.bean.HomeAll;
import com.wd.tech.bean.InformationSearchByTitleBean;
import com.wd.tech.bean.InitFriendlist;
import com.wd.tech.bean.IntegralRecordBean;
import com.wd.tech.bean.LoginBean;
import com.wd.tech.bean.MenusBean;
import com.wd.tech.bean.MyBanner;
import com.wd.tech.bean.FindCommunityList;
import com.wd.tech.bean.MyLoveBean;
import com.wd.tech.bean.MyComment;
import com.wd.tech.bean.MyPostByIdBean;
import com.wd.tech.bean.MyTongzhiBean;
import com.wd.tech.bean.PayBean;
import com.wd.tech.bean.Result;
import com.wd.tech.bean.UserComment;
import com.wd.tech.bean.UserInfoBean;
import com.wd.tech.bean.UserPost;
import com.wd.tech.bean.UserTaskBean;
import com.wd.tech.bean.UserintegralBean;
import com.wd.tech.bean.VIPList;

import java.io.File;
import java.util.List;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    /**
     * 资讯详情展示
     * @param userId
     * @param sessionId
     * @param id
     * @return
     */
    @GET("information/v1/findInformationDetails")
    Observable<Result<DetailsBean>> findInformationDetails(@Header("userId")int userId,
                                                           @Header("sessionId")String sessionId,
                                                           @Query("id")int id);

    /**
     * 根据手机号查询用户信息
     * @param userId
     * @param sessionId
     * @param phone
     * @return
     */
    @GET("user/verify/v1/findUserByPhone")
    Observable<Result<FindUser>> findUserByPhone(@Header("userId")int userId,
                                                 @Header("sessionId")String sessionId,
                                                 @Query("phone")String phone);

    /**
     * 通过群Id查群
     *
     * @param userId
     * @param sessionId
     * @param groupId
     * @return
     */
    @GET("group/verify/v1/findGroupInfo")
    Observable<Result<FindGroup>> findGroupInfo(@Header("userId")int userId,
                                                @Header("sessionId")String sessionId,
                                                @Query("groupId")int groupId);

    /**
     * 发布帖子
     * @param userId
     * @param sessionId
     * @param body 传资源使用
     * @return
     */
    @POST("community/verify/v1/releasePost")
    @FormUrlEncoded
    Observable<Result> releasePost(@Header("userId")int userId,
                                   @Header("sessionId")String sessionId,
                                   @Body MultipartBody body);
    /*@Field("content")String content,
    @Field("file")File file*/

    /**
     * 帖子点赞
     * @param userId
     * @param sessionId
     * @param communityId
     * @return
     */
    @POST("community/verify/v1/addCommunityGreat")
    @FormUrlEncoded
    Observable<Result> addCommunityGreat(@Header("userId")int userId,
                                       @Header("sessionId")String sessionId,
                                       @Field("communityId")int communityId);

    /**
     * 社区取消点赞
     * @param userId
     * @param sessionId
     * @param communityId
     * @return
     */
    @DELETE("community/verify/v1/cancelCommunityGreat")
    Observable<Result> cancelCommunityGreat(@Header("userId")int userId,
                                            @Header("sessionId")String sessionId,
                                            @Query("communityId")int communityId);
    /**
     * 查询资讯评论列表
     *
     * @param userId
     * @param sessionId
     * @param infoId
     * @param page
     * @param count
     * @return
     */
    @GET("information/v1/findAllInfoCommentList")
    Observable<Result<List<MyComment>>> myComment(@Header("userId") int userId,
                                                  @Header("sessionId") String sessionId,
                                                  @Query("infoId") int infoId,
                                                  @Query("page") int page,
                                                  @Query("count") int count);

    /**
     * 资讯用户评论
     * @param userId
     * @param sessionId
     * @param content
     * @param infoId
     * @return
     */
    @FormUrlEncoded
    @POST("information/verify/v1/addInfoComment")
    Observable<Result<List<MyComment>>> addInfoComment(@Header("userId") int userId,
                                                       @Header("sessionId") String sessionId,
                                                       @Field("content") String content,
                                                       @Field("infoId") int infoId);


    /**
     * 查询分组
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("chat/verify/v1/initFriendList")
    Observable<Result<List<InitFriendlist>>> allFriendsList(@Header("userId") int userId,
                                                            @Header("sessionId") String sessionId);


    /**
     * 用户关注列表
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("user/verify/v1/findFollowUserList")
    Observable<Result<List<MyLoveBean>>> findFollowUserList(@Header("userId")int userId,
                                                            @Header("sessionId")String sessionId,
                                                            @Query("page")int page,
                                                            @Query("count")int count);



    /**
     * 查询我创建的群组
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("group/verify/v1/findGroupsByUserId")
    Observable<Result<List<GroupByUser>>> findGroupsByUserId(@Header("userId") int userId,
                                                             @Header("sessionId") String sessionId);

    /**
     * 创建群
     * @param userId
     * @param sessionId
     * @param name
     * @param description
     * @return
     */
    @POST("group/verify/v1/createGroup")
    @FormUrlEncoded
    Observable<Result> createGroup(@Header("userId") int userId,
                                   @Header("sessionId") String sessionId,
                                   @Field("name") String name,
                                   @Field("description") String description);


    /**
     * 取消关注
     * @param userId
     * @param sessionId
     * @param focusId
     * @return
     */
    @DELETE("user/verify/v1/cancelFollow")
    Observable<Result> cancelFollow(@Header("userId") int userId,
                                   @Header("sessionId") String sessionId,
                                   @Query("focusId")int focusId);

    /**
     * 社区评论
     * @param userId
     * @param sessionId
     * @param communityId
     * @param content
     * @return
     */
    @POST("community/verify/v1/addCommunityComment")
    @FormUrlEncoded
    Observable<Result> addCommunityComment(@Header("userId") int userId,
                                           @Header("sessionId") String sessionId,
                                           @Field("communityId")int communityId,
                                           @Field("content")String content);

    /**
     * 添加收藏
     *
     * @param userId
     * @param sessionId
     * @param infoId
     * @return
     */
    @FormUrlEncoded
    @POST("user/verify/v1/addCollection")
    Observable<Result> addCollection(@Header("userId") int userId,
                                     @Header("sessionId") String sessionId,
                                     @Field("infoId") int infoId);

    /**
     * 取消收藏（支持批量操作）
     *
     * @param userId
     * @param sessionId
     * @param infoId
     * @return
     */
    @DELETE("user/verify/v1/cancelCollection")
    Observable<Result> cancelCollection(@Header("userId") int userId,
                                        @Header("sessionId") String sessionId,
                                        @Query("infoId") String infoId);


    /**
     * 用户收藏列表
     *
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("user/verify/v1/findAllInfoCollection")
    Observable<Result<List<CollectionBean>>> findAllInfoCollection(@Header("userId") int userId,
                                                                   @Header("sessionId") String sessionId,
                                                                   @Query("page") int page,
                                                                   @Query("count") int count);


    /**
     * 检测是否为我的好友
     *
     * @param userId
     * @param sessionId
     * @param friendUid
     * @return
     */
    @GET("chat/verify/v1/checkMyFriend")
    Observable<Result> checkMyFriend(@Header("userId") int userId,
                                     @Header("sessionId") String sessionId,
                                     @Query("friendUid") int friendUid);

    /**
     * 判断用户是否已在群内
     *
     * @param userId
     * @param sessionId
     * @param groupId
     * @return
     */
    @GET("group/verify/v1/whetherInGroup")
    Observable<Result> whetherInGroup(@Header("userId") int userId,
                                      @Header("sessionId") String sessionId,
                                      @Query("groupId") int groupId);

    /**
     * 添加好友
     *
     * @param userId
     * @param sessionId
     * @param friendUid
     * @param remark
     * @return
     */

    @POST("chat/verify/v1/addFriend")
    @FormUrlEncoded
    Observable<Result> addFriend(@Header("userId") int userId,
                                 @Header("sessionId") String sessionId,
                                 @Field("friendUid") int friendUid,
                                 @Field("remark") String remark);

    /**
     * 资讯点赞
     *
     * @param userId
     * @param sessionId
     * @param infoId
     * @return
     */
    @FormUrlEncoded
    @POST("information/verify/v1/addGreatRecord")
    Observable<Result> addGreatRecord(@Header("userId") int userId,
                                      @Header("sessionId") String sessionId,
                                      @Field("infoId") int infoId);

    /**
     * 取消点赞
     *
     * @param userId
     * @param sessionId
     * @param infoId
     * @return
     */
    @DELETE("information/verify/v1/cancelGreat")
    Observable<Result> cancelGreat(@Header("userId") int userId,
                                   @Header("sessionId") String sessionId,
                                   @Query("infoId") int infoId);

    /**
     * 根据标题模糊查询
     *
     * @param title
     * @param page
     * @param count
     * @return
     */
    @GET("information/v1/findInformationByTitle")
    Observable<Result<List<InformationSearchByTitleBean>>> findInformationByTitle(@Query("title") String title,
                                                                                  @Query("page") int page,
                                                                                  @Query("count") int count);

    /**
     * 查询用户积分
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("user/verify/v1/findUserIntegral")
    Observable<Result<UserintegralBean>> findUserIntegral(@Header("userId") int userId,
                                                          @Header("sessionId")String sessionId);


    /**
     * 查询用户当月所有签到的日期
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("user/verify/v1/findContinuousSignDays")
    Observable<Result<Integer>> findContinuousSignDays(@Header("userId") int userId,
                                                       @Header("sessionId")String sessionId);


    /**
     * 查询用户积分明细
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("user/verify/v1/findUserIntegralRecord")
    Observable<Result<List<IntegralRecordBean>>> findUserIntegralRecord(@Header("userId") int userId,
                                                                        @Header("sessionId") String sessionId,
                                                                        @Query("page")int page,
                                                                        @Query("count")int count);


    /**
     * 查询用户任务列表
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("user/verify/v1/findUserTaskList")
    Observable<Result<List<UserTaskBean>>> findUserTaskList(@Header("userId") int userId,
                                                            @Header("sessionId") String sessionId);


    /**
     * 我的帖子
     * @param userId
     * @param sessionId
     * @param page
     * @param count
     * @return
     */
    @GET("community/verify/v1/findMyPostById")
    Observable<Result<List<MyPostByIdBean>>> findMyPostById(@Header("userId") int userId,
                                                            @Header("sessionId") String sessionId,
                                                            @Query("page")int page,
                                                            @Query("count")int count);


    /**
     * 查询用户系统通知
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("tool/verify/v1/findSysNoticeList")
    Observable<Result<List<MyTongzhiBean>>> findSysNoticeList(@Header("userId") int userId,
                                                              @Header("sessionId") String sessionId,
                                                              @Query("page")int page,
                                                              @Query("count")int count);

    /**
     * 社区用户评论列表（bean方式反参）
     * @param userId
     * @param sessionId
     * @param communityId
     * @param page
     * @param count
     * @return
     */
    @GET("community/v1/findCommunityUserCommentList")
    Observable<Result<List<UserComment>>> findCommunityUserCommentList(@Header("userId") int userId,
                                                                       @Header("sessionId") String sessionId,
                                                                       @Query("communityId")int communityId,
                                                                       @Query("page")int page,
                                                                       @Query("count")int count);

    /**
     * 查询用户发布的帖子
     * @param userId
     * @param sessionId
     * @param fromUid  用户id
     * @param page
     * @param count
     * @return
     */
    @GET("community/verify/v1/findUserPostById")
    Observable<Result<List<UserPost>>> findUserPostById(@Header("userId") int userId,
                                                        @Header("sessionId") String sessionId,
                                                        @Query("fromUid")int fromUid,
                                                        @Query("page")int page,
                                                        @Query("count")int count);


    /**
     * 删除帖子(支持批量删除)
     *
     * @param userId
     * @param sessionId
     * @param communityId
     * @return
     */
    @DELETE("community/verify/v1/deletePost")
    Observable<Result> deletePost(@Header("userId") int userId,
                                  @Header("sessionId") String sessionId,
                                  @Query("communityId") String communityId);


    /**
     * 查询当天签到状态
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("user/verify/v1/findUserSignStatus")
    Observable<Result> findUserSignStatus(@Header("userId") int userId,
                                          @Header("sessionId") String sessionId);


    /**
     * 查询用户当月所有签到的日期
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @GET("user/verify/v1/findUserSignRecording")
    Observable<Result> findUserSignRecording(@Header("userId") int userId,
                                             @Header("sessionId") String sessionId);


    /**
     * 签到
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @POST("user/verify/v1/userSign")
    Observable<Result> userSign(@Header("userId") int userId,
                                @Header("sessionId") String sessionId);


    /**
     * 申请进群
     *
     * @param userId
     * @param sessionId
     * @param groupId
     * @param remark
     * @return
     */
    @POST("group/verify/v1/applyAddGroup")
    @FormUrlEncoded
    Observable<Result> applyAddGroup(@Header("userId") int userId,
                                     @Header("sessionId") String sessionId,
                                     @Field("groupId") int groupId,
                                     @Field("remark") String remark);

    /**
     * https://172.17.8.100/techApi/chat/verify/v1/findFriendNoticePageList
     * 查询新朋友的界面
     */
    @GET("chat/verify/v1/findFriendNoticePageList")
    Observable<Result<List<FindFriendNoticePageList>>> findFriendNoticePageList(@Header("userId") int userId,
                                                                                @Header("sessionId") String sessionId,
                                                                                @Query("page") int page, @Query("count") int count);

    /**
     * https://172.17.8.100/techApi/group/verify/v1/findGroupNoticePageList
     * 查询群聊界面findgroupnoticepagelist
     */
    @GET("group/verify/v1/findGroupNoticePageList")
    Observable<Result<List<FindGroupNoticePageList>>> findGroupNoticePageList(@Header("userId") int userId,
                                                                              @Header("sessionId") String sessionId,
                                                                              @Query("page") int page, @Query("count") int count);

    /**
     * 审核好友申请
     *
     * @param userId
     * @param sessionId
     * @param noticeId
     * @param flag
     * @return
     */
    @PUT("chat/verify/v1/reviewFriendApply")
    @FormUrlEncoded
    Observable<Result> reviewFriendApply(@Header("userId") int userId,
                                         @Header("sessionId") String sessionId,
                                         @Field("noticeId") int noticeId,
                                         @Field("flag") int flag);

    /**
     * 审核群申请
     *
     * @param userId
     * @param sessionId
     * @param noticeId
     * @param flag
     * @return
     */
    @PUT("group/verify/v1/reviewGroupApply")
    @FormUrlEncoded
    Observable<Result> reviewGroupApply(@Header("userId") int userId,
                                        @Header("sessionId") String sessionId,
                                        @Field("noticeId") int noticeId,
                                        @Field("flag") int flag);


    /**
     * 用户购买VIP
     *
     * @param userId
     * @param sessionId
     * @param commodityId
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("tool/verify/v1/buyVip")
    Observable<Result> buyVip(@Header("userId") int userId,
                              @Header("sessionId") String sessionId,
                              @Field("commodityId") int commodityId,
                              @Field("sign") String sign);

    /**
     * 资讯积分兑换
     *
     * @param userId
     * @param sessionId
     * @param infoId
     * @param integralCost
     * @return
     */
    @FormUrlEncoded
    @POST("information/verify/v1/infoPayByIntegral")
    Observable<Result> infoPayByIntegral(@Header("userId") int userId,
                                         @Header("sessionId") String sessionId,
                                         @Field("infoId") int infoId,
                                         @Field("integralCost") int integralCost);

    /**
     * 上传头像
     *
     * @param userId
     * @param sessionId
     * @param image
     * @return
     */
    @POST("user/verify/v1/modifyHeadPic")
    Observable<Result> modifyHeadPic(@Header("userId") int userId,
                                     @Header("sessionId") String sessionId,
                                     @Body MultipartBody image);


    /**
     * 修改邮箱
     *
     * @param userId
     * @param sessionId
     * @param email
     * @return
     */
    @FormUrlEncoded
    @PUT("user/verify/v1/modifyEmail")
    Observable<Result> modifyEmail(@Header("userId") int userId,
                                   @Header("sessionId") String sessionId,
                                   @Field("email") String email);

    /**
     * 修改用户密码
     *
     * @param userId
     * @param sessionId
     * @return
     */
    @FormUrlEncoded
    @PUT("user/verify/v1/modifyUserPwd")
    Observable<Result> modifyUserPwd(@Header("userId") int userId,
                                     @Header("sessionId") String sessionId,
                                     @Field("oldPwd") String oldPwd,
                                     @Field("newPwd") String newPwd);


    /**
     * 做任务
     *
     * @param userId
     * @param sessionId
     * @param taskId
     * @return
     */
    @FormUrlEncoded
    @POST("user/verify/v1/doTheTask")
    Observable<Result> doTheTask(@Header("userId") int userId,
                                 @Header("sessionId") String sessionId,
                                 @Field("taskId") int taskId);

    /**
     * 完善用户信息
     *
     * @param userId
     * @param sessionId
     * @param nickName
     * @param sex
     * @param signature
     * @param birthday
     * @param email
     * @return
     */
    @FormUrlEncoded
    @POST("user/verify/v1/perfectUserInfo")
    Observable<Result> perfectUserInfo(@Header("userId") int userId,
                                       @Header("sessionId") String sessionId,
                                       @Field("nickName") String nickName,
                                       @Field("sex") int sex,
                                       @Field("signature") String signature,
                                       @Field("birthday") String birthday,
                                       @Field("email") String email);

    /**
     * 支付
     *
     * @param userId
     * @param sessionId
     * @param orderId
     * @param payType
     * @return
     */
    @FormUrlEncoded
    @POST("tool/verify/v1/pay")
    Observable<Result<String>> pay(@Header("userId") int userId,
                                   @Header("sessionId") String sessionId,
                                   @Field("orderId") String orderId,
                                   @Field("payType") int payType);

    /**
     * 微信分享前置接口，获取分享所需参数
     *
     * @param time
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("tool/v1/wxShare")
    Observable<Result> wxShare(@Field("time") String time,
                               @Field("String") String sign);

    @GET("tool/v1/findVipCommodityList")
    Observable<Result<List<VIPList>>> findVipCommodityList();


    /**
     * 删除好友
     *
     * @param userId
     * @param sessionId
     * @param friendUid
     * @return
     */
    @DELETE("chat/verify/v1/deleteFriendRelation")
    Observable<Result> deleteFriendRelation(@Header("userId") int userId,
                                            @Header("sessionId") String sessionId,
                                            @Query("friendUid") int friendUid);

    /**
     * 转移好友到其他分组
     *
     * @param userId
     * @param sessionId
     * @param newGroupId
     * @param friendUid
     * @return
     */
    @PUT("chat/verify/v1/transferFriendGroup")
    @FormUrlEncoded
    Observable<Result> transferFriendGroup(@Header("userId") int userId,
                                           @Header("sessionId") String sessionId,
                                           @Field("newGroupId") int newGroupId,
                                           @Field("friendUid") int friendUid);



    @POST("user/v1/weChatLogin")
    @FormUrlEncoded
    Observable<Result<LoginBean>> weChatLogin(@Header("ak")String ak,
                                              @Field("code")String code);

}
