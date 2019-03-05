package com.wd.tech.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.activity.DetailsActivity;
import com.wd.tech.activity.SearchActivity;
import com.wd.tech.bean.InformationSearchByTitleBean;
import com.wd.tech.utils.util.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchByTitleAdapter extends RecyclerView.Adapter<SearchByTitleAdapter.ViewHolder> {

    Context mContext;
    List<InformationSearchByTitleBean> mSearchByTitleBeans=new ArrayList<>();


    public SearchByTitleAdapter(Context context) {
        mContext = context;
    }

    public void reset(List<InformationSearchByTitleBean> searchlist) {
        mSearchByTitleBeans.clear();
        mSearchByTitleBeans.addAll(searchlist);
        notifyDataSetChanged();

    }

    public void clear() {
        mSearchByTitleBeans.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.searchbytitle_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.title.setText(mSearchByTitleBeans.get(i).getTitle());
        viewHolder.zuozhe.setText(mSearchByTitleBeans.get(i).getSource());
        try {
            viewHolder.time.setText(DateUtils.dateFormat(new Date(mSearchByTitleBeans.get(i).getReleaseTime()), DateUtils.MINUTE_PATTERN));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,DetailsActivity.class);
                intent.putExtra("zid",mSearchByTitleBeans.get(i).getId());
                mContext.startActivity(intent);
            }
        });
    }




    @Override
    public int getItemCount() {
        return mSearchByTitleBeans.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView zuozhe;
        private TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            zuozhe = itemView.findViewById(R.id.zuozhe);
            time = itemView.findViewById(R.id.time);
        }
    }
}
