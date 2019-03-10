package com.wd.tech.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.activity.DetailsActivity;
import com.wd.tech.bean.DetailsBean;

import java.util.ArrayList;
import java.util.List;

public class InforAdapter extends RecyclerView.Adapter<InforAdapter.Myholder> {
    Context context;
    List<DetailsBean.InformationListBean> list=new ArrayList<>();

    public InforAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.infor_item, viewGroup, false);
        Myholder myholder=new Myholder(view);
        return myholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder myholder, final int i) {
        myholder.title.setText(list.get(i).getTitle());
        myholder.thumbnail.setImageURI(list.get(i).getThumbnail());
        myholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DetailsActivity.class);
                intent.putExtra("zid",list.get(i).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<DetailsBean.InformationListBean> informationListBeans) {
        if (informationListBeans!=null){
            list.addAll(informationListBeans);
        }
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView title;
        SimpleDraweeView thumbnail;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }
}

