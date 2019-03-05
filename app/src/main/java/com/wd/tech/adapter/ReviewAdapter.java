package com.wd.tech.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.bean.CommunityItem;
import com.wd.tech.bean.UserComment;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:<br>
 * Author:GJ<br>
 * Date:2019/2/26 9:37
 */
class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyHolder> {

    ArrayList<CommunityItem> mItemList = new ArrayList();

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(),R.layout.layout_review_show_item,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        CommunityItem communityItem = mItemList.get(i);
        myHolder.review_name.setText(communityItem.getNickName());//昵称
        myHolder.review_string.setText(communityItem.getContent());//评论内容
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void addAll(List<CommunityItem> communityCommentVoList) {
        if(communityCommentVoList!=null){
            mItemList.addAll(communityCommentVoList);
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final TextView review_name;
        private final TextView review_string;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            review_name = itemView.findViewById(R.id.review_item_name);
            review_string = itemView.findViewById(R.id.review_item_string);
        }
    }
}
