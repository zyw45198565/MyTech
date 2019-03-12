package com.wd.tech.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.CommunityItem;
import com.wd.tech.bean.FindCommunityList;
import com.wd.tech.utils.util.DateUtils1;
import com.wd.tech.utils.util.SpacingItemDecoration;
import com.wd.tech.utils.util.StringUtils;
import com.wd.tech.utils.util.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description:社区列表展示adapter
 * Author:GJ<br>
 * Date:2019/2/19 12:01
 */
public class CommunityListAdapter extends RecyclerView.Adapter<CommunityListAdapter.MyHolder> {

    Context context;
    private ClickOkListener clickOkListener;
    private TalkBack myTalkBack;
    private ClickTextStart clickTextStart;
    private ClickHeadLinstener clickHeadLinstener;

    public CommunityListAdapter(Context context) {
        this.context = context;
    }

    ArrayList<FindCommunityList> mList = new ArrayList<>();

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_findcommunitylist, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
        final FindCommunityList findCommunityList = mList.get(i);
        final String headPic = findCommunityList.getHeadPic();//头像
        myHolder.head.setImageURI(headPic);
        final String nickName = findCommunityList.getNickName();//昵称
        myHolder.nick.setText(nickName);
        try {
            myHolder.time.setText(DateUtils1.dateTransformer(Long.parseLong(findCommunityList.getPublishTime() + ""), DateUtils1.DATE_PATTERN));//显示时间
        } catch (Exception e) {
            e.printStackTrace();
        }

        myHolder.signature.setText(findCommunityList.getSignature());
        if(findCommunityList.getContent().equals("")){
            myHolder.title.setVisibility(View.GONE);
        }else{
            myHolder.title.setVisibility(View.VISIBLE);
            myHolder.title.setText(findCommunityList.getContent());
        }

        final int comment = findCommunityList.getComment();//评论数量
        myHolder.commentText.setText( comment+ "");
        myHolder.likeText.setText(findCommunityList.getPraise() + "");

        //图片判断
        if (StringUtils.isEmpty(findCommunityList.getFile())) {//图片为空
            myHolder.picterRlv.setVisibility(View.GONE);//没有图片设为空
        } else {
            myHolder.picterRlv.setVisibility(View.VISIBLE);//没有图片设为空
            String[] images = findCommunityList.getFile().split(",");
            int colNum;//列数
            if (images.length == 1) {
                colNum = 1;
            } else if (images.length == 2 || images.length == 4) {
                colNum = 2;
            } else {
                colNum = 3;
            }
            myHolder.gridLayoutManager.setSpanCount(colNum);//设置列数

            myHolder.imageAdapter.clear();//清空图片适配器集合
            myHolder.imageAdapter.addAll(Arrays.asList(images));
            myHolder.imageAdapter.notifyDataSetChanged();
        }

        final int whetherGreat = findCommunityList.getWhetherGreat();//点赞状态
        if(whetherGreat==1){
            myHolder.communitylistLike.setImageResource(R.drawable.community_icon_like_true);
        }else{
            myHolder.communitylistLike.setImageResource(R.drawable.community_icon_like_false);
        }
        myHolder.communitylistRightLinear.setOnClickListener(new View.OnClickListener() {//点赞
            @Override
            public void onClick(View v) {
                if(whetherGreat==2){
                    findCommunityList.setWhetherGreat(1);//设置点赞状态为选中
                }else{
                    findCommunityList.setWhetherGreat(2);//设置点赞状态为不选中
                }
                if(whetherGreat!=1){//如果不选中，设置点赞数量+1
                    findCommunityList.setPraise(findCommunityList.getPraise()+1);
                    myHolder.communitylistLike.setImageResource(R.drawable.community_icon_like_true);
                }else{
                    findCommunityList.setPraise(findCommunityList.getPraise()-1);
                    myHolder.communitylistLike.setImageResource(R.drawable.community_icon_like_false);
                }
                clickOkListener.clickOk(findCommunityList.getId(),whetherGreat);
                notifyDataSetChanged();
            }
        });

        myHolder.communitylistLeftLinear.setOnClickListener(new View.OnClickListener() {//点击评论
            @Override
            public void onClick(View v) {
                myTalkBack.talkBacks(findCommunityList.getId());
            }
        });

        ReviewAdapter reviewAdapter = new ReviewAdapter();//社区评论展示
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        myHolder.reviewShow.setLayoutManager(linearLayoutManager);
        myHolder.reviewShow.setAdapter(reviewAdapter);
        List<CommunityItem> communityCommentVoList = findCommunityList.getCommunityCommentVoList();


