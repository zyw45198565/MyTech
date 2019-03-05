package com.wd.tech.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.UserComment;
import com.wd.tech.utils.util.DateUtils1;

import java.util.ArrayList;

/**
 * Description:<br>
 * Author:GJ<br>
 * Date:2019/2/26 19:15
 */
public class CommentShowItemAdapter extends RecyclerView.Adapter<CommentShowItemAdapter.MyHolder> {
    ArrayList<UserComment> mList = new ArrayList<>();
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =View.inflate(viewGroup.getContext(),R.layout.layout_show_item,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        UserComment userComment = mList.get(i);
        myHolder.itemHead.setImageURI(userComment.getHeadPic());
        myHolder.itemNick.setText(userComment.getNickName());
        try {
            myHolder.itemTime.setText(DateUtils1.dateTransformer(Long.parseLong(userComment.getCommentTime() + ""), DateUtils1.DATE_PATTERN));//显示时间
        } catch (Exception e) {
            e.printStackTrace();
        }
        myHolder.itemContent.setText(userComment.getContent());//评论
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addList(UserComment userComment) {
        if(userComment!=null){
            mList.add(userComment);
        }
    }

    public void remove() {
        mList.clear();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final SimpleDraweeView itemHead;
        private final TextView itemNick;
        private final TextView itemTime;
        private final TextView itemContent;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            itemHead = itemView.findViewById(R.id.item_head);
            itemNick = itemView.findViewById(R.id.item_nick);
            itemTime = itemView.findViewById(R.id.item_time);
            itemContent = itemView.findViewById(R.id.item_content);
        }
    }
}
