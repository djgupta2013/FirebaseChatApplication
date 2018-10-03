package com.wildnet.firebasechatapplication.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.wildnet.firebasechatapplication.activity.GroupChatActivity;
import com.wildnet.firebasechatapplication.model.GroupNameModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.UserViewHolder> {
    List<GroupNameModel> userList;
    String groupName;
    private Context context;

    public GroupListAdapter(Context context, List<GroupNameModel> userList, String groupName) {
        this.context = context;
        this.userList = userList;
        this.groupName = groupName;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_main, viewGroup, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        String nameOfGroup = userList.get(position).getGroupName();
        holder.groupName.setText(nameOfGroup);
        String url = userList.get(position).getIamgeUrl();
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
        TextView groupName;
        CircleImageView userImage;

        public UserViewHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.tv_userName);
            userImage = itemView.findViewById(R.id.iv_userImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            List<GroupNameModel> list = new ArrayList<>();
            Intent intent = new Intent(context, GroupChatActivity.class);
            list.add(userList.get(position));
            intent.putExtra("data", (Serializable) list);
            context.startActivity(intent);
        }
    }
}
