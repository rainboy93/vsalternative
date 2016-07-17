package vn.com.vshome.database;

import com.orm.SugarRecord;

public class DeviceState extends SugarRecord{

	public int state = 1;
	public int param = 0;				// param: % dimer from 0 to 100%  // for pir: param = ldr_state; | mode for rgb
	public int param1 = 0;				// red for rgb
	public int param2 = 0;				// green for rgb
	public int param3 = 0;				// blue for rgb
}
