package vn.com.vshome;

import android.app.Application;

import com.orm.SugarApp;

import vn.com.vshome.networks.SocketManager;

/**
 * Created by anlab on 7/4/16.
 */
public class VSHome extends SugarApp{
    public static SocketManager socketManager;

    public static int userPriority = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        if(socketManager == null){
            socketManager = new SocketManager();
        }
    }
}
