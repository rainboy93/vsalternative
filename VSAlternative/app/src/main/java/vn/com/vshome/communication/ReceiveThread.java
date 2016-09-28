package vn.com.vshome.communication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vn.com.vshome.VSHome;
import vn.com.vshome.callback.LightingControlCallback;
import vn.com.vshome.callback.LoginCallback;
import vn.com.vshome.callback.SceneControlCallback;
import vn.com.vshome.callback.SceneSetUpCallback;
import vn.com.vshome.callback.UserControlCallback;
import vn.com.vshome.database.DeviceState;
import vn.com.vshome.database.LightingDevice;
import vn.com.vshome.database.PirConfig;
import vn.com.vshome.database.Scene;
import vn.com.vshome.database.SceneDevice;
import vn.com.vshome.database.User;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.networks.ReturnMessage;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Logger;
import vn.com.vshome.utils.PreferenceDefine;
import vn.com.vshome.utils.PreferenceUtils;
import vn.com.vshome.utils.Utils;

/**
 * Created by anlab on 7/6/16.
 */
public class ReceiveThread extends Thread {

    private boolean isRunning = true;
    private byte[] data = new byte[402];
    private LoginCallback loginCallback;
    private SceneControlCallback sceneCallback;
    private LightingControlCallback lightingCallback;
    private UserControlCallback userCallback;
    private SceneSetUpCallback sceneSetUpCallback;
    public int currentControlDeviceId = -1;
    public int currentUserId = -1;
    public boolean isSceneControl = false;

    public void setOnLoginSuccessCallback(LoginCallback callback) {
        this.loginCallback = callback;
    }

    public void setSceneCallback(SceneControlCallback sceneCallback) {
        this.sceneCallback = sceneCallback;
    }

    public void setSceneSetUpCallback(SceneSetUpCallback sceneSetUpCallback) {
        this.sceneSetUpCallback = sceneSetUpCallback;
    }

    public void setLightingCallback(LightingControlCallback lightingCallback) {
        this.lightingCallback = lightingCallback;
    }

    public void setUserCallback(UserControlCallback userCallback) {
        this.userCallback = userCallback;
    }

    public void stopRunning() {
        isRunning = false;
        interrupt();
    }

    private Context mContext;

    public ReceiveThread(Context context) {
        mContext = context;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                int len = SocketManager.getInstance().inputStream.read(data);
                if (len == -1) {
                    isRunning = false;
                    SocketManager.getInstance().destroySocket();
                    VSHome.restart();
                    break;
                }
                handleMessage();
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
    }

