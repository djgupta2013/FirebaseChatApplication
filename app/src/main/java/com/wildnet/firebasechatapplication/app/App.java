package com.wildnet.firebasechatapplication.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class App extends MultiDexApplication {
    private static App instance;
    private boolean persitanceEnabled;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        myApp();
        FirebaseInit();
    }

    public void myApp() {
        instance = this;
    }

    private void FirebaseInit() {
        if (!persitanceEnabled) {
            if (!FirebaseApp.getApps(this).isEmpty()) {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                persitanceEnabled = true;
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
