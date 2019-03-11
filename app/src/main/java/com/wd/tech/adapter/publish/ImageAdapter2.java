package com.wd.tech.adapter.publish;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.adapter.MyPostAdapter;
import com.wd.tech.utils.util.UIUtils;
import com.wd.tech.utils.util.WDActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dingtao
 * @date 2019/1/3 23:24
 * qq:1940870847
 * 跳转相册上传图片
 */
public class ImageAdapter2 extends RecyclerView.Adapter<ImageAdapter2.MyHodler> {

    private List<Object> mList;
    Open open;
    Context context;
    private LayoutInflater inflater;
    public ImageAdapter2(List<Object> mList,Open open,Context context) {
        this.mList = mList;
        this.open = open;
        this.context=context;
        inflater= LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      //  View view = View.inflate(context, R.layout.publish_picter_layout, null);
        View view = inflater.inflate(R.layout.publish_picter_layout,viewGroup,false);
        MyHodler holder=new MyHodler(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHodler myHodler, final int position) {
            if (mList.get(position) instanceof String) {
                String imageUrl = (String) mList.get(position);
                if (imageUrl.contains("http:")) {//加载http
                    myHodler.image.setImageURI(imageUrl);//Uri.parse(imageUrl)
                } else {//加载sd卡
                    Uri uri = Uri.parse("file://" + imageUrl);
                    myHodler.image.setImageURI(uri);
                }
            } else {//加载资源文件
                int id = (int) mList.get(position);
                Uri uri = Uri.parse("res:///" + id);//res://com.dingtao.rrmmp/
                myHodler.image.setImageURI(uri);
            }

        myHodler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){
                    open.onDakaiXiangCe();
                }else {
                    UIUtils.showToastSafe(""+position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clear() {
        mList.clear();
    }

    public void add(Object image) {
        if (image != null) {
            mList.add(image);
        }
    }

    public List<Object> getList() {
        return mList;
    }

    class MyHodler extends RecyclerView.ViewHolder {
        SimpleDraweeView image;

        public MyHodler(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.publish_picter);
        }
    }
    public interface Open{
        void onDakaiXiangCe();
    }
}
