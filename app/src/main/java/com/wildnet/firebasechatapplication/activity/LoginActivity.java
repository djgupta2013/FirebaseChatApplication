package com.wildnet.firebasechatapplication.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wildnet.firebasechatapplication.R;
import com.wildnet.firebasechatapplication.model.UserLoginSignupModel;
import com.wildnet.firebasechatapplication.utils.Constants;
import com.wildnet.firebasechatapplication.utils.Utility;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button login,signUp;
    EditText emailEt,passwordEt;
    private UserLoginSignupModel userModel;
    FirebaseAuth mAuth;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        login=findViewById(R.id.btn_login);
        signUp=findViewById(R.id.btn_signUp);
        emailEt=findViewById(R.id.et_emailLogin);
        passwordEt=findViewById(R.id.et_passwordLogin);
        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
        if(Utility.getPreferences(this)){
            Intent intent=new Intent(this,UserListActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login :{
                String email=emailEt.getText().toString().trim();
                String password=passwordEt.getText().toString().trim();
                if(!email.equalsIgnoreCase("")&&!password.equalsIgnoreCase("")) {
                    userModel = new UserLoginSignupModel(email, password);
                    userLogin(userModel);
                }else{
                    Toast.makeText(this, "Please enter all field", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btn_signUp :{
                Intent intent=new Intent(this,Signup_activity.class);
                startActivity(intent);
            }
        }
    }

    private void userLogin(final UserLoginSignupModel userModel) {
        if(Utility.isValidEmail(userModel.getEmail())){
            progressBar=new ProgressDialog(LoginActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            progressBar.setMessage("Loading......");
            progressBar.setCancelable(false);
            progressBar.show();
            mAuth.signInWithEmailAndPassword(userModel.getEmail(), userModel.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Utility.saveCurrentUser(LoginActivity.this,"Email", true);
                    Utility.saveCurrentUser(LoginActivity.this, Constants.CURRENT_USER_EMAIL, userModel.getEmail());
                    Intent intent=new Intent(LoginActivity.this,UserListActivity.class);
                    progressBar.dismiss();
                    startActivity(intent);
                    finish();
                   // Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            emailEt.setError("Please enter valid email");
        }
    }
}
