package com.wildnet.firebasechatapplication.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wildnet.firebasechatapplication.R;
import com.wildnet.firebasechatapplication.adapter.GroupListAdapter;
import com.wildnet.firebasechatapplication.adapter.UserListAdapter;
import com.wildnet.firebasechatapplication.model.GroupNameModel;
import com.wildnet.firebasechatapplication.model.UserActiveModel;
import com.wildnet.firebasechatapplication.model.UserModel;
import com.wildnet.firebasechatapplication.utils.Constants;
import com.wildnet.firebasechatapplication.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    public List<UserModel> userList;
    public List<GroupNameModel> mGroupNameModelList;
    RecyclerView recyclerView, groupRecyclerView;
    UserListAdapter mChatAdapter;
    GroupListAdapter mGroupChatAdapter;
    FirebaseAuth mAuth;
    String userEmail, groupName;
    String name;
    RecyclerView.LayoutManager RecyclerViewLayoutManager, recyclerViewLayoutManager;

    private ProgressDialog progressBar;
    private UserActiveModel userActiveModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);
        mAuth = FirebaseAuth.getInstance();
        userEmail = mAuth.getCurrentUser().getEmail();
        mGroupNameModelList = new ArrayList<>();
        userActiveModel = new UserActiveModel();
        recyclerView = findViewById(R.id.recycler);
        groupRecyclerView = findViewById(R.id.group_recycler);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("User");
        userActiveModel.setActiveCheck("online");
        String email = userEmail.replace(".", "");
        usersdRef.child(email).child("activeUser").setValue(userActiveModel);
        progressBar = new ProgressDialog(UserListActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressBar.setMessage("Loading......");
        progressBar.setCancelable(false);
        progressBar.show();
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        groupRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        getAllUser();
        getAllGroup();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.userLastSeen(Utility.getUserEmail(this));
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

    private void setDataToAdapter() {
        mChatAdapter = new UserListAdapter(this, userList, userEmail);
        recyclerView.setAdapter(mChatAdapter);
        progressBar.dismiss();
    }

    private void getAllUser() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("User");

        rootRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                UserModel userModel;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String emailCheck = ds.child("email").getValue(String.class);
                    if (emailCheck.equalsIgnoreCase(userEmail)) {
                        name = ds.child("name").getValue(String.class);
                        Utility.saveCurrentUserName(UserListActivity.this, Constants.CURRENT_USER_NAME, name);
                        Log.e("userEmail", userEmail);
                    } else {
                        String email = ds.child("email").getValue(String.class);
                        String name = ds.child("name").getValue(String.class);
                        String imageUrl = ds.child("userImageUrl").getValue(String.class);
                        String activeCheck = ds.child("activeUser").child("activeCheck").getValue(String.class);
                        userModel = new UserModel(name, email, imageUrl, activeCheck);
                        userList.add(userModel);
                        Log.d("TAG", name);
                    }
                }
                setDataToAdapter();
                Log.d("onDataChange", "onDataChange");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("onCancelled", "onCancelled");
            }
        });
    }

    public void setGroupUserToAdapter() {
        mGroupChatAdapter = new GroupListAdapter(this, mGroupNameModelList, groupName);
        groupRecyclerView.setAdapter(mGroupChatAdapter);
    }

    private void getAllGroup() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("GroupUser");
        usersdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGroupNameModelList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    groupName = ds.getKey();
                    GroupNameModel groupNameModel;
                    String groupImageUrl = ds.child("groupImageUrl").getValue(String.class);
                    if (ds.child("groupMember").getValue().toString().contains(userEmail)) {
                        groupNameModel = new GroupNameModel(groupName, groupImageUrl);
                        mGroupNameModelList.add(groupNameModel);
                    }
                    Log.e("DataSnapshot", ds.toString());
                }
                if (mGroupNameModelList.size() > 0)
                    setGroupUserToAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("databaseError", databaseError.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            Utility.saveCurrentUser(this, "Email", false);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.action_groupCreate) {
            Intent intent = new Intent(UserListActivity.this, CreateGroupActivity.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
