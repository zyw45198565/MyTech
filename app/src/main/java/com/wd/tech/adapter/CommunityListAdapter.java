package com.wd.tech.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.FindCommunityList;
import com.wd.tech.utils.util.DateUtils1;
import com.wd.tech.utils.util.SpacingItemDecoration;
import com.wd.tech.utils.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Description:社区列表展示adapter
 * Author:GJ<br>
 * Date:2019/2/19 12:01
 */
public class CommunityListAdapter extends RecyclerView.Adapter<CommunityListAdapter.MyHolder> {

    Context context;
    public CommunityListAdapter(Context context) {
        this.context = context;
    }

    ArrayList<FindCommunityList> list = new ArrayList<>();

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(),R.layout.item_findcommunitylist,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        FindCommunityList findCommunityList = list.get(i);
        myHolder.head.setImageURI(findCommunityList.getHeadPic());
        myHolder.nick.setText(findCommunityList.getNickName());
        try {
            myHolder.time.setText(DateUtils1.dateTransformer(Long.parseLong(findCommunityList.getPublishTime()+""),DateUtils1.DATE_PATTERN));//显示时间
        } catch (Exception e) {
            e.printStackTrace();
        }
        myHolder.signature.setText(findCommunityList.getSignature());
        myHolder.title.setText(findCommunityList.getContent());
        myHolder.comment_text.setText(findCommunityList.getComment());
        myHolder.like_text.setText(findCommunityList.getPraise());

        //图片判断
        if(StringUtils.isEmpty(findCommunityList.getFile())){//图片为空
            myHolder.picter_rlv.setVisibility(View.GONE);//没有图片设为空
        }else {
            myHolder.picter_rlv.setVisibility(View.VISIBLE);//没有图片设为空
            String[] images = findCommunityList.getFile().split(",");
            int colNum;//列数
            if(images.length==1){
                colNum = 1;
            }else if(images.length==2||images.length == 4){
                colNum = 2;
            }else{
                colNum = 3;
            }
            myHolder.gridLayoutManager.setSpanCount(colNum);//设置列数

            myHolder.imageAdapter.clear();//清空图片适配器集合
            myHolder.imageAdapter.addAll(Arrays.asList(images));
            myHolder.imageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void remove() {
        list.clear();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final SimpleDraweeView head;
        private final TextView nick;
        private final TextView time;
        private final TextView signature;
        private final TextView title;
        private final RecyclerView picter_rlv;//图片列表
        private final LinearLayout communitylist_left_linear;//点击弹框
        private final LinearLayout communitylist_right_linear;//点击点赞
        private final RecyclerView review_show;//评论列表 有就展示 没有隐藏
        private final TextView review_text;//点击跳转,如果评论有数量超过3条点击查看更多评论,不超过是没有更多评论了~
        private final TextView comment_text;
        private final TextView like_text;
        private final ImageAdapter imageAdapter;
        private final GridLayoutManager gridLayoutManager;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.communitylist_head_img);
            nick = itemView.findViewById(R.id.communitylist_nick);
            time = itemView.findViewById(R.id.communitylist_time);
            signature = itemView.findViewById(R.id.communitylist_signature);
            title = itemView.findViewById(R.id.communitylist_title);
            picter_rlv = itemView.findViewById(R.id.communitylist_picter_rlv);
            communitylist_left_linear = itemView.findViewById(R.id.communitylist_layout_left);
            comment_text = itemView.findViewById(R.id.communitylist_comment_text);//评论数量
            communitylist_right_linear = itemView.findViewById(R.id.communitylist_layout_right);
            like_text = itemView.findViewById(R.id.communitylist_like_text);//点赞数量
            review_show = itemView.findViewById(R.id.communitylist_review_show);
            review_text = itemView.findViewById(R.id.communitylist_review_text);

            imageAdapter = new ImageAdapter();//社区图片展示
            int space = context.getResources().getDimensionPixelSize(R.dimen.dip_10);;//图片间距
            gridLayoutManager = new GridLayoutManager(context,3);
            picter_rlv.addItemDecoration(new SpacingItemDecoration(space));//添加视图的间距
            picter_rlv.setLayoutManager(gridLayoutManager);
            picter_rlv.setAdapter(imageAdapter);
        }
    }
}
