package com.ithena.krishna.ithenaMusic;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.RefWatcher;



public class IthenaMusicApplication extends Application {

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        IthenaMusicApplication application = (IthenaMusicApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

}
