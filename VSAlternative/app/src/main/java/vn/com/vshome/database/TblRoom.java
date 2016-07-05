package vn.com.vshome.database;

import com.orm.SugarRecord;

import vn.com.vshome.database.model.Room;

/**
 * Created by rAinmAker on 06/07/2016.
 */
public class TblRoom extends SugarRecord {
    public int floorID;
    public String name;

    public void setRoom(Room room) {
        setId((long) room.id);
        this.floorID = room.floorID;
        this.name = room.name;
    }

}
