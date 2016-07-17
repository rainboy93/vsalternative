package vn.com.vshome.database;


import com.orm.SugarRecord;

public class User extends SugarRecord{
	public String username;
	public String password;
	public int priority;
	public int status;
	public String roomControl;
}
