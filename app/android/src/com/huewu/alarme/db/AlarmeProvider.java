package com.huewu.alarme.db;

import java.util.ArrayList;

import com.huewu.alarme.model.AlarmInfo;

public class AlarmeProvider {
	
	private static ArrayList<AlarmInfo> mAlarmArray = new ArrayList<AlarmInfo>();

	//provide helper methods to get/set persistent data.
	public static AlarmInfo[] loadAlarms() {
		return mAlarmArray.toArray(new AlarmInfo[]{});
	}
	
	public static void addAlarm( AlarmInfo[] alamrs ){
		
	}
	
	public static void backupAlarms( AlarmInfo[] alarms ){
	}

}//end of class
