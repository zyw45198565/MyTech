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
import com.wd.tech.bean.FindFriendNoticePageList;
import com.wd.tech.utils.TimeUtills;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${LinJiangtao}
 * on 2019/2/25
 */
public class FindFriendNoticePageListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<FindFriendNoticePageList> list = new ArrayList<>();
    private ClickListener clickListener;

    public FindFriendNoticePageListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position){
        if (list.get(position).getStatus()==1){
            //未处理
            return 1;
        }else if(list.get(position).getStatus()==2){
            //通过
            return 2;
        }else {
            //拒绝
            return 3;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case 1:
                View inflate1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_find_friend_notice_page, viewGroup, false);
                return new Daichuli(inflate1);
            case 2:
                View inflate2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_find_friend_notice_page_yes, viewGroup, false);
                return new Agree(inflate2);
            case 3:
                View inflate3 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_find_friend_notice_page_no, viewGroup, false);
                return new Jujue(inflate3);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof Daichuli) {
            String timedate = TimeUtills.timedate(list.get(i).getNoticeTime());
            ((Daichuli) viewHolder).tvtime.setText(timedate);
            ((Daichuli) viewHolder).sdv.setImageURI(list.get(i).getFromHeadPic());
            ((Daichuli) viewHolder).tvname.setText(list.get(i).getFromNickName());
            ((Daichuli) viewHolder).agree1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.clickOk(list.get(i).getNoticeId());
                    //Toast.makeText(context, "同意", Toast.LENGTH_SHORT).show();
                }
            });
            ((Daichuli) viewHolder).jujue1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.clickNo(list.get(i).getNoticeId());
                    //Toast.makeText(context, "拒绝", Toast.LENGTH_SHORT).show();
                }
            });

        }else if (viewHolder instanceof Agree){
            String timedate = TimeUtills.timedate(list.get(i).getNoticeTime());
            ((Agree) viewHolder).tvtime2.setText(timedate);
            ((Agree) viewHolder).sdv2.setImageURI(list.get(i).getFromHeadPic());
            ((Agree) viewHolder).tv_name2.setText(list.get(i).getFromNickName());
        }else{
            String timedate = TimeUtills.timedate(list.get(i).getNoticeTime());
            ((Jujue) viewHolder).tvtime3.setText(timedate);
            ((Jujue) viewHolder).sdv3.setImageURI(list.get(i).getFromHeadPic());
            ((Jujue) viewHolder).tv_name3.setText(list.get(i).getFromNickName());
        }
    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    public void addAll(List<FindFriendNoticePageList> result) {
        if (result!=null){
            list.addAll(result);
        }
    }

    public void clear() {
        list.clear();
    }

    //待处理
    class Daichuli extends RecyclerView.ViewHolder {
        TextView tvtime , tvname , agree1 , jujue1;
        SimpleDraweeView sdv;


        public Daichuli(@NonNull View itemView) {
            super(itemView);
            tvtime = itemView.findViewById(R.id.longtime1);
            sdv = itemView.findViewById(R.id.sdv_t1);
            tvname = itemView.findViewById(R.id.tv_name1);
            agree1 = itemView.findViewById(R.id.agree1);
            jujue1 = itemView.findViewById(R.id.jujue1);

        }
    }

    //同意
    class Agree extends RecyclerView.ViewHolder {

        TextView tvtime2 , tv_name2;
        SimpleDraweeView sdv2;

        public Agree(@NonNull View itemView) {
            super(itemView);
            tvtime2 = itemView.findViewById(R.id.longtime2);
            tv_name2 = itemView.findViewById(R.id.tv_name2);
            sdv2 = itemView.findViewById(R.id.sdv_t2);

        }
    }

    //拒绝
    class Jujue extends RecyclerView.ViewHolder {

        TextView tvtime3 , tv_name3;
        SimpleDraweeView sdv3;

        public Jujue(@NonNull View itemView) {
            super(itemView);
            tvtime3 = itemView.findViewById(R.id.longtime3);
            tv_name3 = itemView.findViewById(R.id.tv_name3);
            sdv3 = itemView.findViewById(R.id.sdv_t3);


        }
    }
    public interface ClickListener{
        void clickOk(int id);
        void clickNo(int id);
    }
    public void setOnClickListener(ClickListener clickListener){
       this.clickListener=clickListener;
    }
}
