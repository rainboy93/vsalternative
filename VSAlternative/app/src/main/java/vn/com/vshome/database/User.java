package vn.com.vshome.database;


import com.orm.SugarRecord;

import java.io.Serializable;

public class User extends SugarRecord implements Serializable{
	public String username;
	public String password;
	public int priority;
	public int status;
	public String roomControl;
}
