package com.huewu.alarme.model;

import java.util.Date;

/**
 * Data class which represent one alarm setting.
 * @author huewu.yang
 *
 */
public class AlarmInfo {
	
	public int id = -1;
	public boolean isGroup = false;
	public long alarmTime = System.currentTimeMillis();
	public String message = "";
	
	//to json
	
	//to content values.

}//end of class
