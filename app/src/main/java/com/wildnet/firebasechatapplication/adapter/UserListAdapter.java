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
import com.wildnet.firebasechatapplication.activity.UserChatActivity;
import com.wildnet.firebasechatapplication.model.UserModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    List<UserModel> userList;
    String userEmail;
    private Context context;

    public UserListAdapter(Context context, List<UserModel> userList, String userEmail) {
        this.context = context;
        this.userList = userList;
        this.userEmail = userEmail;
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
        holder.name.setText(userList.get(position).getName());
        String url = userList.get(position).getUrl();
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
        TextView name;
        CircleImageView userImage;

        public UserViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_userName);
            userImage = itemView.findViewById(R.id.iv_userImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String email = userList.get(position).getEmail();
            String name = userList.get(position).getName();
            String imageUrl = userList.get(position).getUrl();
            Intent intent = new Intent(context, UserChatActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("name", name);
            intent.putExtra("currentUser", userEmail);
            intent.putExtra("imageUrl", imageUrl);
            intent.putExtra("activeUser", userList.get(position).getUserActiveCheck());
            context.startActivity(intent);
        }
    }
}
