package com.wildnet.firebasechatapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.wildnet.firebasechatapplication.R;
import com.wildnet.firebasechatapplication.adapter.GroupAllUserAdapter;
import com.wildnet.firebasechatapplication.model.GroupUserModel;
import com.wildnet.firebasechatapplication.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowAllGroupUserActivity extends AppCompatActivity implements View.OnClickListener {

    CircleImageView groupImageIv;
    Intent intent;
    GroupAllUserAdapter mGroupAllUserAdapter;
    private TextView groupNameTv;
    private ImageView onBackIv;
    private RecyclerView allGroupUserRv;
    private String groupName, groupImageUrl;
    private List<GroupUserModel> groupUserList;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_group_user);
        getSupportActionBar().hide();
        findViewById();
        intent = getIntent();
        groupName = intent.getStringExtra("groupName");
        groupImageUrl = intent.getStringExtra("groupImageUrl");
        if (!groupImageUrl.equals("")) {
            Picasso.get().load(groupImageUrl).resize(50, 50).
                    centerCrop().into(groupImageIv);
        }
        groupNameTv.setText(groupName);
        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        allGroupUserRv.setLayoutManager(recyclerViewLayoutManager);
        onBackIv.setOnClickListener(this);
        getGroupAllUser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.userLastSeen(Utility.getUserEmail(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.userOnline(Utility.getUserEmail(this));
    }

    private void getGroupAllUser() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.child("GroupUser").child(groupName).child("groupMember").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupUserList = new ArrayList<>();
                GroupUserModel groupUserModel;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String admin = ds.child("adminTxt").getValue(String.class);
                    String userImageUrl = ds.child("userImageUrl").getValue(String.class);
                    groupUserModel = new GroupUserModel(email, admin, name, userImageUrl);
                    groupUserList.add(groupUserModel);
                }
                Log.e("groupUserList", groupUserList.toString());
                setDataToAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setDataToAdapter() {
        mGroupAllUserAdapter = new GroupAllUserAdapter(this, groupUserList);
        allGroupUserRv.setAdapter(mGroupAllUserAdapter);
    }

    private void findViewById() {
        groupNameTv = (TextView) findViewById(R.id.tv_groupChatName);
        onBackIv = (ImageView) findViewById(R.id.iv_backGroupChat);
        groupImageIv = (CircleImageView) findViewById(R.id.iv_groupImage);
        allGroupUserRv = (RecyclerView) findViewById(R.id.rv_groupAllUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backGroupChat: {
                ShowAllGroupUserActivity.super.onBackPressed();
            }
        }
    }
}
