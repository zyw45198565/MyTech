package com.wd.tech.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.UserPost;
import com.wd.tech.utils.util.SpacingItemDecoration;
import com.wd.tech.utils.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description:<br>
 * Author:GJ<br>
 * Date:2019/2/27 16:29
 */
public class UserPostAdatper extends RecyclerView.Adapter<UserPostAdatper.MyHolder> {

    Context context;
    ArrayList<UserPost.CommunityUserPostVoListBean> mList = new ArrayList<>();
    private ClickComment clickComment;
    private UserPost.CommunityUserPostVoListBean userPost;
    private int whetherGreat;
    private ClickLike clickLike;
    private LayoutInflater inflater;

    public UserPostAdatper(Context context) {
        this.context = context;
        inflater=LayoutInflater.from(context);//注意
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.layout_user_post_item,viewGroup,false);//初始化
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {
        userPost = mList.get(i);
        String content = userPost.getContent();//title
        if(content.equals("")){
            myHolder.userPostContent.setVisibility(View.GONE);
        }else{
            myHolder.userPostContent.setVisibility(View.VISIBLE);
            myHolder.userPostContent.setText(content);
        }

        int praise = userPost.getPraise();//点赞数量
        myHolder.userPostLikeText.setText( praise+ "");
        int comment = userPost.getComment();//评论数量
        myHolder.userPostCommentText.setText( comment+ "");

        String file = userPost.getFile();//图片判断
        if (StringUtils.isEmpty(file)) {//图片为空
            myHolder.userPostImg.setVisibility(View.GONE);//没有图片设为空
        } else {
            myHolder.userPostImg.setVisibility(View.VISIBLE);//没有图片设为空
            String[] images = file.split(",");
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


        whetherGreat = userPost.getWhetherGreat();//点赞状态
        if(whetherGreat ==1){
            myHolder.userPostLike.setChecked(true);
        }else{
            myHolder.userPostLike.setChecked(false);
        }

        myHolder.userPostLike.setOnClickListener(new View.OnClickListener() {//点赞
            @Override
            public void onClick(View v) {
                CheckBox checkBox= (CheckBox) v;
                boolean checked = checkBox.isChecked();
                if (checked){
                    userPost.setPraise(userPost.getPraise()+1);
//                    userPost.setWhetherGreat(1);//设置点赞状态为选中

                    clickLike.clickLikeChange(userPost.getId());

                }else{
//                    userPost.setWhetherGreat(2);

                    clickLike.clickLikefail(userPost.getId());
                    userPost.setPraise(userPost.getPraise()-1);

                }
               /* if(whetherGreat==2){
                    userPost.setWhetherGreat(1);//设置点赞状态为选中
                    userPost.setPraise(userPost.getPraise()+1);
                    myHolder.userPostLike.setChecked(true);
                    clickLike.clickLikeChange(userPost.getId());

                }else {
                    userPost.setWhetherGreat(2);
                    userPost.setPraise(userPost.getPraise()-1);
                    myHolder.userPostLike.setChecked(false);
                    clickLike.clickLikefail(userPost.getId());
                }*/
//                notifyDataSetChanged();
            }
        });

        myHolder.userPostLayoutLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickComment.clickStart(userPost.getId(),userPost.getComment());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void remove() {
        mList.clear();
    }

    public void addList(List<UserPost.CommunityUserPostVoListBean> userPost) {//添加数据
        if(userPost!=null){
            mList.addAll(userPost);
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final TextView userPostContent;
        private final RecyclerView userPostImg;
        private final LinearLayout userPostLayoutLeft;
        private final CheckBox userPostLike;
        private final TextView userPostLikeText;
        private final ImageAdapterTwo imageAdapter;
        private final GridLayoutManager gridLayoutManager;
        private final TextView userPostCommentText;
        private final LinearLayout userPostLayoutRight;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            userPostContent = itemView.findViewById(R.id.user_post_item_content);
            userPostImg = itemView.findViewById(R.id.user_post_item_img);
            userPostLayoutLeft = itemView.findViewById(R.id.user_post_item_layout_left);
            userPostCommentText = itemView.findViewById(R.id.user_post_item_comment_text);
            userPostLayoutRight = itemView.findViewById(R.id.user_post_item_layout_right);
            userPostLike = itemView.findViewById(R.id.user_post_item_like);
            userPostLikeText = itemView.findViewById(R.id.user_post_item_like_text);

            imageAdapter = new ImageAdapterTwo();//社区图片展示
            int space = context.getResources().getDimensionPixelSize(R.dimen.dip_6);//图片间距
            gridLayoutManager = new GridLayoutManager(context, 3);
            userPostImg.addItemDecoration(new SpacingItemDecoration(space));//添加视图的间距
            userPostImg.setLayoutManager(gridLayoutManager);
            userPostImg.setAdapter(imageAdapter);
        }
    }

    public interface ClickComment{//评论的自定义接口回调
        void clickStart(int id,int number);
    }
    public void setClickComment(ClickComment clickComment){
        this.clickComment = clickComment;
    }

    public interface ClickLike{//点赞
        void clickLikeChange(int id);
        void clickLikefail(int id);
    }
    public void setClickLike(ClickLike clickLike){
        this.clickLike = clickLike;
    }
}
