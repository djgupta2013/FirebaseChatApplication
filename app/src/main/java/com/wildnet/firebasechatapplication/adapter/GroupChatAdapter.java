package com.wildnet.firebasechatapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wildnet.firebasechatapplication.R;
import com.wildnet.firebasechatapplication.model.GroupChatModel;
import com.wildnet.firebasechatapplication.utils.Utility;

import java.util.List;

import me.himanshusoni.chatmessageview.ChatMessageView;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.UserViewHolder> {

    public static int position;
    List<GroupChatModel> chatList;
    private Context context;

    public GroupChatAdapter(Context context, List<GroupChatModel> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public GroupChatAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.group_chat_layout, viewGroup, false);
        return new GroupChatAdapter.UserViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        this.position = position;
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatAdapter.UserViewHolder holder, int position) {

        String userEmail = Utility.getUserEmail(context);
        if (userEmail.equalsIgnoreCase(chatList.get(position).getEmail())) {
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.userChatRight.setText(chatList.get(position).getChat());
            holder.chatTimeRight.setText(chatList.get(position).getCurrentTime());
        } else {
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.userChatLeft.setText(chatList.get(position).getChat());
            holder.chatTimeLeft.setText(chatList.get(position).getCurrentTime());
            holder.userNameLeft.setText(chatList.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userChatRight, chatTimeRight, userChatLeft, chatTimeLeft, userNameRight, userNameLeft;
        ChatMessageView rightChatView, leftChatView;

        public UserViewHolder(View itemView) {
            super(itemView);
            userChatRight = itemView.findViewById(R.id.tv_groupChatMsgSetRight);
            chatTimeRight = itemView.findViewById(R.id.tv_groupChatTimeSetRight);
            userChatLeft = itemView.findViewById(R.id.tv_groupChatMsgSetLeft);
            chatTimeLeft = itemView.findViewById(R.id.tv_groupChatTimeSetLeft);
            rightChatView = itemView.findViewById(R.id.groupRightChatView);
            leftChatView = itemView.findViewById(R.id.groupLeftChatView);
            userNameRight = itemView.findViewById(R.id.tv_userNameRight);
            userNameLeft = itemView.findViewById(R.id.tv_userNameLeft);
        }
    }
}
