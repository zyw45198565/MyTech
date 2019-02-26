package com.wd.tech.adapter.publish;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.CollectionBean;
import com.wd.tech.bean.IntegralRecordBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author happy_movie
 * @date 2019/1/23 19:42
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class IntegralRecordAdapter extends RecyclerView.Adapter<IntegralRecordAdapter.MyViewHolder>  {
    private List<IntegralRecordBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    Shan shan;

    public IntegralRecordAdapter(Context context, List<IntegralRecordBean> datas){
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


        if(mDatas.get(position).getType()==1){
            holder.title1.setText("签到成功");
        }else if(mDatas.get(position).getType()==2){
            holder.title1.setText("首评资讯");
        }else if(mDatas.get(position).getType()==3){
            holder.title1.setText("分享成功");
        }else if(mDatas.get(position).getType()==4){
            holder.title1.setText("发布帖子");
        }else if(mDatas.get(position).getType()==5){
            holder.title1.setText("抽奖收入");
        }else if(mDatas.get(position).getType()==6){
            holder.title1.setText("兑换付费资讯");
        }else if(mDatas.get(position).getType()==7){
            holder.title1.setText("抽奖支出");
        }else if(mDatas.get(position).getType()==8){
            holder.title1.setText("完善个人信息");
        }else if(mDatas.get(position).getType()==9){
            holder.title1.setText("查看广告");
        }else if(mDatas.get(position).getType()==10){
            holder.title1.setText("绑定第三方");
        }
        if(mDatas.get(position).getDirection()==1){
            holder.jiajian.setText("+"+mDatas.get(position).getAmount());
            holder.jiajian.setTextColor(0xffff5757);
        }else {
            holder.jiajian.setTextColor(0xff20affa);
            holder.jiajian.setText("-"+mDatas.get(position).getAmount());
        }

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date(mDatas.get(position).getCreateTime());
        String format = sf.format(date1);
        holder.date.setText(""+format);
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.integralrecord_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder { //承载Item视图的子布局
        TextView title1;
        TextView date;
        TextView jiajian;

        public MyViewHolder(View view) {
            super(view);
            title1 = view.findViewById(R.id.title1);
            date = view.findViewById(R.id.date);
            jiajian = view.findViewById(R.id.jiajian);
        }
    }

    public interface Shan{
        void onshan(int i);
    }


}