package vn.com.vshome.communication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import vn.com.vshome.VSHome;
import vn.com.vshome.activitymanager.TheActivityManager;
import vn.com.vshome.callback.LoginCallback;
import vn.com.vshome.foscamsdk.CameraManager;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.security.PreviewService;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Logger;
import vn.com.vshome.utils.PreferenceDefine;
import vn.com.vshome.utils.PreferenceUtils;
import vn.com.vshome.utils.Utils;

/**
 * Created by anlab on 7/4/16.
 */
public class SocketManager {
    private final int HEART_BEAT_DELAY = 100;

    private Socket socket;
    public InputStream inputStream;
    public OutputStream outputStream;
    public ReceiveThread receiveThread;
    public SendThread sendThread;

    private static SocketManager socketManager;

    public static SocketManager getInstance() {
        if (socketManager == null) {
            socketManager = new SocketManager();
        }
        return socketManager;
    }

    private CommandMessage heartBeatMessage;

    private Runnable heartBeatRunnable;

    private Handler heartBeatHandler;

    public void startHeartBeat() {
        heartBeatHandler = new Handler(Looper.getMainLooper());
        heartBeatRunnable = new Runnable() {
            @Override
            public void run() {
                Logger.LogD("Send heart beat");
                sendMessage(heartBeatMessage);
                heartBeatHandler.postDelayed(heartBeatRunnable, HEART_BEAT_DELAY * 1000);
            }
        };
        heartBeatMessage = new CommandMessage();
        heartBeatMessage.cmd = 19;
        heartBeatHandler.postDelayed(heartBeatRunnable, HEART_BEAT_DELAY * 1000);
    }

    public void stopHeartBeat(){
        try {
            heartBeatHandler.removeCallbacks(heartBeatRunnable);
        } catch (Exception e){

        }
        heartBeatHandler = null;
        heartBeatRunnable = null;
    }

    public void startCommunication(LoginCallback callback) {
        receiveThread = new ReceiveThread(TheActivityManager.getInstance().getCurrentActivity());
        receiveThread.start();
        sendThread = new SendThread();
        sendThread.start();
        receiveThread.setOnLoginSuccessCallback(callback);
        startHeartBeat();
    }

    public void removeLoginCallback() {
        receiveThread.setOnLoginSuccessCallback(null);
    }

    public void sendMessage(CommandMessage message) {
        if (sendThread != null) {
            sendThread.sendMessage(message);
        }
    }

    public boolean canConnect(Context context, int type) {
        boolean success = false;
        destroySocket();
        String address = "";
        int port = 0;
        if (type == 0) {
            address = PreferenceUtils.getInstance(context).getValue(PreferenceDefine.IP_ADDRESS, "");
            port = PreferenceUtils.getInstance(context).getIntValue(PreferenceDefine.IP_PORT, 0);
        } else if (type == 1) {
            address = PreferenceUtils.getInstance(context).getValue(PreferenceDefine.DNS_ADDRESS, "");
            port = PreferenceUtils.getInstance(context).getIntValue(PreferenceDefine.DNS_PORT, 0);
        }

        try {
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(address, port);
            if (type == 0) {
                socket.connect(socketAddress, 1000);
            } else if (type == 1) {
                socket.connect(socketAddress, 2000);
            }
            socket.setSoTimeout(1000);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            if (type == 0) {
                Define.NETWORK_TYPE = Define.NetworkType.LocalNetwork;
                Logger.LogD("Connect to LAN success");
            } else if (type == 1) {
                Logger.LogD("Connect to WAN success");
                Define.NETWORK_TYPE = Define.NetworkType.DnsNetwork;
            }
            localTry = 3;
            dnsTry = 3;
            return true;
        } catch (IOException e) {
//            e.printStackTrace();
            Define.NETWORK_TYPE = Define.NetworkType.NotDetermine;
            destroySocket();
            if (type == 0 && localTry > 0) {
                Logger.LogD("Connect to LAN failed");
                localTry--;
                return canConnect(context, type);
            } else if (type == 1 && dnsTry > 0) {
                Logger.LogD("Connect to WAN failed");
                return canConnect(context, type);
            } else {
                localTry = 3;
                dnsTry = 3;
                return false;
            }
        }
    }

    private int dnsTry = 3;
    private int localTry = 3;

    public void destroySocket() {
        try {
            Intent intent = new Intent(TheActivityManager.getInstance().getApplication(), PreviewService.class);
            TheActivityManager.getInstance().getApplication().stopService(intent);
        } catch (Exception e){

        }
        stopHeartBeat();

        if (sendThread != null) {
            sendThread.stopRunning();
            sendThread = null;
        }

        if (receiveThread != null) {
            receiveThread.stopRunning();
            receiveThread = null;
        }

        if (inputStream != null) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (outputStream != null) {
            try {
                outputStream.close();
                outputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        CameraManager.getInstance().releaseCamera();
    }
}
