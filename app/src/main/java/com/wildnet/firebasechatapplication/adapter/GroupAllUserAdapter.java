package com.wildnet.firebasechatapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wildnet.firebasechatapplication.R;
import com.wildnet.firebasechatapplication.model.GroupUserModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAllUserAdapter extends RecyclerView.Adapter<GroupAllUserAdapter.UserViewHolder> {
    List<GroupUserModel> userList;
    private Context context;

    public GroupAllUserAdapter(Context context, List<GroupUserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public GroupAllUserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_all_group_user, viewGroup, false);
        return new GroupAllUserAdapter.UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAllUserAdapter.UserViewHolder holder, int position) {
        String adminUser = userList.get(position).getAdminTxt();
        if (!adminUser.equals("")) {
            holder.admin.setVisibility(View.VISIBLE);
            holder.admin.setText(adminUser);
        } else {
            holder.admin.setVisibility(View.GONE);
        }
        holder.userName.setText(userList.get(position).getName());
        String url = userList.get(position).getUserImageUrl();
        if (!url.equalsIgnoreCase(""))
            Picasso.get().load(url).resize(50, 50).
                    centerCrop().into(holder.userImage, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError(Exception e) {
                }
            });
    }

    @Override
    public int getItemCount() {
        Log.d("userList.size()", userList.size() + "");
        return userList.size();

    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userName, admin;
        CircleImageView userImage;

        public UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_groupUserName);
            userImage = itemView.findViewById(R.id.iv_groupUserImage);
            admin = itemView.findViewById(R.id.tv_groupUserAdminName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            List<GroupUserModel> list = new ArrayList<>();
        }
    }
}
