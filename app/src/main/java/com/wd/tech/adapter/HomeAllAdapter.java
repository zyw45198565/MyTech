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
import com.wd.tech.bean.HomeAll;

import java.util.ArrayList;
import java.util.List;

public class HomeAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPRONE = 1;
    private final int TYPETWO = 2;
    private final int TYPETHREE = 3;
    List<HomeAll> list = new ArrayList<>();
    private int whetherAdvertising;

    Context context;

    public HomeAllAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (whetherAdvertising == 1) {
            return TYPRONE;
        } else if (whetherAdvertising == 2) {
            return TYPETWO;
        }
        return TYPETWO;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 2) {
            View view = LayoutInflater.from(context).inflate(R.layout.home_one_item, viewGroup, false);
            ViewHolderA viewHolderA = new ViewHolderA(view);
            return viewHolderA;
        } else if (i == 1) {
            //广告
            View view = LayoutInflater.from(context).inflate(R.layout.home_two_item, viewGroup, false);
            ViewHolderB viewHolderB = new ViewHolderB(view);
            return viewHolderB;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        whetherAdvertising = list.get(i).getWhetherAdvertising();
        HomeAll homeAll = list.get(i);
        int itemViewType = getItemViewType(i);
        switch (itemViewType) {
            case TYPETWO:
                ViewHolderA holderA=(ViewHolderA)viewHolder;
                String[] split = list.get(i).getThumbnail().split("\\?");
                holderA.simple.setImageURI(split[0]);
                holderA.title.setText(homeAll.getTitle());
                holderA.content.setText(homeAll.getSummary());
                holderA.writer.setText(homeAll.getSource());
                break;
            case TYPRONE:
                ViewHolderB holderB= (ViewHolderB) viewHolder;
                holderB.twoimg.setImageURI(homeAll.getInfoAdvertisingVo().getUrl());
                break;

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<HomeAll> result) {
        if (result != null) {
            list.addAll(result);
        }
    }

    private class ViewHolderA extends RecyclerView.ViewHolder {
        SimpleDraweeView simple;
        TextView title, content, writer;

        public ViewHolderA(@NonNull View itemView) {
            super(itemView);
            simple = itemView.findViewById(R.id.simple);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            writer = itemView.findViewById(R.id.writer);

        }
    }

    private class ViewHolderB extends RecyclerView.ViewHolder {
        SimpleDraweeView twoimg;
        public ViewHolderB(@NonNull View itemView) {
            super(itemView);
            twoimg=itemView.findViewById(R.id.two_img);
        }
    }

}
