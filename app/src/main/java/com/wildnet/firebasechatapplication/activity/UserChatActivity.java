package com.wildnet.firebasechatapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.wildnet.firebasechatapplication.adapter.UserChatAdapter;
import com.wildnet.firebasechatapplication.model.UserActiveModel;
import com.wildnet.firebasechatapplication.model.UserChatModel;
import com.wildnet.firebasechatapplication.utils.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class UserChatActivity extends AppCompatActivity implements View.OnClickListener {
    String currentUserEmail;
    EmojiconEditText mEmojiEditTextChat;
    View mRootView;
    CircleImageView userImage;
    private Intent intent;
    private String userName, userEmail, url, userActiveChk;
    private TextView userNameTv, userLastSeenCheck;
    private ImageView imageViewEmoji, messageSend, iv_moreEvent;
    private EditText chatMessage;
    private RecyclerView recyclerViewChat;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private List<UserChatModel> userChatList = new ArrayList<>();
    private UserChatModel userChatModel, userChat;
    private UserChatAdapter userChatAdapter;
    private ImageView menuBar;
    private LinearLayout ll_userChatProfile;
    private Toolbar mToolbar;
    private EmojIconActions mEmojiIcons;
    private ImageView backArrow;
    private UserActiveModel userActiveModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        findViewById();
        getSupportActionBar().hide();
        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewChat.setLayoutManager(recyclerViewLayoutManager);
        userActiveModel = new UserActiveModel();
        messageSend.setOnClickListener(this);
        imageViewEmoji.setOnClickListener(this);
        userNameTv.setOnClickListener(this);

        intent = getIntent();
        showUserProfile();
        setUserChat();
        menuBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsMenu();
            }
        });

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

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserChatActivity.super.onBackPressed();
            }
        });
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserChatActivity.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerViewChat.smoothScrollToPosition(UserChatAdapter.position);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.userLastSeen(Utility.getUserEmail(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewChat.smoothScrollToPosition(UserChatAdapter.position);
        Utility.userOnline(Utility.getUserEmail(this));
    }


    private void findViewById() {
        userNameTv = findViewById(R.id.tv_userChatName);
        recyclerViewChat = findViewById(R.id.rv_userChatHistory);
        imageViewEmoji = findViewById(R.id.iv_emoji);
        messageSend = findViewById(R.id.iv_messageSend);
        chatMessage = findViewById(R.id.et_chatMessage);
        userImage = findViewById(R.id.iv_userChatImage);
        menuBar = (ImageView) findViewById(R.id.iv_menuBar);
        ll_userChatProfile = findViewById(R.id.ll_userChatProfile);
        mEmojiEditTextChat = (EmojiconEditText) findViewById(R.id.edittext_hani_chatbox);
        mRootView = findViewById(R.id.mRootView);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        backArrow = findViewById(R.id.iv_backUserList);
        userLastSeenCheck = (TextView) findViewById(R.id.tv_userLastSeen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mToolbar.inflateMenu(R.menu.menu_user_chat);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_userDetail: {
                Intent intent = new Intent(UserChatActivity.this, UserProfileActivity.class);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
                //Toast.makeText(this, "action_userDetail", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private void showUserProfile() {
        userName = intent.getStringExtra("name");
        userEmail = intent.getStringExtra("email");
        url = intent.getStringExtra("imageUrl");
        userActiveChk = intent.getStringExtra("activeUser");
        currentUserEmail = intent.getStringExtra("currentUser");
        userLastSeenCheck.setText(userActiveChk);
        userNameTv.setText(userName);
        if (!url.equalsIgnoreCase(""))
            Picasso.get().load(url).resize(50, 50).
                    centerCrop().into(userImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_messageSend: {
                userChatList = new ArrayList<>();
                String chatMsg = mEmojiEditTextChat.getText().toString().trim();
                if (!chatMsg.equalsIgnoreCase("")) {
                    userChatModel = new UserChatModel(chatMsg, Utility.getCurrTime(), Utility.getUserEmail(this));
                    //userChat=new UserChatModel(chatMsg,Utility.getCurrTime());
                    userChatList.add(userChatModel);
                    mEmojiEditTextChat.setText("");
                    setDataToAdapter();
                    setChatToFirebase();
                } else {
                    mEmojiEditTextChat.setError("Please enter message");
                }
                break;
            }
            case R.id.iv_emoji: {
                mEmojiIcons.setUseSystemEmoji(true);
                mEmojiEditTextChat.setUseSystemDefault(true);
                break;
            }
            case R.id.tv_userChatName: {
                Intent intent = new Intent(UserChatActivity.this, UserProfileActivity.class);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
                break;
            }

        }
    }

    private void setChatToFirebase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        currentUserEmail = currentUserEmail.replace(".", "");
        userEmail = userEmail.replace(".", "");

        myRef.child("User").child(currentUserEmail).child(currentUserEmail + "To" + userEmail)
                .child(String.valueOf(new Date().getTime())).setValue(userChatModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        myRef.child("User").child(userEmail).child(userEmail + "To" + currentUserEmail)
                .child(String.valueOf(new Date().getTime())).setValue(userChatModel);
    }


    private void setUserChat() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        currentUserEmail = currentUserEmail.replace(".", "");
        userEmail = userEmail.replace(".", "");

        myRef.child("User").child(currentUserEmail).child(currentUserEmail + "To" + userEmail).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userChatList = new ArrayList<>();
                        UserChatModel userChatModel;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String chat = ds.child("chat").getValue(String.class);
                            String time = ds.child("currentTime").getValue(String.class);
                            String email = ds.child("email").getValue(String.class);
                            userChatModel = new UserChatModel(chat, time, email);
                            userChatList.add(userChatModel);

                        }
                        setDataToAdapter();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        setUserLastSeen();
    }

    private void setUserLastSeen() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        userEmail = userEmail.replace(".", "");
        myRef.child("User").child(userEmail).child("activeUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("dataSnapshot", dataSnapshot.toString());
                String userLastSeen = dataSnapshot.child("activeCheck").getValue(String.class);
                userLastSeenCheck.setText(userLastSeen);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setDataToAdapter() {
        userChatAdapter = new UserChatAdapter(this, userChatList);
        recyclerViewChat.setAdapter(userChatAdapter);
        recyclerViewChat.scrollToPosition(recyclerViewChat.getAdapter().getItemCount() - 1);
    }
}
