package vn.com.vshome;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import com.fos.sdk.FosSdkJNI;
import com.glidebitmappool.GlideBitmapPool;
import com.orm.SugarApp;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration;
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;
import com.zplesac.connectionbuddy.models.ConnectivityState;

import java.io.File;
import java.io.IOException;

import vn.com.vshome.account.LoginActivity;
import vn.com.vshome.activitymanager.TheActivityManager;
import vn.com.vshome.communication.SocketManager;
import vn.com.vshome.database.User;
import vn.com.vshome.utils.FileUtils;
import vn.com.vshome.utils.Logger;
import vn.com.vshome.utils.MiscUtils;
import vn.com.vshome.utils.Utils;

/**
 * Created by anlab on 7/4/16.
 */
public class VSHome extends SugarApp implements ConnectivityChangeListener, ComponentCallbacks2, ComponentCallbacks {
    public static User currentUser;
    public static boolean isTakePhoto = false;
    private final long BACKGROUND_DELAY_WITH_TAKE_IMAGE = 120 * 1000;

    private final Handler mBackgroundDelayHandler = new Handler();
    private Runnable mBackgroundTransition;

    @Override
    public void onCreate() {
        super.onCreate();

        TheActivityManager.getInstance().configure(this);
        MiscUtils.init(getApplicationContext());
        FosSdkJNI.Init();
        GlideBitmapPool.initialize(10 * 1024 * 1024);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
//                handleUncaughtException(ex);
            }
        });

        ConnectionBuddyConfiguration configuration = new ConnectionBuddyConfiguration.Builder(this).build();
        ConnectionBuddy.getInstance().init(configuration);
        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, true, this);
    }

    @Override
    public void onTerminate() {
        FosSdkJNI.DeInit();
        GlideBitmapPool.clearMemory();
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        GlideBitmapPool.trimMemory(level);
        Logger.LogD("Trim memory level " + level);
        if (level == TRIM_MEMORY_COMPLETE || level == TRIM_MEMORY_UI_HIDDEN) {
//            trimMemory();
        }
    }

    private void trimMemory() {
        if (isTakePhoto) {
            mBackgroundTransition = new Runnable() {
                @Override
                public void run() {
                    mBackgroundTransition = null;
                    SocketManager.getInstance().destroySocket();
                    TheActivityManager.getInstance().finishAll();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            };
            mBackgroundDelayHandler.postDelayed(mBackgroundTransition,
                    BACKGROUND_DELAY_WITH_TAKE_IMAGE);
        } else {
            mBackgroundTransition = null;
            SocketManager.getInstance().destroySocket();
            TheActivityManager.getInstance().finishAll();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private void handleUncaughtException(Throwable e) {
        Logger.LogD("Exception caught: " + e.getMessage());
        try {
            FileUtils.stringToFile(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "vshome.debug", e.getMessage());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        SocketManager.getInstance().destroySocket();
        Utils.restart();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    @Override
    public void onConnectionChange(ConnectivityEvent event) {
        if (event.getState() == ConnectivityState.CONNECTED) {

        } else {
            if (!Utils.isAppInBackground(getApplicationContext())) {
                SocketManager.getInstance().destroySocket();
                Utils.restart();
            }
        }
    }
}
