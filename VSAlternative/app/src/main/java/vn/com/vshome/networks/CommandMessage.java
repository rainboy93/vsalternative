package vn.com.vshome.networks;

import java.util.ArrayList;

import vn.com.vshome.database.LightingDevice;
import vn.com.vshome.database.Scene;
import vn.com.vshome.database.User;
import vn.com.vshome.utils.Define;

/**
 * Created by anlab on 7/5/16.
 */
public class CommandMessage {

    public static final int STATUS_OK = 17;
    public static final int STATUS_ERROR = 19;

    public static final int CMD_NOTHING = 0;
    public static final int CMD_ADD_NEW_USER = 1;
    public static final int CMD_UPDATE_USER_PASSWORD = 2;
    public static final int CMD_LOGIN = 3;
    public static final int CMD_UPDATE_USER_PRIORITY = 4;
    public static final int CMD_UPDATE_USER_STATUS = 5;
    public static final int CMD_ERROR_MESSAGE = 6;
    public static final int CMD_DELETE_USER = 7;
    public static final int CMD_GET_USER_LIST = 8;
    public static final int CMD_DISCONNECT_ALL_USER = 9;

    public static final int CMD_LIGHTING_CONTROL = 10;
    public static final int CMD_LIGHTING_CONFIG = 11;
    public static final int CMD_LIGHTING_UPDATE = 12;
    public static final int CMD_SCENE_CREATE = 13;
    public static final int CMD_SCENE_EDIT = 14;
    public static final int CMD_SCENE_DELETE = 15;
    public static final int CMD_SCENE_CONFIG = 16;
    public static final int CMD_SCHEDULE_UPDATE = 17;
    public static final int CMD_UPDATE_USER_ROOM = 18;
    public static final int CMD_HEART_BEAT = 19;
    public static final int CMD_SCENE_UPDATE = 20;
    public static final int CMD_COMPLETE_CONFIG = 21;

    public int cmd;
    public int[] data = new int[250];
    public String str1;
    public String str2;
    public String str3;

    public CommandMessage() {
        this.cmd = 0;
        for (int i = 0; i < 250; i++)
            this.data[i] = 0;
        this.str1 = new String();
        this.str2 = new String();
        this.str3 = new String();
    }

    public CommandMessage(int cmd) {
        this.cmd = cmd;
        for (int i = 0; i < 250; i++)
            this.data[i] = 0;
        this.str1 = new String();
        this.str2 = new String();
        this.str3 = new String();
    }

    public void setLoginMessage(String username, String password) {
        this.cmd = CMD_LOGIN;
        this.str1 = new String(username);
        this.str2 = new String(password);
    }

    public void setControlMessage(LightingDevice device) {
        ArrayList<LightingDevice> devices = new ArrayList<>();
        devices.add(device);
        setControlMessage(devices, true);
    }

