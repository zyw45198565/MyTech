package com.wd.tech.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

public class HomeAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPRONE = 1;
    private final int TYPETWO = 2;
    private final int TYPETHREE = 3;
    List<HomeAll> list = new ArrayList<>();
    private int whetherAdvertising;
    Mylove mylove;
    MyShare myShare;
    Context context;

    public HomeAllAdapter(Context context) {
        this.context = context;
    }

    public void xihuan(Mylove mylove) {
        this.mylove = mylove;
    }

    public void sharecircle(MyShare myShare) {
        this.myShare = myShare;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getWhetherAdvertising() == 1) {
            return TYPRONE;
        } else if (list.get(position).getWhetherAdvertising() == 2) {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        whetherAdvertising = list.get(i).getWhetherAdvertising();
        final HomeAll homeAll = list.get(i);
        int itemViewType = getItemViewType(i);
        switch (itemViewType) {
            case TYPETWO:
                final ViewHolderA holderA = (ViewHolderA) viewHolder;
                String[] split = list.get(i).getThumbnail().split("\\?");
                holderA.simple.setImageURI(split[0]);
                holderA.title.setText(homeAll.getTitle());
                holderA.content.setText(homeAll.getSummary());
                holderA.writer.setText(homeAll.getSource());
                holderA.likenum.setText(homeAll.getCollection() + "");
                holderA.sharenum.setText(homeAll.getShare() + "");

                holderA.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("zid", homeAll.getId());
                        intent.putExtra("classify",2);

                        context.startActivity(intent);
                    }
                });
                //是否付费
                int whetherPay = homeAll.getWhetherPay();
                if (whetherPay == 1) {
                    holderA.buyall.setVisibility(View.VISIBLE);
                } else {
                    holderA.buyall.setVisibility(View.GONE);
                }

                //是否收藏

                if (homeAll.getWhetherCollection() == 1) {
                    holderA.like.setImageResource(R.drawable.collect_s);
                }else{
                    holderA.like.setImageResource(R.drawable.collect_w);

                }

                holderA.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int whetherCollection1 = homeAll.getWhetherCollection();
                        mylove.win(homeAll.getId(),homeAll.getWhetherCollection(),i);

                        if (whetherCollection1 ==2){
                            homeAll.setWhetherCollection(1);
                            homeAll.setCollection(homeAll.getCollection()+1);
                       }else{
                            homeAll.setWhetherCollection(2);
                            homeAll.setCollection(homeAll.getCollection()-1);
//                            mylove.win(homeAll.getId(),homeAll.getWhetherCollection(),i);
                       }
                    }
                });

                holderA.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myShare.share(i);
                    }
                });
                break;
            case TYPRONE:
                ViewHolderB holderB = (ViewHolderB) viewHolder;
                holderB.twotitle.setText(homeAll.getInfoAdvertisingVo().getContent());
                Glide.with(context).load(homeAll.getInfoAdvertisingVo().getPic()).into(holderB.twoimg);
//                holderB.twoimg.setImageURI(homeAll.getInfoAdvertisingVo().getPic());
                holderB.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AdvertiseActivity.class);
                        intent.putExtra("zurl", homeAll.getInfoAdvertisingVo().getUrl());
                        context.startActivity(intent);
                    }
                });
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

    public void clearAll() {
        list.clear();
    }

    public void notifyDataSetChanged(int homealli) {
        notifyItemChanged(homealli);
//        notifyDataSetChanged(homealli);
    }

    private class ViewHolderA extends RecyclerView.ViewHolder {
        SimpleDraweeView simple;
        ImageView buyall;
        TextView title, content, writer, likenum, sharenum;
        CheckBox  share;
        ImageView like;

        public ViewHolderA(@NonNull View itemView) {
            super(itemView);
            simple = itemView.findViewById(R.id.simple);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            writer = itemView.findViewById(R.id.writer);
            like = itemView.findViewById(R.id.like);
            share = itemView.findViewById(R.id.share);
            buyall = itemView.findViewById(R.id.buy_all);
            likenum = itemView.findViewById(R.id.likenum);
            sharenum = itemView.findViewById(R.id.sharenum);

        }
    }

    private class ViewHolderB extends RecyclerView.ViewHolder {
        //        SimpleDraweeView twoimg;
        ImageView twoimg;
        TextView twotitle;

        public ViewHolderB(@NonNull View itemView) {
            super(itemView);
            twoimg = itemView.findViewById(R.id.two_img);
            twotitle = itemView.findViewById(R.id.tilte_two);
        }
    }

    public interface Mylove {
        void win(int id,int whetherCollection, int possion);
    }

    public interface MyShare {
        void share(int possion);

    }
}
