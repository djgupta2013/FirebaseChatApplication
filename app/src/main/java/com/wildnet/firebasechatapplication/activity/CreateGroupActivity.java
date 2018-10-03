package com.wildnet.firebasechatapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wildnet.firebasechatapplication.R;
import com.wildnet.firebasechatapplication.adapter.NewGroupCreateAdapter;
import com.wildnet.firebasechatapplication.model.GroupUserModel;
import com.wildnet.firebasechatapplication.model.UserModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity implements NewGroupCreateAdapter.CheckBoxData {

    public List<UserModel> userList;
    String userEmail, userName, userImageUrl;
    List<UserModel> selectUserList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager RecyclerViewLayoutManager;
    private Intent intent;
    private GroupUserModel groupUserModel;
    private NewGroupCreateAdapter mNewGroupCreateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        getSupportActionBar().hide();
        intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");
        selectUserList = new ArrayList<>();
        groupUserModel = new GroupUserModel();
        findViewById();
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        getAllUser();
    }

    private void findViewById() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_Newgroup);
    }

    private void getAllUser() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                UserModel userModel;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String emailCheck = ds.child("email").getValue(String.class);
                    if (emailCheck.equalsIgnoreCase(userEmail)) {
                        Log.e("userEmail", userEmail);
                        userName = ds.child("name").getValue(String.class);
                        userImageUrl = ds.child("userImageUrl").getValue(String.class);
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

    private void setDataToAdapter() {
        mNewGroupCreateAdapter = new NewGroupCreateAdapter(this, userList, userEmail);
        mNewGroupCreateAdapter.setListener(this);
        recyclerView.setAdapter(mNewGroupCreateAdapter);
    }

    @Override
    public void checkData(int position, boolean isCheck) {
        userList.get(position).setSelected(isCheck);
    }

    public void proceedData(View v) {
        List<GroupUserModel> selectedGroupUserList = new ArrayList<>();
        for (UserModel model : userList) {
            Log.e("model", model.toString());
            if (model.isSelected()) {
                groupUserModel = new GroupUserModel(model.getEmail(), "", model.getName(), model.getUrl());
                selectedGroupUserList.add(groupUserModel);
            }
        }
        if (selectedGroupUserList.size() > 0) {
            groupUserModel = new GroupUserModel(userEmail, "admin", userName, userImageUrl);
            selectedGroupUserList.add(groupUserModel);
            Intent intent = new Intent(CreateGroupActivity.this, GroupNameActivity.class);
            intent.putExtra("userList", (Serializable) selectedGroupUserList);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Please select atleast one user...", Toast.LENGTH_SHORT).show();
        }

    }
}
