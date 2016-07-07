package vn.com.vshome.communication;

import java.io.IOException;
import java.util.Arrays;

import vn.com.vshome.VSHome;
import vn.com.vshome.callback.LoginCallback;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.networks.ReturnMessage;
import vn.com.vshome.utils.Logger;

/**
 * Created by anlab on 7/6/16.
 */
public class ReceiveThread extends Thread {

    private boolean isRunning = true;
    private byte[] data = new byte[402];
    private LoginCallback loginCallback;

    public void setOnLoginSuccessCallback(LoginCallback callback){
        this.loginCallback = callback;
    }

    public void stopRunning(){
        isRunning = false;
        interrupt();
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                int len = VSHome.socketManager.inputStream.read(data);
                if (len == -1) {
                    isRunning = false;

                    break;
                }
                handleMessage();
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
    }

    private void handleMessage() {
        synchronized (data) {
            Logger.LogD(Arrays.toString(data));
            ReturnMessage ret = new ReturnMessage(data);
            switch (ret.cmd) {
                case CommandMessage.CMD_ADD_NEW_USER:
                    handleAddNewUser(ret);
                    break;
                case CommandMessage.CMD_UPDATE_USER_PASSWORD:
                    handleUpdatePassword(ret);
                    break;
                case CommandMessage.CMD_UPDATE_USER_PRIORITY:
                    handleUpdatePriority(ret);
                    break;
                case CommandMessage.CMD_UPDATE_USER_STATUS:
                    handleUpdateUserStatus(ret);
                    break;
                case CommandMessage.CMD_ERROR_MESSAGE:

                    break;
                case CommandMessage.CMD_DELETE_USER:
                    handleDeleteUser(ret);
                    break;
                case CommandMessage.CMD_GET_USER_LIST:
                    handleGetUserList(ret);
                    break;
                case CommandMessage.CMD_LIGHTING_CONTROL:
                    handleLightingControl(ret);
                    break;
                case CommandMessage.CMD_LIGHTING_CONFIG:
                    handleLightingConfig(ret);
                    break;
                case CommandMessage.CMD_LIGHTING_UPDATE:
                    handleLightingUpdate(ret);
                    break;
                case CommandMessage.CMD_SCENE_CREATE:
                    handleSceneCreate(ret);
                    break;
                case CommandMessage.CMD_SCENE_EDIT:
                    handleSceneEdit(ret);
                    break;
                case CommandMessage.CMD_SCENE_DELETE:
                    handleSceneDelete(ret);
                    break;
                case CommandMessage.CMD_SCENE_CONFIG:
                    handleSceneConfig(ret);
                    break;
                case CommandMessage.CMD_SCHEDULE_UPDATE:
                    handleSceneScheduleUpdate(ret);
                    break;
                case CommandMessage.CMD_UPDATE_USER_ROOM:
                    handleUpdateUserRoom(ret);
                    break;
                case CommandMessage.CMD_HEART_BEAT:
                    handleHeartBeat(ret);
                    break;
                case CommandMessage.CMD_SCENE_UPDATE:
                    handleSceneUpdate(ret);
                    break;
                case CommandMessage.CMD_COMPLETE_CONFIG:
                    handleCompleteConfig(ret);
                    break;
                default:
                    break;
            }
        }
    }

    private void handleAddNewUser(ReturnMessage ret) {

    }

    private void handleUpdatePassword(ReturnMessage ret) {

    }

    private void handleUpdatePriority(ReturnMessage ret) {

    }

    private void handleUpdateUserStatus(ReturnMessage ret) {

    }

    private void handleDeleteUser(ReturnMessage ret) {

    }

    private void handleGetUserList(ReturnMessage ret) {

    }

    private void handleLightingControl(ReturnMessage ret) {

    }

    private void handleLightingConfig(ReturnMessage ret) {

    }

    private void handleLightingUpdate(ReturnMessage ret) {

    }

    private void handleSceneCreate(ReturnMessage ret) {

    }

    private void handleSceneEdit(ReturnMessage ret) {

    }

    private void handleSceneDelete(ReturnMessage ret) {

    }

    private void handleSceneConfig(ReturnMessage ret) {

    }

    private void handleSceneScheduleUpdate(ReturnMessage ret) {

    }

    private void handleUpdateUserRoom(ReturnMessage ret) {

    }

    private void handleHeartBeat(ReturnMessage ret) {

    }

    private void handleSceneUpdate(ReturnMessage ret) {

    }

    private void handleCompleteConfig(ReturnMessage ret) {
        if(loginCallback != null){
            loginCallback.onLoginSuccess();
        }
    }
}
