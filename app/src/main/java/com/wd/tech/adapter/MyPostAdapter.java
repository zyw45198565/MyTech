package com.wd.tech.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.wd.tech.R;
import com.wd.tech.bean.MyLoveBean;
import com.wd.tech.bean.MyPostByIdBean;
import com.wd.tech.utils.util.DateUtils;
import com.wd.tech.utils.util.DateUtils1;
import com.wd.tech.view.FolderTextView;
import com.wd.tech.view.SideslipView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author happy_movie
 * @date 2019/1/23 19:42
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyViewHolder>  {
    private List<MyPostByIdBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public MyPostAdapter(Context context, List<MyPostByIdBean> datas){
        this.mContext=context;
        this.mDatas=datas;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.content1.setText(mDatas.get(position).getContent()+"");
        holder.content1.setTailColor(0xff20affa);
        holder.zansum.setText(""+mDatas.get(position).getPraise());
        holder.pingsum.setText(""+mDatas.get(position).getComment());
        holder.recycler.setLayoutManager(new GridLayoutManager(mContext,3));
        String file = mDatas.get(position).getFile();
        if(file.equals("")){
            holder.recycler.setVisibility(View.GONE);
        }else {
            holder.recycler.setVisibility(View.VISIBLE);
        }
        String[] split = file.split(",");
        holder.recycler.setAdapter(new MyPostAdapter2(mContext,Arrays.asList(split)));

        try {
            String s = DateUtils1.dateTransformer(mDatas.get(position).getPublishTime(), DateUtils1.DATE_TIME_PATTERN);//显示时间
            holder.date.setText(""+s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mypost_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder { //承载Item视图的子布局
        FolderTextView content1;
        TextView zansum;
        TextView pingsum;
        TextView  date;
        RecyclerView recycler;

        public MyViewHolder(View view) {
            super(view);
            content1 = view.findViewById(R.id.content1);
            zansum = view.findViewById(R.id.zansum);
            pingsum = view.findViewById(R.id.pingsum);
            date = view.findViewById(R.id.date);
            recycler = view.findViewById(R.id.recycler);
        }
    }

}