    public void setControlMessage(ArrayList<LightingDevice> devices, boolean isControl) {
        int numberOfDevice = devices.size();
        if (isControl) {
            for (int i = 0; i < 250; i++)
                this.data[i] = 0;
            this.cmd = CMD_LIGHTING_CONTROL;
            data[0] = 1;
            data[1] = numberOfDevice;
        }
        for (int i = 0; i < numberOfDevice; i++) {
            LightingDevice device = devices.get(i);
            int id = device.getId().intValue();
            data[10 * (i + 1) + 0] = (byte) (id >> 8);
            data[10 * (i + 1) + 1] = (byte) (id & 0xFF);
            int type = device.typeId;
            data[10 * (i + 1) + 2] = (byte) type;
            data[10 * (i + 1) + 3] = (byte) device.lcId;
            data[10 * (i + 1) + 4] = (byte) device.devId;
            data[10 * (i + 1) + 5] = (byte) device.channel;
            int state = device.deviceState.state;
            int param = device.deviceState.param;
            int param1 = device.deviceState.param1;
            int param2 = device.deviceState.param2;
            int param3 = device.deviceState.param3;
            switch (type) {
                case Define.DEVICE_TYPE_RELAY:
                    if (state == Define.STATE_ON) {
                        data[10 * (i + 1) + 6] = (byte) Define.COMMAND_TURN_ON;
                    } else if (state == Define.STATE_OFF) {
                        data[10 * (i + 1) + 6] = (byte) Define.COMMAND_TURN_OFF;
                    }
                    break;
                case Define.DEVICE_TYPE_DIMMER:
                    if (state == Define.STATE_ON) {
                        data[10 * (i + 1) + 6] = (byte) Define.COMMAND_TURN_ON;
                        data[10 * (i + 1) + 7] = (byte) 100;
                    } else if (state == Define.STATE_OFF) {
                        data[10 * (i + 1) + 6] = (byte) Define.COMMAND_TURN_OFF;
                        data[10 * (i + 1) + 7] = (byte) 0;
                    } else if (state == Define.COMMAND_SET_PARAM) {
                        data[10 * (i + 1) + 6] = (byte) Define.COMMAND_SET_PARAM;
                        data[10 * (i + 1) + 7] = (byte) param;
                    }
                    break;
                case Define.DEVICE_TYPE_PIR:
                case Define.DEVICE_TYPE_WIR:
                    if (state == Define.STATE_ENABLE) {
                        data[10 * (i + 1) + 6] = (byte) Define.COMMAND_ENABLE;
                    } else if (state == Define.STATE_DISBALE) {
                        data[10 * (i + 1) + 6] = (byte) Define.COMMAND_DISABLE;
                    }
                    break;
                case Define.DEVICE_TYPE_SHUTTER_RELAY:
                    data[10 * (i + 1) + 6] = (byte) state;
                    if (state == Define.COMMAND_OPEN) {
                        data[10 * (i + 1) + 7] = (byte) 100;
                    } else if (state == Define.COMMAND_CLOSE) {
                        data[10 * (i + 1) + 7] = (byte) 0;
                    } else if (state == Define.COMMAND_SET_PARAM) {
                        data[10 * (i + 1) + 7] = (byte) param;
                    } else if (state == Define.COMMAND_STOP) {
                        data[10 * (i + 1) + 7] = (byte) param;
                    }
                    break;
                case Define.DEVICE_TYPE_RGB:
                    if (state == Define.STATE_ON) {
                        data[10 * (i + 1) + 6] = (byte) Define.COMMAND_TURN_ON;
                    } else if (state == Define.STATE_OFF) {
                        data[10 * (i + 1) + 6] = (byte) Define.COMMAND_TURN_OFF;
                    } else if (state == Define.STATE_PARAM) {
                        data[10 * (i + 1) + 6] = (byte) Define.COMMAND_SET_PARAM;
                    }
                    data[10 * (i + 1) + 7] = (byte) param1;
                    data[10 * (i + 1) + 8] = (byte) param2;
                    data[10 * (i + 1) + 9] = (byte) param3;
                    break;
                default:
                    break;
            }
        }
    }

    public void setDeleteScene(int sceneID) {
        this.cmd = CMD_SCENE_DELETE;
        for (int i = 0; i < 250; i++)
            data[i] = 0;
        data[0] = (byte) 1;
        data[1] = (byte) (sceneID >> 16);
        data[2] = (byte) (sceneID >> 8);
        data[3] = (byte) (sceneID);
    }

    public void setEditScene(Scene scene, ArrayList<LightingDevice> devices) {
        int sceneID = scene.getId().intValue();
        this.cmd = CMD_SCENE_EDIT;
        for (int i = 0; i < 250; i++)
            data[i] = 0;
        data[0] = (byte) 1;
        data[1] = (byte) (sceneID >> 16);
        data[2] = (byte) (sceneID >> 8);
        data[3] = (byte) (sceneID);
        data[4] = (byte) scene.roomId;
        data[5] = (byte) scene.schedule;
        data[6] = (byte) scene.hour;
        data[7] = (byte) scene.minute;
        int numberOfDevice = devices.size();
        data[8] = (byte) numberOfDevice;
        data[9] = (byte) getWeekDays(scene);
        setControlMessage(devices, false);
    }

