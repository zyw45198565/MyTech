package com.wd.tech.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.GroupByUser;

import java.util.ArrayList;
import java.util.List;

public class FindGroupsByUserIdAdapter extends RecyclerView.Adapter<FindGroupsByUserIdAdapter.VH> {
    private List<GroupByUser> list = new ArrayList<>();
    private ClickListener clickListener;

    public void setList(List<GroupByUser> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FindGroupsByUserIdAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LinearLayout.inflate(viewGroup.getContext(), R.layout.find_groups_adapter_layout, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindGroupsByUserIdAdapter.VH vh, final int i) {
        vh.simpleDraweeView.setImageURI(Uri.parse(list.get(i).getGroupImage()));
        vh.textView.setText(list.get(i).getGroupName());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.click(list.get(i).getGroupId(),list.get(i).getGroupName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        private final SimpleDraweeView simpleDraweeView;
        private final TextView textView;
        public VH(@NonNull View itemView) {
            super(itemView);
            simpleDraweeView=itemView.findViewById(R.id.find_groups_adapter_icon);
            textView=itemView.findViewById(R.id.find_groups_adapter_name);
        }
    }
    public interface ClickListener{
        void click(int id,String name);
    }
    public void setOnItemClickListener(ClickListener clickListener){
      this.clickListener=clickListener;
    }
}
