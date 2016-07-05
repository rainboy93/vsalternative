package vn.com.vshome.database.model;

import java.util.ArrayList;

/**
 * Created by rAinmAker on 06/07/2016.
 */
public class Floor {
    public int id;
    public String name;
    public ArrayList<Room> rooms = new ArrayList<Room>();
}
