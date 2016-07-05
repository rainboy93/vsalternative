package vn.com.vshome.networks;

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

    public void setLoginMessage(String username, String password){
        this.cmd = CMD_LOGIN;
        this.str1 = new String(username);
        this.str2 = new String(password);
    }

    public byte[] getByteArray() {
        int i;
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