    private void handleMessage() {
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
            case CommandMessage.CMD_DISCONNECT_ALL_USER:
                handleDisconnect(ret);
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

    private void handleDisconnect(ReturnMessage ret) {
        if (userCallback != null) {
            userCallback.onResponse(ret.cmd, ret.status);
        }
    }

    private void handleAddNewUser(ReturnMessage ret) {
        if (userCallback != null) {
            userCallback.onResponse(ret.cmd, ret.status);
        }
    }

    private void handleUpdatePassword(ReturnMessage ret) {
        if (userCallback != null) {
            userCallback.onResponse(ret.cmd, ret.status);
        }
    }

    private void handleUpdatePriority(ReturnMessage ret) {
        if (userCallback != null) {
            userCallback.onResponse(ret.cmd, ret.status);
        }
    }

    private void handleUpdateUserStatus(ReturnMessage ret) {
        if (ret.status == CommandMessage.STATUS_OK) {
            User user = User.findById(User.class, currentUserId);
            if (user != null) {
                if (user.status == Define.USER_STATUS_DISABLE) {
                    user.status = Define.USER_STATUS_ENABLE;
                } else {
                    user.status = Define.USER_STATUS_DISABLE;
                }
                user.save();
            }
        }
        if (userCallback != null) {
            userCallback.onResponse(ret.cmd, ret.status);
        }
    }

    private void handleDeleteUser(ReturnMessage ret) {
        if (userCallback != null) {
            userCallback.onResponse(ret.cmd, ret.status);
        }
    }

    private boolean isReceivingUserList = false;
    private int mUserListCount = 0;

    private void handleGetUserList(ReturnMessage ret) {
        if (!isReceivingUserList) {
            if (ret.status == CommandMessage.STATUS_OK) {
                mUserListCount = ret.data[0];
                isReceivingUserList = true;
            }
            return;
        }

        mUserListCount--;
        User user = new User();
        user.username = ret.str1;
        user.priority = ret.data[0];
        user.status = ret.data[1];
        user.roomControl = toBinary(ret);
        user.save();

        if (mUserListCount == 0) {
            if (userCallback != null) {
                userCallback.onGetUserListDone();
            }
            isReceivingUserList = false;
        }
    }

    private void handleLightingControl(ReturnMessage ret) {
        int deviceId = new Integer(currentControlDeviceId);
        if (lightingCallback != null) {
            lightingCallback.onResponse(deviceId, ret.status);
        }
    }

    private void handleLightingConfig(ReturnMessage ret) {
        int deviceId = new Integer(currentControlDeviceId);
        if (lightingCallback != null) {
            lightingCallback.onResponse(deviceId, ret.status);
        }
    }

    private void handleLightingUpdate(ReturnMessage ret) {
        int numberOfDevice = ret.status;
        for (int i = 0; i < numberOfDevice; i++) {
            int deviceId = ((ret.data[i * 10 + 0]) << 8)
                    + (ret.data[i * 10 + 1]);
            DeviceState state = new DeviceState();
            state.setId((long) deviceId);
            state.state = ret.data[i * 10 + 5];

            if (ret.data[i * 10 + 4] == Define.DEVICE_TYPE_PIR
                    || ret.data[i * 10 + 4] == Define.DEVICE_TYPE_WIR) {
                state.param = 0;
                state.param1 = 0;
                state.param2 = 0;
                state.param3 = 0;

                List<PirConfig> pirConfigs = PirConfig.find(PirConfig.class, "device_id = ?",
                        String.valueOf(deviceId));
                if (pirConfigs != null && pirConfigs.size() > 0) {
                    PirConfig pirConfig = pirConfigs.get(0);
                    pirConfig.ldrStatus = ret.data[i * 10 + 6];
                    pirConfig.ldrThreshold = ret.data[i * 10 + 7];
                    pirConfig.activePeriod = (ret.data[i * 10 + 8] << 8) + ret.data[i * 10 + 9];
                    pirConfig.save();
                } else {
                    PirConfig pirConfig = new PirConfig();
                    pirConfig.deviceId = deviceId;
                    pirConfig.ldrStatus = ret.data[i * 10 + 6];
                    pirConfig.ldrThreshold = ret.data[i * 10 + 7];
                    pirConfig.activePeriod = (ret.data[i * 10 + 8] << 8) + ret.data[i * 10 + 9];
                    pirConfig.save();
                }
            } else if (ret.data[i * 10 + 4] == Define.DEVICE_TYPE_RGB) {
                state.param = ret.data[i * 10 + 6];
                state.param1 = ret.data[i * 10 + 7];
                state.param2 = ret.data[i * 10 + 8];
                state.param3 = ret.data[i * 10 + 9];
            } else {
                state.param = ret.data[i * 10 + 6];
                state.param1 = 0;
                state.param2 = 0;
                state.param3 = 0;
            }
            state.save();
            if (lightingCallback != null) {
                if (deviceId == currentControlDeviceId) {
                    lightingCallback.onControl(deviceId, true);
                } else {
                    lightingCallback.onControl(deviceId, false);
                }
                currentControlDeviceId = -1;
            }
        }
    }

    private void handleSceneCreate(ReturnMessage ret) {
        if (sceneSetUpCallback != null) {
            sceneSetUpCallback.onResponse(ret.status);
        }
    }

    private void handleSceneEdit(ReturnMessage ret) {
        if (sceneSetUpCallback != null) {
            sceneSetUpCallback.onResponse(ret.status);
        }
    }

    private void handleSceneDelete(ReturnMessage ret) {
        if (sceneCallback != null) {
            sceneCallback.onResponse(ret.cmd, ret.status);
        }
    }

    private int countScene = 0;

    private void handleSceneConfig(ReturnMessage ret) {
        int scene_db_version = ret.status;
        int numberOfScene = ret.data[0];
        int sceneID = ((ret.data[1]) << 16)
                + ((ret.data[2]) << 8)
                + (ret.data[3]);
        int roomID = ret.data[4];
        int schedule = ret.data[5];
        int hour = ret.data[6];
        int minute = ret.data[7];
        int num_device = ret.data[8];
        int weekday = ret.data[9];

        String sceneName = new String(ret.str1);
        int monday = Scene.DAY_OFF;
        int tuesday = Scene.DAY_OFF;
        int wednesday = Scene.DAY_OFF;
        int thursday = Scene.DAY_OFF;
        int friday = Scene.DAY_OFF;
        int saturday = Scene.DAY_OFF;
        int sunday = Scene.DAY_OFF;

        if ((weekday & (1 << 0)) == (1 << 0))
            monday = Scene.DAY_ON;
        if ((weekday & (1 << 1)) == (1 << 1))
            tuesday = Scene.DAY_ON;
        if ((weekday & (1 << 2)) == (1 << 2))
            wednesday = Scene.DAY_ON;
        if ((weekday & (1 << 3)) == (1 << 3))
            thursday = Scene.DAY_ON;
        if ((weekday & (1 << 4)) == (1 << 4))
            friday = Scene.DAY_ON;
        if ((weekday & (1 << 5)) == (1 << 5))
            saturday = Scene.DAY_ON;
        if ((weekday & (1 << 6)) == (1 << 6))
            sunday = Scene.DAY_ON;

        Scene scene = new Scene();
        scene.setId((long) sceneID);
        scene.name = sceneName;
        scene.roomId = roomID;
        scene.schedule = schedule;
        scene.hour = hour;
        scene.minute = minute;
        scene.monday = monday;
        scene.tuesday = tuesday;
        scene.wednesday = wednesday;
        scene.thursday = thursday;
        scene.friday = friday;
        scene.saturday = saturday;
        scene.sunday = sunday;

        List<SceneDevice> devices = new ArrayList<>();

        for (int i = 0; i < num_device; i++) {
            SceneDevice device = new SceneDevice();
            int deviceId = (ret.data[10 * (i + 1)] << 8)
                    + (ret.data[10 * (i + 1) + 1]);
            int type = ret.data[10 * (i + 1) + 2];
            int state = ret.data[10 * (i + 1) + 3];
            int param = ret.data[10 * (i + 1) + 4];
            int param1 = ret.data[10 * (i + 1) + 5];
            int param2 = ret.data[10 * (i + 1) + 6];
            int param3 = ret.data[10 * (i + 1) + 7];

            device.sceneId = sceneID;
            device.deviceId = deviceId;
            device.state = state;
            device.param = param;
            device.param1 = param1;
            device.param2 = param2;
            device.param3 = param3;
            filterState(device, type);
            devices.add(device);
        }
        addScene(scene, devices);
        if (countScene == numberOfScene) {
            countScene = 0;
            PreferenceUtils.getInstance(mContext).setValue(PreferenceDefine.SCENE_DB_VERSION, scene_db_version);
        }
        countScene++;
    }

    private void handleSceneScheduleUpdate(ReturnMessage ret) {
        if (sceneCallback != null) {
            sceneCallback.onResponse(ret.cmd, ret.status);
        }
    }

    private void handleUpdateUserRoom(ReturnMessage ret) {
        if (userCallback != null) {
            userCallback.onResponse(ret.cmd, ret.status);
        }
    }

    private void handleHeartBeat(ReturnMessage ret) {

    }

    private void handleSceneUpdate(ReturnMessage ret) {
        int sceneMode = ret.data[0];
        int sceneID = ((ret.data[1]) << 16)
                + ((ret.data[2]) << 8)
                + (ret.data[3]);
        int roomID = ret.data[4];
        int schedule = ret.data[5];
        int hour = ret.data[6];
        int minute = ret.data[7];
        int num_device = ret.data[8];
        int weekday = ret.data[9];

        String sceneName = new String(ret.str1);
        int monday = Scene.DAY_OFF;
        int tuesday = Scene.DAY_OFF;
        int wednesday = Scene.DAY_OFF;
        int thursday = Scene.DAY_OFF;
        int friday = Scene.DAY_OFF;
        int saturday = Scene.DAY_OFF;
        int sunday = Scene.DAY_OFF;

        if ((weekday & (1 << 0)) == (1 << 0))
            monday = Scene.DAY_ON;
        if ((weekday & (1 << 1)) == (1 << 1))
            tuesday = Scene.DAY_ON;
        if ((weekday & (1 << 2)) == (1 << 2))
            wednesday = Scene.DAY_ON;
        if ((weekday & (1 << 3)) == (1 << 3))
            thursday = Scene.DAY_ON;
        if ((weekday & (1 << 4)) == (1 << 4))
            friday = Scene.DAY_ON;
        if ((weekday & (1 << 5)) == (1 << 5))
            saturday = Scene.DAY_ON;
        if ((weekday & (1 << 6)) == (1 << 6))
            sunday = Scene.DAY_ON;

        Scene scene = new Scene();
        scene.setId((long) sceneID);
        scene.name = sceneName;
        scene.roomId = roomID;
        scene.schedule = schedule;
        scene.hour = hour;
        scene.minute = minute;
        scene.monday = monday;
        scene.tuesday = tuesday;
        scene.wednesday = wednesday;
        scene.thursday = thursday;
        scene.friday = friday;
        scene.saturday = saturday;
        scene.sunday = sunday;

        List<SceneDevice> devices = new ArrayList<>();

        for (int i = 0; i < num_device; i++) {
            SceneDevice device = new SceneDevice();
            int deviceId = (ret.data[10 * (i + 1)] << 8)
                    + (ret.data[10 * (i + 1) + 1]);
            int type = ret.data[10 * (i + 1) + 2];
            int state = ret.data[10 * (i + 1) + 3];
            int param = ret.data[10 * (i + 1) + 4];
            int param1 = ret.data[10 * (i + 1) + 5];
            int param2 = ret.data[10 * (i + 1) + 6];
            int param3 = ret.data[10 * (i + 1) + 7];

            device.sceneId = sceneID;
            device.deviceId = deviceId;
            device.state = state;
            device.param = param;
            device.param1 = param1;
            device.param2 = param2;
            device.param3 = param3;
            filterState(device, type);
            devices.add(device);
        }

        if (sceneMode == 2) {
            deleteScene(scene);
        } else {
            addScene(scene, devices);
        }
        PreferenceUtils.getInstance(mContext).setValue(PreferenceDefine.SCENE_DB_VERSION,
                PreferenceUtils.getInstance(mContext).getIntValue(PreferenceDefine.SCENE_DB_VERSION, 0));
        if (sceneCallback != null) {
            sceneCallback.onControl(sceneID, sceneMode);
        }
    }

    private void handleCompleteConfig(ReturnMessage ret) {
        if (loginCallback != null) {
            loginCallback.onLoginSuccess();
        }
    }

    private String toBinary(ReturnMessage ret) {
        StringBuilder sb = new StringBuilder(Byte.SIZE);
        for (int j = 0; j < 10; j++) {
            byte b = (byte) ret.data[j + 2];
            for (int i = Byte.SIZE - 1; i >= 0; i--)
                sb.append((b << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        }
        return sb.toString();
    }

    private void deleteScene(Scene scene) {
        if (scene == null) {
            return;
        }
        scene.delete();
    }

    private void addScene(Scene scene, List<SceneDevice> devices) {
        if (scene == null) {
            return;
        }
        scene.save();
        SceneDevice.deleteAll(SceneDevice.class, "scene_id = ?", new String[]{scene.getId() + ""});
        for (SceneDevice device : devices) {
            device.save();
        }
    }

    private void filterState(SceneDevice device, int type) {
        switch (type) {
            case Define.DEVICE_TYPE_RELAY:
                if (device.state == Define.COMMAND_TURN_ON)
                    device.state = Define.STATE_ON;
                else if (device.state == Define.COMMAND_TURN_OFF)
                    device.state = Define.STATE_OFF;
                device.param = 0;
                device.param1 = 0;
                device.param2 = 0;
                device.param3 = 0;
                break;

            case Define.DEVICE_TYPE_DIMMER:
                if (device.state == Define.COMMAND_TURN_ON)
                    device.state = Define.STATE_ON;
                else if (device.state == Define.COMMAND_TURN_OFF)
                    device.state = Define.STATE_OFF;
                device.param1 = 0;
                device.param2 = 0;
                device.param3 = 0;
                break;

            case Define.DEVICE_TYPE_PIR:
            case Define.DEVICE_TYPE_WIR:
                if (device.state == Define.COMMAND_ENABLE)
                    device.state = Define.STATE_ENABLE;
                else if (device.state == Define.COMMAND_DISABLE)
                    device.state = Define.STATE_DISBALE;
                device.param = 0;
                device.param1 = 0;
                device.param2 = 0;
                device.param3 = 0;
                break;
            case Define.DEVICE_TYPE_SHUTTER_RELAY:
                device.param1 = 0;
                device.param2 = 0;
                device.param3 = 0;
                break;
            case Define.DEVICE_TYPE_RGB:
                if (device.state == Define.COMMAND_TURN_ON
                        || device.state == Define.COMMAND_SET_PARAM)
                    device.state = Define.STATE_PARAM;
                else if (device.state == Define.COMMAND_TURN_OFF)
                    device.state = Define.STATE_OFF;
                break;
        }
    }
}
