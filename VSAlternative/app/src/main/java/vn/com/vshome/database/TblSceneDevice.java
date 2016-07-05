package vn.com.vshome.database;

import com.orm.SugarRecord;

import vn.com.vshome.database.model.LightingSceneDevice;

/**
 * Created by rAinmAker on 06/07/2016.
 */
public class TblSceneDevice extends SugarRecord {
    public int scene_id;
    public int device_id;
    public int state_id;
    public int param;        // mode for rgb
    public int param1;        // red for rgb
    public int param2;        // green for rgb
    public int param3;

    public void setLightingSceneDevice(LightingSceneDevice device) {
        this.scene_id = device.scene_id;
        this.device_id = device.device_id;
        this.state_id = device.state_id;
        this.param = device.param;
        this.param1 = device.param1;
        this.param2 = device.param2;
        this.param3 = device.param3;

    }
}
