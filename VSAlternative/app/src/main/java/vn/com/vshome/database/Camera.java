package vn.com.vshome.database;

import com.orm.SugarRecord;

import java.io.Serializable;


/**
 * Created by rAinmAker on 06/07/2016.
 */
public class Camera extends SugarRecord implements Serializable {
    public int roomId;
    public int deviceType;
    public String deviceName;
    public String ipAddress;
    public String dnsAddress;
    public int streamType;
    public int webPort;
    public int localPort;
    public String uid;
    public String username;
    public String password;

    public boolean isAuth;
    public String localStreamLink;
    public String dnsStreamLink;
    public String localDeviceService;
    public String dnsDeviceService;
    public String localMediaService;
    public String dnsMediaService;
    public String localPtzService;
    public String dnsPtzService;
    public String token;
}
