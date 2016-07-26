package vn.com.vshome.database;

import com.orm.SugarRecord;


/**
 * Created by rAinmAker on 06/07/2016.
 */
public class Scene extends SugarRecord {
    public int roomId;
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

    public static final int SCHEDULE_ON = 2;
    public static final int SCHEDULE_OFF = 1;
    public static final int DAY_ON = 2;
    public static final int DAY_OFF = 1;
}
