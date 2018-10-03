package com.wildnet.firebasechatapplication.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wildnet.firebasechatapplication.R;
import com.wildnet.firebasechatapplication.model.UserLoginSignupModel;
import com.wildnet.firebasechatapplication.utils.Constants;
import com.wildnet.firebasechatapplication.utils.Utility;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup_activity extends AppCompatActivity implements View.OnClickListener {

    public static String userImageUrl = "";
    private static int RESULT_LOAD_IMAGE = 1;
    TextInputEditText passwordt, emailt, namet, confirmPasswordt;
    CircleImageView userImage;
    Button signUp;
    private UserLoginSignupModel signUpModel;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        namet = findViewById(R.id.etNameID);
        emailt = findViewById(R.id.etEmailID);
        passwordt = findViewById(R.id.et_passwordSignup);
        confirmPasswordt = findViewById(R.id.etConfirmPassword);
        signUp = findViewById(R.id.btn_registration);
        userImage = findViewById(R.id.ImageSend);
        userImage.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_registration: {
                String name = namet.getText().toString().trim();
                String email = emailt.getText().toString().trim();
                String password = passwordt.getText().toString().trim();
                String confirmPassword = confirmPasswordt.getText().toString().trim();

                if (!name.equalsIgnoreCase("") && !email.equalsIgnoreCase("")
                        && !password.equalsIgnoreCase("") && !confirmPassword.equalsIgnoreCase("")) {
                    if (Utility.isValidEmail(email)) {
                        if (password.length() >= 8) {
                            if (password.equals(confirmPassword)) {
                                progressBar = new ProgressDialog(Signup_activity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                progressBar.setMessage("Loading......");
                                progressBar.setCancelable(false);
                                progressBar.show();
                                //String userImageUrl= Utility.getUserProfileUrl(this);
                                String imageUrl = Utility.getSignUpUserImage(this);
                                signUpModel = new UserLoginSignupModel(name, email, password, imageUrl, "online");
                                saveData(signUpModel);
                            } else {
                                passwordt.setError("Password is not match");
                                confirmPasswordt.setError("Password is not match");
                            }
                        } else {
                            passwordt.setError("Password must be 4 digit number");
                        }
                    } else {
                        emailt.setError("Please enter valid email");
                    }

                } else {
                    Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.ImageSend: {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        }
    }

    public void saveData(final UserLoginSignupModel signUpModel) {
        mAuth.createUserWithEmailAndPassword(signUpModel.getEmail(), signUpModel.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference();
                String email = signUpModel.getEmail();
                email = email.replace(".", "");
                myRef.child("User").child(email).setValue(signUpModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Utility.saveCurrentUser(Signup_activity.this, "Email", true);
                        Utility.saveCurrentUser(Signup_activity.this, Constants.CURRENT_USER_EMAIL, signUpModel.getEmail());
                        Utility.saveCurrentUserName(Signup_activity.this, Constants.CURRENT_USER_NAME, signUpModel.getName());
                        Intent intent = new Intent(Signup_activity.this, UserListActivity.class);
                        progressBar.dismiss();
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.dismiss();
                Toast.makeText(Signup_activity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            CircleImageView imageView = (CircleImageView) findViewById(R.id.profile_image);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            imageView.setImageBitmap(bmp);
            saveImageToFirebase(picturePath);
        }
    }

    public void saveImageToFirebase(String picturePath) {
        Uri file = Uri.fromFile(new File(String.valueOf(picturePath)));
        final StorageReference riversRef = mStorageRef.child(file.getLastPathSegment());

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();
                                Utility.saveCurrentUser(Signup_activity.this, "userImageUrl", downloadUrl);
                                Log.e("onSuccess", "onSuccess " + downloadUrl);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("onFailure", "onFailure");
                            }
                        });
                        // Get a URL to the uploaded content
                        userImageUrl = taskSnapshot.getUploadSessionUri().toString();
                        Log.d("userImageUrl", userImageUrl);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private Bitmap getBitmapFromUri(Uri selectedImage) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(selectedImage, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackPressed();
    }
}
