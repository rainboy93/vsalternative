package vn.com.vshome;

import android.app.Activity;
import android.content.Intent;

import com.orm.SugarApp;

import vn.com.vshome.communication.SocketManager;
import vn.com.vshome.database.User;
import vn.com.vshome.utils.BackgroundManager;

/**
 * Created by anlab on 7/4/16.
 */
public class VSHome extends SugarApp implements BackgroundManager.Listener {
    public static SocketManager socketManager;
    public static Activity activity;
    public static User currentUser;

    private BackgroundManager backgroundManager;

    @Override
    public void onCreate() {
        super.onCreate();
        currentUser = new User();
        if (socketManager == null) {
            socketManager = new SocketManager();
        }

        if (backgroundManager == null) {
            backgroundManager = BackgroundManager.get(this);
        }
        backgroundManager.registerListener(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onBecameForeground() {

    }

    @Override
    public void onBecameBackground() {
        if (socketManager != null) {
            socketManager.destroySocket();
            socketManager = null;
        }
        restart();
    }

    public static void restart() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
