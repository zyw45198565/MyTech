package com.wd.tech.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.wd.tech.R;
import com.wd.tech.activity.HomeActivity;
import com.wd.tech.bean.MyLoveBean;
import com.wd.tech.view.SideslipView;

import java.util.List;

/**
 * @author happy_movie
 * @date 2019/1/23 19:42
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class LoveAdapter extends RecyclerView.Adapter<LoveAdapter.MyViewHolder>  {
    private List<MyLoveBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    Shan shan;

    public LoveAdapter(Context context, List<MyLoveBean> datas){
        this.mContext=context;
        this.mDatas=datas;
        inflater=LayoutInflater.from(mContext);
    }

    public void getShan(Shan shan){
        this.shan=shan;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Glide.with(mContext).load(mDatas.get(position).getHeadPic())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.head);
        holder.name.setText(""+mDatas.get(position).getNickName());
        holder.sideslipView.close();
        holder.shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shan.onshan(position);
            }
        });
        holder.ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "111111", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mylove_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder { //承载Item视图的子布局
        ImageView  head;
        TextView  name;
        TextView qian;
        TextView shanchu;
        SideslipView sideslipView;
        LinearLayout ll2;

        public MyViewHolder(View view) {
            super(view);
            head = view.findViewById(R.id.head);
           name = view.findViewById(R.id.name);
           qian = view.findViewById(R.id.qian);
           shanchu = view.findViewById(R.id.shanchu);
            sideslipView = view.findViewById(R.id.side);
            ll2 = view.findViewById(R.id.ll2);
        }
    }

    public interface Shan{
        void onshan(int i);
    }


}