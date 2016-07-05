package vn.com.vshome.database;

import com.orm.SugarRecord;

import vn.com.vshome.database.model.LightingScene;

/**
 * Created by rAinmAker on 06/07/2016.
 */
public class TblScene extends SugarRecord {
    public int room_id;
    public String name;
    public int state;
    public int schedule;
    public int hour;
    public int minute;
    public int monday;
    public int tuesday;
    public int wednesday;
    public int thursday;
    public int friday;
    public int saturday;
    public int sunday;

    public void setScene(LightingScene scene) {
        this.room_id = scene.room_id;
        this.name = scene.name;
        this.state = scene.state;
        this.schedule = scene.schedule;
        this.hour = scene.hour;
        this.minute = scene.minute;
        this.monday = scene.monday;
        this.tuesday = scene.tuesday;
        this.wednesday = scene.wednesday;
        this.thursday = scene.thursday;
        this.friday = scene.friday;
        this.saturday = scene.saturday;
        this.sunday = scene.sunday;
    }
}