    private int getWeekDays(Scene scene) {
        int WeekDays = 0;
        if (scene.monday == Scene.DAY_ON)
            WeekDays = WeekDays + (1 << 0);
        if (scene.tuesday == Scene.DAY_ON)
            WeekDays = WeekDays + (1 << 1);
        if (scene.wednesday == Scene.DAY_ON)
            WeekDays = WeekDays + (1 << 2);
        if (scene.thursday == Scene.DAY_ON)
            WeekDays = WeekDays + (1 << 3);
        if (scene.monday == Scene.DAY_ON)
            WeekDays = WeekDays + (1 << 4);
        if (scene.monday == Scene.DAY_ON)
            WeekDays = WeekDays + (1 << 5);
        if (scene.monday == Scene.DAY_ON)
            WeekDays = WeekDays + (1 << 6);
        return WeekDays;
    }

    public void setCreateScene(Scene scene, ArrayList<LightingDevice> devices) {
        this.cmd = CMD_SCENE_CREATE;
        for (int i = 0; i < 250; i++)
            data[i] = 0;
        data[0] = (byte) 1;
        data[1] = (byte) scene.roomId;
        data[2] = (byte) scene.schedule;
        data[3] = (byte) scene.hour;
        data[4] = (byte) scene.minute;
        int numberOfDevice = devices.size();
        data[5] = (byte) numberOfDevice;
        data[6] = (byte) getWeekDays(scene);
        setControlMessage(devices, false);
    }

    public void setSceneSchedule(Scene scene) {
        int sceneID = scene.getId().intValue();
        this.cmd = CMD_SCHEDULE_UPDATE;
        for (int i = 0; i < 250; i++)
            data[i] = 0;
        data[0] = (byte) 1;
        data[1] = (byte) (sceneID >> 16);
        data[2] = (byte) (sceneID >> 8);
        data[3] = (byte) (sceneID);
        int schedule = scene.schedule;
        if (schedule == Scene.SCHEDULE_OFF) {
            data[4] = (byte) Scene.SCHEDULE_ON;
        } else {
            data[4] = (byte) Scene.SCHEDULE_OFF;
        }
    }

    public void setDeleteUser(String username) {
        this.cmd = CMD_DELETE_USER;
        this.str1 = new String(username);
    }

    public void setCreateUser(User user, int[] roomInt) {
        this.cmd = CMD_ADD_NEW_USER;
        this.str1 = new String(user.username);
        this.str2 = new String(user.password);
        this.data[0] = user.priority;
        this.data[1] = user.status;
        for (int i = 2; i <= 11; i++) {
            this.data[i] = roomInt[i - 2];
        }
    }

    public void setChangeUserStatus(User user) {
        this.cmd = CMD_UPDATE_USER_STATUS;
        this.str1 = new String(user.username);
        if (user.status == Define.USER_STATUS_ENABLE) {
            data[0] = Define.USER_STATUS_DISABLE;
        } else {
            data[0] = Define.USER_STATUS_ENABLE;
        }
    }

    public byte[] getByteArray() {
        int i = 0;
        byte[] b = new byte[401];

        b[0] = (byte) this.cmd;
        for (i = 1; i <= 250; i++)
            b[i] = (byte) this.data[i - 1];

        byte[] temp1 = this.str1.getBytes();
        for (i = 0; i < temp1.length; i++)
            b[i + 251] = temp1[i];
        for (i = temp1.length; i < 50; i++)
            b[i + 251] = '\0';

        byte[] temp2 = this.str2.getBytes();
        for (i = 0; i < temp2.length; i++)
            b[i + 301] = temp2[i];
        for (i = temp2.length; i < 50; i++)
            b[i + 301] = '\0';

        byte[] temp3 = this.str3.getBytes();
        for (i = 0; i < temp3.length; i++)
            b[i + 351] = temp3[i];
        for (i = temp3.length; i < 50; i++)
            b[i + 351] = '\0';

        return b;
    }
}
