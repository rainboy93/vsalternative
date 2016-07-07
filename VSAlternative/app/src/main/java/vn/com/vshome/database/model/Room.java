package vn.com.vshome.database.model;

import java.util.ArrayList;

/**
 * Created by rAinmAker on 06/07/2016.
 */
public class Room {
    public int id;
    public int floorID;
    public String name;
    public ArrayList<LightingDevice> devices = new ArrayList<>();
    public ArrayList<CameraDevice> foscams = new ArrayList<>();
}
