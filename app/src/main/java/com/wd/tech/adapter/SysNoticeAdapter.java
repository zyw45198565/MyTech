package com.wd.tech.adapter;

import android.content.Context;
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
import com.wd.tech.bean.MyTongzhiBean;
import com.wd.tech.view.SideslipView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author happy_movie
 * @date 2019/1/23 19:42
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class SysNoticeAdapter extends RecyclerView.Adapter<SysNoticeAdapter.MyViewHolder>  {
    private List<MyTongzhiBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public SysNoticeAdapter(Context context, List<MyTongzhiBean> datas){
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
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date1 = new Date(mDatas.get(position).getCreateTime());
        holder.date.setText(sf.format(date1)+"");
        holder.count.setText(""+mDatas.get(position).getContent());
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.syssnotice_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder { //承载Item视图的子布局
        TextView date;
        TextView  count;

        public MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            count = view.findViewById(R.id.count);
        }
    }


}