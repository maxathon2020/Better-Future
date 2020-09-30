package com.eve.spear;

import android.app.Activity;
import android.app.Application;
import android.content.Context;


import com.eve.spear.helper.MaxonrowHelper;
import com.eve.spear.helper.SharedPrefsHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        setContext(getApplicationContext());
        SharedPrefsHelper.newInstance(context);

        MaxonrowHelper.setupBouncyCastle();

        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(Context context) {
        setContext(context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(Activity activity) {
        setContext(activity);
    }


    public static void setContext(Context con) {
        context = con;
    }

    public static Context getContext() {
        return context;
    }

}
