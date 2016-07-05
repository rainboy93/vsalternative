package vn.com.vshome.database;

import com.orm.SugarRecord;

import vn.com.vshome.database.model.LightingDevice;

/**
 * Created by rAinmAker on 06/07/2016.
 */
public class TblLightingDevice extends SugarRecord {
    public int type_id;
    public int room_id;
    public String name;
    public int lc_id;
    public int dev_id;
    public int channel;

    public int state = 1;
    public int param = 0;
    public int param1 = 0;
    public int param2 = 0;
    public int param3 = 0;

    public void setLightingDevice(LightingDevice device) {
        setId((long) device.dev_id);
        this.room_id = device.room_id;
        this.name = device.name;
        this.lc_id = device.lc_id;
        this.channel = device.channel;
    }
}
