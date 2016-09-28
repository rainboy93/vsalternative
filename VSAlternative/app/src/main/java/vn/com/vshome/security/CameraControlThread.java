package vn.com.vshome.security;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.fos.sdk.FosSdkJNI;
import com.fos.sdk.PtzCmd;

import java.io.IOException;
import java.util.Arrays;

import vn.com.vshome.communication.SocketManager;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Logger;


/**
 * Created by anlab on 7/6/16.
 */
public class CameraControlThread extends Thread{

    private final int SEND_MESSAGE = 1;
    private final String HANDLER = "Handler";
    private final String CMD = "Command";

    private static CameraControlThread cameraControlThread;

    public static CameraControlThread getInstance(){
        if(cameraControlThread == null){
            cameraControlThread = new CameraControlThread();
        }
        if(!cameraControlThread.isAlive()){
            cameraControlThread.start();
        }
        return cameraControlThread;
    }

    public Handler mHandler;

    public void sendControl(int handler, int cmd){
        if(mHandler == null){
            return;
        }
        Message message = new Message();
        message.what = SEND_MESSAGE;
        Bundle bundle = new Bundle();
        bundle.putInt(HANDLER, handler);
        bundle.putInt(CMD, cmd);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    public void stopRunning(){
        interrupt();
    }

    @Override
    public void run() {
        Looper.prepare();

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == SEND_MESSAGE){
                    Bundle bundle = msg.getData();
                    if(bundle != null){
                        int handler = bundle.getInt(HANDLER);
                        int cmd = bundle.getInt(CMD);
                        FosSdkJNI.PtzCmd(handler, cmd, 1000);
                    }
                } else {
                    super.handleMessage(msg);
                }
            }
        };

        Looper.loop();
    }
}
