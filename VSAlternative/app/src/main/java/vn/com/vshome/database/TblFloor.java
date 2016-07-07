package vn.com.vshome.database;

import com.orm.SugarRecord;

import vn.com.vshome.database.model.Floor;

/**
 * Created by rAinmAker on 06/07/2016.
 */
public class TblFloor extends SugarRecord {
    public String name;

    public void setFloor(Floor floor) {
        setId((long) floor.id);
        this.name = floor.name;
    }
}
