package vn.com.vshome.database;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rAinmAker on 06/07/2016.
 */
public class Room extends SugarRecord {
    public int floorID;
    public String name;
    public ArrayList<LightingDevice> devices = new ArrayList<>();
    public ArrayList<Camera> foscams = new ArrayList<>();
}
