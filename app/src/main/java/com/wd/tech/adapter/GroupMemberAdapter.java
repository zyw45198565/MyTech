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
import com.wd.tech.bean.FriendGroup;
import com.wd.tech.bean.GroupMember;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.VH> {
    private List<GroupMember> list = new ArrayList<>();
    private ClickListener clickListener;

    public void setList(List<GroupMember> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public GroupMemberAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LinearLayout.inflate(viewGroup.getContext(), R.layout.group_member_item, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMemberAdapter.VH vh, final int i) {

        vh.textView.setText(list.get(i).getNickName());
        vh.simpleDraweeView.setImageURI(Uri.parse(list.get(i).getHeadPic()));
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.click(list.get(i).getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        private final TextView textView;
        private final SimpleDraweeView simpleDraweeView;
        public VH(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.group_member_item_name);
            simpleDraweeView=itemView.findViewById(R.id.group_member_item_icon);
        }
    }
    public interface ClickListener{
        void click(int id);
    }
    public void setOnItemClickListener(ClickListener clickListener){
      this.clickListener=clickListener;
    }
}
