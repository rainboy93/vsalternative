package vn.com.vshome.database;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rAinmAker on 06/07/2016.
 */
public class Floor extends SugarRecord {
    public String name;
    public ArrayList<Room> rooms = new ArrayList<>();

    public List<Room> getListRoom() {
        return Room.find(Room.class, "floor_id = ?", new String(getId() + ""));
    }
}
