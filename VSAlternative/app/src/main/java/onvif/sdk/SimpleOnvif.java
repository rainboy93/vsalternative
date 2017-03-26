package onvif.sdk;

import java.util.ArrayList;

import onvif.sdk.obj.Device;
import onvif.sdk.obj.DeviceCapability;
import onvif.sdk.obj.DeviceInfo;
import onvif.sdk.obj.ImagingSetting;
import onvif.sdk.obj.MediaProfilesInfo;
import onvif.sdk.obj.MediaStreamUri;
import onvif.sdk.obj.PTZType;

interface SimpleOnvif {

    ArrayList<Device> discoverDevices();

    DeviceCapability getDeviceCapabilities(String username, String password, String deviceService);

    DeviceInfo getDeviceInfo(String username, String password, String deviceService);

    ArrayList<MediaProfilesInfo> getMediaProfiles(String username, String password, String mediaService);

    ArrayList<MediaStreamUri> getMediaStreamUri(String username, String password, String deviceService);

    ImagingSetting getImagingSetting(String username, String password, String imagingService, String videoSourceToken);

    int setImagingSetting(String username, String password, String imagingService, String videoSourceToken, ImagingSetting imgSetting);

    int ptzContinuousMove(String username, String password, String ptzService, String profileToken, PTZType type, float x, float y, float z);

    int ptzRelativeMove(String username, String password, String ptzService, String profileToken, PTZType type, float x, float y, float z);

    int ptzStop(String username, String password, String ptzService, String profileToken, PTZType type);

}
