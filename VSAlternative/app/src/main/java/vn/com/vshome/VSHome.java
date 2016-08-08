package vn.com.vshome;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.fos.sdk.FosSdkJNI;
import com.orm.SugarApp;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration;
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;
import com.zplesac.connectionbuddy.models.ConnectivityState;

import vn.com.vshome.account.LoginActivity;
import vn.com.vshome.communication.SocketManager;
import vn.com.vshome.database.User;
import vn.com.vshome.utils.Logger;

/**
 * Created by anlab on 7/4/16.
 */
public class VSHome extends SugarApp implements ConnectivityChangeListener,
        Application.ActivityLifecycleCallbacks {
    public static SocketManager socketManager;
    public static Activity activity;
    public static User currentUser;
    public static boolean isTakePhoto = false;
    private final long BACKGROUND_DELAY = 500;
    private final long BACKGROUND_DELAY_WITH_TAKE_IMAGE = 120 * 1000;
    public static boolean isLogIn = false;

    private boolean mInBackground = true;
    private final Handler mBackgroundDelayHandler = new Handler();
    private Runnable mBackgroundTransition;

    @Override
    public void onCreate() {
        super.onCreate();

        FosSdkJNI.Init();

//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread thread, Throwable ex) {
//                handleUncaughtException(thread, ex);
//            }
//        });


        ConnectionBuddyConfiguration configuration = new ConnectionBuddyConfiguration.Builder(this).build();
        ConnectionBuddy.getInstance().init(configuration);
        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, true, this);

        if (socketManager == null) {
            socketManager = new SocketManager();
        }

        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onTerminate() {
        FosSdkJNI.DeInit();
        super.onTerminate();
    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//    }

    public static void finish() {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("EXIT", true);
        activity.startActivity(intent);
    }

    public static void restart() {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    private void handleUncaughtException(Thread thread, Throwable e) {
        Logger.LogD("Exception caught: " + e.getMessage());
        if (socketManager != null) {
            socketManager.destroySocket();
            socketManager = null;
        }
        restart();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    @Override
    public void onConnectionChange(ConnectivityEvent event) {
        if (event.getState() == ConnectivityState.CONNECTED) {

        } else {
            if (socketManager != null) {
                socketManager.destroySocket();
                socketManager = null;
            }
            restart();
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (mBackgroundTransition != null) {
            mBackgroundDelayHandler.removeCallbacks(mBackgroundTransition);
            mBackgroundTransition = null;
        }

        if (mInBackground) {
            mInBackground = false;
            if (isTakePhoto) {
                isTakePhoto = false;
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (!mInBackground && mBackgroundTransition == null) {
            mBackgroundTransition = new Runnable() {
                @Override
                public void run() {
                    mInBackground = true;
                    mBackgroundTransition = null;
//                    if (!isTakePhoto) {
                    if (socketManager != null) {
                        socketManager.destroySocket();
                        socketManager = null;
                    }
                    finish();
//                    }
                }
            };
            if (isTakePhoto) {
                mBackgroundDelayHandler.postDelayed(mBackgroundTransition,
                        BACKGROUND_DELAY_WITH_TAKE_IMAGE);
            } else {
                mBackgroundDelayHandler.postDelayed(mBackgroundTransition,
                        BACKGROUND_DELAY);
            }
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
