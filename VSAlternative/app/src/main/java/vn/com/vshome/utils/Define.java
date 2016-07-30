package vn.com.vshome.utils;

import android.os.Environment;

import java.io.File;

import vn.com.vshome.R;

/**
 * Created by anlab on 7/4/16.
 */
public class Define {

    public static boolean DEBUG = true;

    public static String BASE_IMAGE_PATH = Environment.getExternalStorageDirectory() + File.separator + ".VSHome";

    public static String DEVICE_NOT_AVAILABLE = "Thiết bị chưa được cài đặt";
    public static String IN_DEVELOPING = "Đang phát triển";

    public enum NetworkType {
        NotDetermine,
        LocalNetwork,
        DnsNetwork
    }

    public static NetworkType NETWORK_TYPE = NetworkType.NotDetermine;

    public static final String INTENT_FLOOR_ID = "FloorID";
    public static final String INTENT_ROOM_ID = "RoomID";

    public static final String INTENT_USER = "UserID";
    public static final String INTENT_SCENE_NAME = "SceneName";

    public static final int CODE_SELECT_PICTURE = 1000;
    public static final int CODE_TAKE_PICTURE = 1001;
    public static final int CODE_CROP_PICTURE = 1002;

    public static final int CODE_USER_EDIT = 1003;
    public static final int CODE_USER_CREATE = 1004;

    public static final int STATE_NONE = 0;
    public static final int STATE_OFF = 1;
    public static final int STATE_ON = 2;
    public static final int STATE_PARAM = 3;
    public static final int STATE_STOP = 5;
    public static final int STATE_OPENING = 6;
    public static final int STATE_CLOSING = 7;
    public static final int STATE_ENABLE = 9;
    public static final int STATE_DISBALE = 10;

    public static final int COMMAND_TURN_ON = 1;
    public static final int COMMAND_TURN_OFF = 2;
    public static final int COMMAND_SET_PARAM = 3;
    public static final int COMMAND_OPEN = 4;
    public static final int COMMAND_CLOSE = 5;
    public static final int COMMAND_STOP = 6;
    public static final int COMMAND_ENABLE = 7;
    public static final int COMMAND_DISABLE = 8;
    public static final int COMMAND_TURN_ON_SLOW = 9;
    public static final int COMMAND_TURN_OFF_SLOW = 10;
    public static final int COMMAND_TURN_SET_MODE = 17;
    public static final int COMMAND_SET_THRESHOLD_SLOW = 18;

    public static final int PRIORITY_ADMIN = 1;
    public static final int PRIORITY_USER = 2;

    public static final int USER_STATUS_ENABLE = 1;
    public static final int USER_STATUS_DISABLE = 0;

    public static final int DEVICE_TYPE_PIR = 4;
    public static final int DEVICE_TYPE_WIR = 5;
    public static final int DEVICE_TYPE_RELAY = 6;
    public static final int DEVICE_TYPE_DIMMER = 7;
    public static final int DEVICE_TYPE_SHUTTER_RELAY = 8;
    public static final int DEVICE_TYPE_RGB = 9;
}
