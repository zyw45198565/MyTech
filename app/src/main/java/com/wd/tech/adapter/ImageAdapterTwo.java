package com.wd.tech.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;

import java.util.ArrayList;
import java.util.List;

class ImageAdapterTwo extends RecyclerView.Adapter<ImageAdapterTwo.MyHolder> {

    ArrayList<String> mList = new ArrayList<>();

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.community_img_item_two,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.image.setImageURI(mList.get(i));//设置图片
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clear() {
        mList.clear();
    }

    public void addAll(List<String> strings) {
        if(strings!=null){
            mList.addAll(strings);
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final SimpleDraweeView image;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.communitylist_img_item);
        }
    }
}
