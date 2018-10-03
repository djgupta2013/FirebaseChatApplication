package com.wildnet.firebasechatapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.wildnet.firebasechatapplication.R;
import com.wildnet.firebasechatapplication.adapter.GroupChatAdapter;
import com.wildnet.firebasechatapplication.model.GroupChatModel;
import com.wildnet.firebasechatapplication.model.GroupNameModel;
import com.wildnet.firebasechatapplication.utils.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    List<GroupNameModel> mModel;
    ImageView backArrow, msgSend, imageViewEmoji;
    TextView groupName;
    EmojiconEditText mEmojiEditTextChat;
    View mRootView;
    GroupChatModel mGroupChatModel;
    List<GroupChatModel> groupChatList;
    GroupChatAdapter mGroupChatAdapter;
    RecyclerView groupRecyclerView;
    String currentUserEmail, groupNametxt;
    private Bundle data;
    private CircleImageView groupChatImage;
    private EmojIconActions mEmojiIcons;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        getSupportActionBar().hide();
        findViewById();
        getIntentData();
        backArrow.setOnClickListener(this);
        imageViewEmoji.setOnClickListener(this);
        msgSend.setOnClickListener(this);
        groupName.setOnClickListener(this);
        groupChatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupChatActivity.super.onBackPressed();
            }
        });
        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        groupRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        setGroupChat();

        mEmojiIcons = new EmojIconActions(this, mRootView, mEmojiEditTextChat, imageViewEmoji);
        mEmojiIcons.ShowEmojIcon();
        mEmojiIcons.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {

            }

            @Override
            public void onKeyboardClose() {

            }
        });
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

    private void findViewById() {
        backArrow = findViewById(R.id.iv_backUserGroupList);
        groupChatImage = findViewById(R.id.iv_groupChatImage);
        groupName = findViewById(R.id.tv_groupChatName);
        msgSend = findViewById(R.id.iv_groupMessageSend);
        mEmojiEditTextChat = findViewById(R.id.edittext_hani_chatboxGroup);
        mRootView = findViewById(R.id.mRootViewGroup);
        imageViewEmoji = findViewById(R.id.iv_groupEmoji);
        groupRecyclerView = findViewById(R.id.rv_groupChatHistory);
    }

    private void getIntentData() {
        intent = getIntent();
        data = new Bundle();
        data = intent.getExtras();
        if (data != null) {
            mModel = (List<GroupNameModel>) data.getSerializable("data");
            groupNametxt = mModel.get(0).getGroupName();
            groupName.setText(mModel.get(0).getGroupName());
            String url = mModel.get(0).getIamgeUrl();
            if (!url.equals("")) {
                Picasso.get().load(url).resize(50, 50).
                        centerCrop().into(groupChatImage);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backUserGroupList: {
                GroupChatActivity.super.onBackPressed();
            }
            case R.id.iv_groupEmoji: {
                mEmojiIcons.setUseSystemEmoji(true);
                mEmojiEditTextChat.setUseSystemDefault(true);
                break;
            }
            case R.id.iv_groupMessageSend: {
                groupChatList = new ArrayList<>();
                String chatMsg = mEmojiEditTextChat.getText().toString().trim();
                if (!chatMsg.equalsIgnoreCase("")) {
                    mGroupChatModel = new GroupChatModel(chatMsg, Utility.getCurrTime(), Utility.getUserEmail(this),
                            Utility.getUserName(this));
                    //userChat=new UserChatModel(chatMsg,Utility.getCurrTime());
                    groupChatList.add(mGroupChatModel);
                    mEmojiEditTextChat.setText("");
                    setDataToAdapter();
                    setChatToFirebase();
                } else {
                    mEmojiEditTextChat.setError("Please enter message");
                }
                break;
            }
            case R.id.tv_groupChatName: {
                Intent intent = new Intent(GroupChatActivity.this, ShowAllGroupUserActivity.class);
                intent.putExtra("groupName", groupNametxt);
                intent.putExtra("groupImageUrl", mModel.get(0).getIamgeUrl());
                startActivity(intent);
            }
        }
    }

    private void setDataToAdapter() {
        mGroupChatAdapter = new GroupChatAdapter(this, groupChatList);
        groupRecyclerView.setAdapter(mGroupChatAdapter);
        groupRecyclerView.scrollToPosition(groupRecyclerView.getAdapter().getItemCount() - 1);
    }

    private void setChatToFirebase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        currentUserEmail = Utility.getUserEmail(this);
        String userEmail = currentUserEmail.replace(".", "");

        myRef.child("GroupUser").child(groupNametxt).child(groupNametxt + "123").child(String.valueOf(new Date().getTime()))
                .setValue(mGroupChatModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void setGroupChat() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("GroupUser");

        myRef.child(groupNametxt).child(groupNametxt + "123").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupChatList = new ArrayList<>();
                GroupChatModel groupChatModel;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String chat = ds.child("chat").getValue(String.class);
                    String time = ds.child("currentTime").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String name = ds.child("name").getValue(String.class);
                    groupChatModel = new GroupChatModel(chat, time, email, name);
                    groupChatList.add(groupChatModel);
                }
                setDataToAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
