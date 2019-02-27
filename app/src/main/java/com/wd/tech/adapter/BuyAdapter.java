package com.wd.tech.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.activity.AdvertiseActivity;
import com.wd.tech.activity.DetailsActivity;
import com.wd.tech.bean.HomeAll;

import java.util.ArrayList;
import java.util.List;

public class BuyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<HomeAll> list = new ArrayList<>();
    Context context;

    public BuyAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_one_item, viewGroup, false);
        ViewHolderA viewHolderA = new ViewHolderA(view);
        return viewHolderA;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final HomeAll homeAll = list.get(i);

        ViewHolderA holderA = (ViewHolderA) viewHolder;
        String[] split = list.get(i).getThumbnail().split("\\?");
        holderA.simple.setImageURI(split[0]);
        holderA.title.setText(homeAll.getTitle());
        holderA.content.setText(homeAll.getSummary());
        holderA.writer.setText(homeAll.getSource());
        holderA.likenum.setText(homeAll.getCollection() + "");
        holderA.sharenum.setText(homeAll.getShare() + "");
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

    public void clearAll() {
        list.clear();
    }

    private class ViewHolderA extends RecyclerView.ViewHolder {
        SimpleDraweeView simple;
        ImageView buyall;
        TextView title, content, writer, likenum, sharenum;
        CheckBox like;

        public ViewHolderA(@NonNull View itemView) {
            super(itemView);
            simple = itemView.findViewById(R.id.simple);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            writer = itemView.findViewById(R.id.writer);
            like = itemView.findViewById(R.id.like);
            buyall = itemView.findViewById(R.id.buy_all);
            likenum = itemView.findViewById(R.id.likenum);
            sharenum = itemView.findViewById(R.id.sharenum);

        }
    }

}
