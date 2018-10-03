package com.wildnet.firebasechatapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wildnet.firebasechatapplication.R;
import com.wildnet.firebasechatapplication.model.UserChatModel;
import com.wildnet.firebasechatapplication.utils.Utility;

import java.util.List;

import me.himanshusoni.chatmessageview.ChatMessageView;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.UserViewHolder> {

    public static int position;
    List<UserChatModel> chatList;
    private Context context;

    public UserChatAdapter(Context context, List<UserChatModel> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_chat_view, viewGroup, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        this.position = position;
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

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
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userChatRight, chatTimeRight, userChatLeft, chatTimeLeft;
        ChatMessageView rightChatView, leftChatView;

        public UserViewHolder(View itemView) {
            super(itemView);
            userChatRight = itemView.findViewById(R.id.chatMsgSetRight);
            chatTimeRight = itemView.findViewById(R.id.chatTimeSetRight);
            userChatLeft = itemView.findViewById(R.id.chatMsgSetLeft);
            chatTimeLeft = itemView.findViewById(R.id.chatTimeSetLeft);
            rightChatView = itemView.findViewById(R.id.RightChatView);
            leftChatView = itemView.findViewById(R.id.LeftChatView);
        }
    }
}
