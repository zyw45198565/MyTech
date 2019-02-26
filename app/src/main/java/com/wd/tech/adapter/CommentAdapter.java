package com.wd.tech.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.DetailsBean;
import com.wd.tech.bean.MyComment;
import com.wd.tech.utils.util.DateUtils;
import com.wd.tech.utils.util.DateUtils1;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Myholder> {
    Context context;
    List<MyComment> list=new ArrayList<>();

    public CommentAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, viewGroup, false);
        Myholder myholder=new Myholder(view);
        return myholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder myholder, int i) {
//        myholder.title.setText(list.get(i).getTitle());
//        myholder.thumbnail.setImageURI(list.get(i).getThumbnail());
        myholder.head.setImageURI(list.get(i).getHeadPic());
        myholder.name.setText(list.get(i).getNickName());
        myholder.title.setText(list.get(i).getContent());
        /*try {
            myholder.time.setText(DateUtils.dateFormat(new Date(list.get(i).getCommentTime()),DateUtils.MINUTE_PATTERN));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        try {
            myholder.time.setText(DateUtils1.dateTransformer(Long.parseLong(list.get(i).getCommentTime() + ""), DateUtils1.DATE_PATTERN));//显示时间
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<MyComment> myComments) {
        if (myComments!=null){
            list.addAll(myComments);
        }
    }

    public void clear() {
        list.clear();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView title,name,time;
        SimpleDraweeView head;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.comment_name);
            time = itemView.findViewById(R.id.comment_time);
            title = itemView.findViewById(R.id.comment_title);
            head = itemView.findViewById(R.id.comment_head);
        }
    }
}

