package vn.com.vshome;

import android.app.Activity;
import android.content.Intent;

import com.orm.SugarApp;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration;
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;
import com.zplesac.connectionbuddy.models.ConnectivityState;

import vn.com.vshome.communication.SocketManager;
import vn.com.vshome.database.User;
import vn.com.vshome.utils.BackgroundManager;

/**
 * Created by anlab on 7/4/16.
 */
public class VSHome extends SugarApp implements BackgroundManager.Listener, ConnectivityChangeListener {
    public static SocketManager socketManager;
    public static Activity activity;
    public static User currentUser;

    private BackgroundManager backgroundManager;

    @Override
    public void onCreate() {
        super.onCreate();

        ConnectionBuddyConfiguration configuration = new ConnectionBuddyConfiguration.Builder(this).build();
        ConnectionBuddy.getInstance().init(configuration);
        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, true, this);

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
//        if (socketManager != null) {
//            socketManager.destroySocket();
//            socketManager = null;
//        }
//        restart();
    }

    public static void restart() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void onConnectionChange(ConnectivityEvent event) {
        if(event.getState() == ConnectivityState.CONNECTED){

        } else {

        }
    }
}
