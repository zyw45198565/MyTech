package com.wd.tech.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.activity.DetailsActivity;
import com.wd.tech.activity.PlateActivity;
import com.wd.tech.bean.MenusBean;

import java.util.ArrayList;
import java.util.List;

public class MenusAdapter extends RecyclerView.Adapter<MenusAdapter.MyHolder> {
    Context context;
    List<MenusBean> list=new ArrayList<>();
    public MenusAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.menus_item, viewGroup, false);
        MyHolder myHolder=new MyHolder(inflate);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final MenusBean menusBean = list.get(i);
        myHolder.cname.setText(menusBean.getName());
        myHolder.img.setImageURI(menusBean.getPic());
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PlateActivity.class);
                intent.putExtra("mname",menusBean.getName());
                intent.putExtra("mid",menusBean.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<MenusBean> result) {
        if (result!=null){
            list.addAll(result);
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView cname,ename;
        SimpleDraweeView img;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cname = itemView.findViewById(R.id.menus_Cname);
            ename = itemView.findViewById(R.id.menus_Ename);
            img = itemView.findViewById(R.id.menus_img);
        }
    }
}
