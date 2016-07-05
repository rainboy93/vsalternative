package vn.com.vshome.database.model;

import java.util.ArrayList;

/**
 * Created by rAinmAker on 06/07/2016.
 */
public class Room {
    public int id;
    public int floorID;
    public String name;
    private ArrayList<LightingDevice> devices = new ArrayList<>();
    private ArrayList<CameraDevice> foscams = new ArrayList<>();
}
