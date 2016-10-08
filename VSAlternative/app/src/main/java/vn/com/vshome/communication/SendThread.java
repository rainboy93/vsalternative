package vn.com.vshome.communication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.util.Arrays;

import vn.com.vshome.VSHome;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Logger;


/**
 * Created by anlab on 7/6/16.
 */
public class SendThread extends Thread{

    private final int SEND_MESSAGE = 1;
    private final String MESSAGE = "Message";

    public Handler mHandler;

    public void sendMessage(CommandMessage msg){
        if(mHandler == null){
            return;
        }

        SocketManager.getInstance().stopHeartBeat();
        SocketManager.getInstance().startHeartBeat();

        Message message = new Message();
        message.what = SEND_MESSAGE;
        Bundle bundle = new Bundle();
        bundle.putByteArray(MESSAGE, msg.getByteArray());
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
                        byte[] data = bundle.getByteArray(MESSAGE);
                        Logger.LogD("Send message: " + Arrays.toString(data));
                        try {
                            SocketManager.getInstance().outputStream.write(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    super.handleMessage(msg);
                }
            }
        };

        Looper.loop();
    }
}
