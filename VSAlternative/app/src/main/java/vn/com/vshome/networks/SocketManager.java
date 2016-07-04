package vn.com.vshome.networks;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.PreferenceDefine;
import vn.com.vshome.utils.PreferenceUtils;

/**
 * Created by anlab on 7/4/16.
 */
public class SocketManager {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public boolean checkIfCanConnect(Context context){

        if(socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String address = "";
        int port = 0;
        if(Define.NETWORK_TYPE == Define.NetworkType.LocalNetwork){
            address = PreferenceUtils.getInstance(context).getValue(PreferenceDefine.IP_ADDRESS , "");
            port = PreferenceUtils.getInstance(context).getIntValue(PreferenceDefine.IP_PORT , 0);
        } else  if(Define.NETWORK_TYPE == Define.NetworkType.DnsNetwork){
            address = PreferenceUtils.getInstance(context).getValue(PreferenceDefine.DNS_ADDRESS , "");
            port = PreferenceUtils.getInstance(context).getIntValue(PreferenceDefine.DNS_PORT , 0);
        }

        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(address, port), 2000);
            socket.setSoTimeout(1000);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            destroySocket();
            return false;
        }
    }

    public void destroySocket(){
        if(inputStream != null){
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(outputStream != null){
            try {
                outputStream.close();
                outputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(socket != null){
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