        if(comment==0){//设置底部文字
            myHolder.reviewText.setText("快来评论呀~");
            myHolder.reviewText.setTextColor(Color.parseColor("#7a7a7a"));
            myHolder.reviewShow.setVisibility(View.GONE);//列表隐藏
        }else if(comment<=3){
            myHolder.reviewShow.setVisibility(View.VISIBLE);//列表隐藏
            reviewAdapter.addAll(communityCommentVoList);
            reviewAdapter.notifyDataSetChanged();
            myHolder.reviewText.setText("没有更多评论了~");
            myHolder.reviewText.setTextColor(Color.parseColor("#7a7a7a"));
        }else{
            myHolder.reviewShow.setVisibility(View.VISIBLE);//列表隐藏
            reviewAdapter.addAll(communityCommentVoList);
            reviewAdapter.notifyDataSetChanged();
            myHolder.reviewText.setText("点击查看更多评论");
            myHolder.reviewText.setTextColor(Color.parseColor("#50bef8"));
        }

        myHolder.reviewText.setOnClickListener(new View.OnClickListener() {//点击评论文字跳转页面
            @Override
            public void onClick(View v) {
                if(comment==0){
                    UIUtils.showToastSafe("暂时没有评论哦，快去评论吧~");
                    return;
                }
                clickTextStart.clickShow(findCommunityList.getId(),comment,headPic,nickName);
            }
        });

        myHolder.head.setOnClickListener(new View.OnClickListener() {//getUserId 发布人id getId社区id
            @Override
            public void onClick(View v) {
                clickHeadLinstener.clickHead(findCommunityList.getUserId(),
                        headPic,nickName,findCommunityList.getSignature());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void remove() {//移除所有数据
        mList.clear();
    }

    public void addList(FindCommunityList findCommunityList) {//添加数据
        if (findCommunityList != null) {
            mList.add(findCommunityList);
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final SimpleDraweeView head;
        private final TextView nick;
        private final TextView time;
        private final TextView signature;
        private final TextView title;
        private final RecyclerView picterRlv;//图片列表
        private final LinearLayout communitylistLeftLinear;//点击弹框
        private final LinearLayout communitylistRightLinear;//点击点赞
        private final RecyclerView reviewShow;//评论列表 有就展示 没有隐藏
        private final TextView reviewText;//点击跳转,如果评论有数量超过3条点击查看更多评论,不超过是没有更多评论了~
        private final TextView commentText;
        private final TextView likeText;
        private final ImageAdapter imageAdapter;
        private final GridLayoutManager gridLayoutManager;
        private final SimpleDraweeView communitylistLike;//点赞图片

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.communitylist_head_img);
            nick = itemView.findViewById(R.id.communitylist_nick);
            time = itemView.findViewById(R.id.communitylist_time);
            signature = itemView.findViewById(R.id.communitylist_signature);
            title = itemView.findViewById(R.id.communitylist_title);
            picterRlv = itemView.findViewById(R.id.communitylist_picter_rlv);
            communitylistLeftLinear = itemView.findViewById(R.id.communitylist_layout_left);
            commentText = itemView.findViewById(R.id.communitylist_comment_text);//评论数量
            communitylistRightLinear = itemView.findViewById(R.id.communitylist_layout_right);
            likeText = itemView.findViewById(R.id.communitylist_like_text);//点赞数量
            communitylistLike = itemView.findViewById(R.id.communitylist_like);
            reviewShow = itemView.findViewById(R.id.communitylist_review_show);
            reviewText = itemView.findViewById(R.id.communitylist_review_text);

            imageAdapter = new ImageAdapter();//社区图片展示
            int space = context.getResources().getDimensionPixelSize(R.dimen.dip_8);//图片间距
            gridLayoutManager = new GridLayoutManager(context, 3);
            picterRlv.addItemDecoration(new SpacingItemDecoration(space));//添加视图的间距
            picterRlv.setLayoutManager(gridLayoutManager);
            picterRlv.setAdapter(imageAdapter);
        }
    }
    
    public interface ClickOkListener{//点赞回调到页面的自定义接口/评论
        void clickOk(int id,int greatStyle);
    }
    public void setClickOkListener(ClickOkListener clickOkListener){
        this.clickOkListener = clickOkListener;
    }
     public interface TalkBack{//评论的自定义接口回调
        void talkBacks(int id);
    }
    public void setMyTalkBack(TalkBack myTalkBack){
        this.myTalkBack = myTalkBack;
    }

    public interface ClickTextStart{
        void clickShow(int id,int num,String headPic,String nickName);
    }
    public void setClickTextStart(ClickTextStart clickTextStart){
        this.clickTextStart = clickTextStart;
    }

    public interface ClickHeadLinstener{//点击头像的自定义接口
        void clickHead(int userId,String head,String nick,String text);
    }

    public void setClickHeadLinstener(ClickHeadLinstener clickHeadLinstener){
        this.clickHeadLinstener = clickHeadLinstener;
    }
}
