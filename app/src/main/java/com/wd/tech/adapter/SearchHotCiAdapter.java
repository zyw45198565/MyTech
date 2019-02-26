package com.wd.tech.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.tech.R;

import java.util.ArrayList;
import java.util.List;

public class SearchHotCiAdapter extends RecyclerView.Adapter<SearchHotCiAdapter.ViewHolder>{
    Context context;
    List<String> lists;

    public SearchHotCiAdapter(Context context, List<String> lists) {
        this.context = context;
        this.lists = lists;
    }

    public interface fuzhi{
        void hotci(String s);
    }
    private fuzhi mFuzhi;

    public void setFuzhi(fuzhi fuzhi) {
        mFuzhi = fuzhi;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.hot_item, viewGroup, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(lists.get(i));

        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFuzhi.hotci(lists.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
        }
    }
}
