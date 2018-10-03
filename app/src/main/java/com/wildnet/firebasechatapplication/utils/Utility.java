package com.wildnet.firebasechatapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wildnet.firebasechatapplication.model.UserActiveModel;
import com.wildnet.firebasechatapplication.model.UserChatModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static UserActiveModel userActiveModel = new UserActiveModel();

    public static boolean isValidEmail(CharSequence email) {
        String email_id = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(email_id);

        Matcher match = pattern.matcher(email);
        boolean b = match.matches();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public static void saveCurrentUser(Context mContext, String email, boolean b) {
        addPreferences(mContext, Constants.USER_LOGIN_CHECK, b);
    }

    private static void addPreferences(Context mContext, String userLoginCheck, boolean email) {
        if (mContext != null) {
            SharedPreferences.Editor preference = mContext.getSharedPreferences("Preferences_", Context.MODE_PRIVATE).edit();
            preference.putBoolean(userLoginCheck, email);
            preference.apply();
        }
    }

    public static boolean getPreferences(Context mContext) {
        if (mContext != null) {
            SharedPreferences preference = mContext.getSharedPreferences("Preferences_", Context.MODE_PRIVATE);
            boolean mBoolean = preference.getBoolean(Constants.USER_LOGIN_CHECK, false);
            return mBoolean;
        }
        return false;
    }

    public static void saveCurrentUser(Context mContext, String constant, String email) {
        addPreferences(mContext, constant, email);
    }

    private static void addPreferences(Context mContext, String constant, String email) {
        if (mContext != null) {
            SharedPreferences.Editor preference = mContext.getSharedPreferences("Preferences_", Context.MODE_PRIVATE).edit();
            preference.putString(constant, email);
            preference.apply();
        }
    }

    public static String getUserEmail(Context mContext) {
        if (mContext != null) {
            SharedPreferences preference = mContext.getSharedPreferences("Preferences_", Context.MODE_PRIVATE);
            String email = preference.getString(Constants.CURRENT_USER_EMAIL, "");
            return email;
        }
        return "";
    }

    public static void saveCurrentUserName(Context mContext, String constant, String name) {
        if (mContext != null) {
            SharedPreferences.Editor preference = mContext.getSharedPreferences("Preferences_", Context.MODE_PRIVATE).edit();
            preference.putString(constant, name);
            preference.apply();
        }
    }

    public static String getUserName(Context mContext) {
        if (mContext != null) {
            SharedPreferences preference = mContext.getSharedPreferences("Preferences_", Context.MODE_PRIVATE);
            String name = preference.getString(Constants.CURRENT_USER_NAME, "");
            return name;
        }
        return "";
    }

    public static String getCurrTime() {

        DateFormat df = new SimpleDateFormat(" hh:mm aa");
        String time = df.format(Calendar.getInstance().getTime());
        return time;

    }

    //save user chat
    public static void saveCurrentUserChat(Context mContext, UserChatModel chat) {
        addChatPreferences(mContext, Constants.SAVE_CURRENT_USER_CHAT, chat);
    }

    private static void addChatPreferences(Context mContext, String saveCurrentUserChat, UserChatModel chat) {
        if (mContext != null) {
            SharedPreferences.Editor preference = mContext.getSharedPreferences("Preferences_", Context.MODE_PRIVATE).edit();
            preference.putString(saveCurrentUserChat, "");
            preference.apply();
        }
    }

    //save current user image
    public static void saveSignUpUserImage(Context mContext, String constant, String url) {
        addPreferencesSignUpUserImage(mContext, constant, url);
    }

    private static void addPreferencesSignUpUserImage(Context mContext, String constant, String url) {
        if (mContext != null) {
            SharedPreferences.Editor preference = mContext.getSharedPreferences("Preferences_", Context.MODE_PRIVATE).edit();
            preference.putString(constant, url);
            preference.apply();
        }
    }

    public static String getSignUpUserImage(Context mContext) {
        if (mContext != null) {
            SharedPreferences preference = mContext.getSharedPreferences("Preferences_", Context.MODE_PRIVATE);
            String url = preference.getString("userImageUrl", "");
            return url;
        }
        return "";
    }

    public static String getGroupUserImage(Context mContext) {
        if (mContext != null) {
            SharedPreferences preference = mContext.getSharedPreferences("Preferences_", Context.MODE_PRIVATE);
            String url = preference.getString("groupImageUrl", "");
            return url;
        }
        return "";
    }

    public static void userLastSeen(String userEmail) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("User");
        userActiveModel.setActiveCheck("Last seen " + Utility.getCurrTime());
        String email = userEmail.replace(".", "");
        usersdRef.child(email).child("activeUser").setValue(userActiveModel).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public static void userOnline(String userEmail) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("User");
        userActiveModel.setActiveCheck("online");
        String email = userEmail.replace(".", "");
        usersdRef.child(email).child("activeUser").setValue(userActiveModel).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
}
