package com.wildnet.firebasechatapplication.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wildnet.firebasechatapplication.R;
import com.wildnet.firebasechatapplication.model.UserModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private Intent intent;
    private ProgressDialog progressBar;
    private CircleImageView userImage;
    private TextView userName, userEmail;
    private ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();
        findViewById();
        intent = getIntent();
        String userEmail = intent.getStringExtra("userEmail");
        progressBar = new ProgressDialog(UserProfileActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressBar.setMessage("Loading......");
        progressBar.setCancelable(false);
        progressBar.show();
        getUserData(userEmail);
    }

    private void findViewById() {
        userImage = (CircleImageView) findViewById(R.id.crv_profileImage);
        userName = findViewById(R.id.tv_userName);
        userEmail = findViewById(R.id.tv_userEmail);
        backArrow = (ImageView) findViewById(R.id.iv_backUserChat);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileActivity.super.onBackPressed();
            }
        });
    }

    private void getUserData(String userEmail) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.child("User").child(userEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = (dataSnapshot.child("name").getValue(String.class));
                String email = (dataSnapshot.child("email").getValue(String.class));
                String userImageUrl = String.valueOf(dataSnapshot.child("userImageUrl").getValue(String.class));
                UserModel userModel = new UserModel(email, name, userImageUrl);
                setUserData(userModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUserData(UserModel userModel) {
        String url = userModel.getUrl();
        if (!url.equalsIgnoreCase("")) {
            Picasso.get().load(url).resize(150, 150).
                    centerCrop().into(userImage, new Callback() {
                @Override
                public void onSuccess() {
                    if (progressBar != null) {
                        if ((progressBar.isShowing())) {
                            progressBar.dismiss();
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    if (progressBar != null) {
                        if ((progressBar.isShowing())) {
                            progressBar.dismiss();
                        }
                    }
                }
            });
        } else {
            if (progressBar != null) {
                if ((progressBar.isShowing())) {
                    progressBar.dismiss();
                }
            }
        }
        userName.setText(userModel.getName());
        userEmail.setText(userModel.getEmail());
    }
}
