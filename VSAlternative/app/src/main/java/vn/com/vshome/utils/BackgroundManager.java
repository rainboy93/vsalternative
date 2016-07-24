package vn.com.vshome.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import vn.com.vshome.VSHome;

public class BackgroundManager implements
        Application.ActivityLifecycleCallbacks {

    public static final long BACKGROUND_DELAY = 500;

    private static BackgroundManager sInstance;

    public interface Listener {
        void onBecameForeground();

        void onBecameBackground();
    }

    private boolean mInBackground = true;
    private final List<Listener> listeners = new ArrayList<>();
    private final Handler mBackgroundDelayHandler = new Handler();
    private Runnable mBackgroundTransition;

    public static BackgroundManager get(Application app) {
        if (sInstance == null) {
            sInstance = new BackgroundManager(app);
        }
        return sInstance;
    }

    private BackgroundManager(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    public void registerListener(Listener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        listeners.remove(listener);
    }

    public boolean isInBackground() {
        return mInBackground;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (mBackgroundTransition != null) {
            mBackgroundDelayHandler.removeCallbacks(mBackgroundTransition);
            mBackgroundTransition = null;
        }

        if (mInBackground) {
            mInBackground = false;
            notifyOnBecameForeground();
            Log.d("dungnt", "Application went to foreground");
        }
    }

    private void notifyOnBecameForeground() {
        for (Listener listener : listeners) {
            try {
                listener.onBecameForeground();
            } catch (Exception e) {
                Log.d("dungnt", "Listener threw exception!");
            }
        }
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        if (!mInBackground && mBackgroundTransition == null) {
            mBackgroundTransition = new Runnable() {
                @Override
                public void run() {
                    mInBackground = true;
                    mBackgroundTransition = null;
                    notifyOnBecameBackground();
//					if (!VSHome.takePhoto) {
//						try {
//							activity.finishAffinity();
//						} catch (Exception e) {
//						}
//					}
                    if (activity != null) {
                        activity.finishAffinity();
                    }

                    Log.d("dungnt", "Application went to background");
                }
            };
            mBackgroundDelayHandler.postDelayed(mBackgroundTransition,
                    BACKGROUND_DELAY);
        }
    }

    private void notifyOnBecameBackground() {
        for (Listener listener : listeners) {
            try {
                listener.onBecameBackground();
            } catch (Exception e) {
                Log.d("dungnt", "Listener threw exception!");
            }
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        VSHome.activity = activity;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
