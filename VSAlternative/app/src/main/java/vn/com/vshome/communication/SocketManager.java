package vn.com.vshome.communication;

import android.app.Activity;
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
import vn.com.vshome.callback.LoginCallback;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Logger;
import vn.com.vshome.utils.PreferenceDefine;
import vn.com.vshome.utils.PreferenceUtils;

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

    public void startCommunication(LoginCallback callback) {
        receiveThread = new ReceiveThread(VSHome.activity);
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
                socket.connect(socketAddress, 2000);
            } else if (type == 1) {
                socket.connect(socketAddress, 3000);
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
            return true;
        } catch (IOException e) {
//            e.printStackTrace();
            if (type == 0) {
                Logger.LogD("Connect to LAN failed");
            } else if (type == 1) {
                Logger.LogD("Connect to WAN failed");
            }
            Define.NETWORK_TYPE = Define.NetworkType.NotDetermine;
            destroySocket();
            return false;
        }
    }

    public void destroySocket() {
        try {
            heartBeatHandler.removeCallbacks(heartBeatRunnable);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        heartBeatRunnable = null;
        heartBeatHandler = null;

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
    }
}
