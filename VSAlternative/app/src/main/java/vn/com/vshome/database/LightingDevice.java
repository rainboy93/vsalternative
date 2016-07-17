package vn.com.vshome.database;

import com.orm.SugarRecord;


/**
 * Created by rAinmAker on 06/07/2016.
 */
public class LightingDevice extends SugarRecord {
    public int typeId;
    public int roomId;
    public String name;
    public int lcId;
    public int devId;
    public int channel;

    public int stateId;
    public DeviceState deviceState;

    public LightingDevice(){

    }

    public LightingDevice(LightingDevice device){
        setId(device.getId());
        this.typeId = device.typeId;
        this.roomId = device.roomId;
        this.name = device.name;
        this.lcId = device.lcId;
        this.devId = device.devId;
        this.channel = device.stateId;
        this.deviceState = device.deviceState;
    }
}
