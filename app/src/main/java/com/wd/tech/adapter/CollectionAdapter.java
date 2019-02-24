package com.wd.tech.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.CollectionBean;
import com.wd.tech.bean.MyLoveBean;
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
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder>  {
    private List<CollectionBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    Shan shan;

    public CollectionAdapter(Context context, List<CollectionBean> datas){
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

        holder.tu.setImageURI(mDatas.get(position).getThumbnail());
        holder.tit.setText(mDatas.get(position).getTitle()+"");


        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date1 = new Date(mDatas.get(position).getCreateTime());
        String format = sf.format(date1);
        holder.count.setText(format);
        boolean ischeck = mDatas.get(position).isIscheck();
        if(ischeck){
            holder.cb.setVisibility(View.VISIBLE);
        }else {
            holder.cb.setVisibility(View.GONE);
        }
        holder.cb.setChecked(mDatas.get(position).isIscheck2());
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                boolean checked = checkBox.isChecked();
                    mDatas.get(position).setIscheck2(checked);
            }
        });
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.collection_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder { //承载Item视图的子布局
        SimpleDraweeView tu;
        TextView tit;
        TextView count;
        CheckBox cb;

        public MyViewHolder(View view) {
            super(view);
             tu = view.findViewById(R.id.tu);
            tit  = view.findViewById(R.id.tit);
            count = view.findViewById(R.id.count);
            cb = view.findViewById(R.id.cb);
        }
    }

    public interface Shan{
        void onshan(int i);
    }


}