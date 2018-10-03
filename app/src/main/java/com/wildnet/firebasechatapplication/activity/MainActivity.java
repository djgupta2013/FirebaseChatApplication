package com.wildnet.firebasechatapplication.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wildnet.firebasechatapplication.R;
import com.wildnet.firebasechatapplication.model.UserLoginSignupModel;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView userName;
    ImageView userProfile;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        userName = findViewById(R.id.tv_userName);
        userProfile = findViewById(R.id.iv_userImage);
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser().getEmail();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        showUserData();
    }

    public void showUserData() {
        String email = mAuth.getCurrentUser().getEmail();
        email = email.replace(".", "");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final String finalEmail = email;
        myRef.child("User").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserLoginSignupModel usermodel = dataSnapshot.getValue(UserLoginSignupModel.class);
                String name = usermodel.getName();
                userName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
