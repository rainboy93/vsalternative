package vn.com.vshome.database;

import com.orm.SugarRecord;

import vn.com.vshome.database.model.CameraDevice;

/**
 * Created by rAinmAker on 06/07/2016.
 */
public class TblCamera extends SugarRecord {
    public int room_id;
    public int device_type;
    public String device_name;
    public String ip_address;
    public String dns_address;
    public int stream_type;
    public int web_port;
    public int local_port;
    public String uid;
    public String username;
    public String password;

    public void setCamera(CameraDevice device) {
        this.room_id = device.room_id;
        this.device_type = device.device_type;
        this.device_name = device.device_name;
        this.ip_address = device.ip_address;
        this.dns_address = device.dns_address;
        this.stream_type = device.stream_type;
        this.web_port = device.web_port;
        this.local_port = device.local_port;
        this.uid = device.uid;
        this.username = device.username;
        this.password = device.password;
    }
}
