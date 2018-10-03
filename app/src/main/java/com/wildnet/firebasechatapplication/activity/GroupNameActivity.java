package com.wildnet.firebasechatapplication.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wildnet.firebasechatapplication.R;
import com.wildnet.firebasechatapplication.model.GroupUserModel;
import com.wildnet.firebasechatapplication.utils.Utility;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupNameActivity extends AppCompatActivity implements View.OnClickListener {

    public static String userImageUrl = "";
    private static int RESULT_LOAD_IMAGE = 1;
    List<GroupUserModel> mModel;
    Intent intent;
    private Bundle userList;
    private EditText groupName;
    private CircleImageView groupImageSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_name);
        intent = getIntent();
        userList = new Bundle();
        userList = intent.getExtras();
        groupName = findViewById(R.id.et_groupName);
        groupImageSend = (CircleImageView) findViewById(R.id.groupImageSend);
        groupImageSend.setOnClickListener(this);
        if (userList != null) {
            mModel = (List<GroupUserModel>) userList.getSerializable("userList");
            Log.e("mModel", mModel.toString());
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void createGroup(View view) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("GroupUser");
        String groupNme = groupName.getText().toString().trim();
        if (!groupNme.equals("")) {
            usersdRef.child(groupNme).child("groupMember").setValue(mModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            String groupImageUrl = Utility.getGroupUserImage(this);
            usersdRef.child(groupNme).child("groupImageUrl").setValue(groupImageUrl);

        } else {
            Toast.makeText(this, "Please enter group name", Toast.LENGTH_SHORT).show();
        }
        groupName.setText("");
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

            CircleImageView imageView = (CircleImageView) findViewById(R.id.groupProfile_image);

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
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
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
                                Utility.saveCurrentUser(GroupNameActivity.this, "groupImageUrl", downloadUrl);
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
}
