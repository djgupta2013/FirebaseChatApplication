package com.wildnet.firebasechatapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wildnet.firebasechatapplication.R;
import com.wildnet.firebasechatapplication.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewGroupCreateAdapter extends RecyclerView.Adapter<NewGroupCreateAdapter.UserViewHolder> {
    List<UserModel> userList;
    List<UserModel> selectUserList;
    String userEmail;
    CheckBoxData listener;
    private Context context;

    public NewGroupCreateAdapter(Context context, List<UserModel> userList, String userEmail) {
        this.context = context;
        this.userList = userList;
        this.userEmail = userEmail;
        selectUserList = new ArrayList<>();
    }

    public void setListener(CheckBoxData listener) {
        this.listener = listener;
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

    public interface CheckBoxData {
        void checkData(int position, boolean isCheck);
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        CircleImageView userImage;
        CheckBox mCheckBox;

        public UserViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_userName);
            userImage = itemView.findViewById(R.id.iv_userImage);
            mCheckBox = itemView.findViewById(R.id.chb_user);
            mCheckBox.setVisibility(View.VISIBLE);

            mCheckBox.setOnClickListener(this);
             }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.chb_user: {
                    int position = getAdapterPosition();
                    UserModel model = userList.get(position);
                    if (model.isSelected()) {
                        model.setSelected(false);
                        listener.checkData(position, false);
                    } else {
                        model.setSelected(true);
                        listener.checkData(position, true);
                    }
                }
                case R.id.iv_next: {
                }
            }
        }
    }
}
