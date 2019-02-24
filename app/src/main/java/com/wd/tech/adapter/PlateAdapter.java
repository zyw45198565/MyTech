package com.wd.tech.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.bean.DetailsBean;

import java.util.ArrayList;
import java.util.List;

public class PlateAdapter extends RecyclerView.Adapter<PlateAdapter.Myholder> {
    Context context;
    List<DetailsBean.PlateBean> list=new ArrayList<>();

    public PlateAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.plate_item, viewGroup, false);
        Myholder myholder=new Myholder(view);
        return myholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder myholder, int i) {
        myholder.plate.setText(list.get(i).getName());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<DetailsBean.PlateBean> plate) {
        if (plate!=null){
            list.addAll(plate);
        }
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView plate;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            plate = itemView.findViewById(R.id.plate);
        }
    }
}
