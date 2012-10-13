package com.huewu.alarme.db;

import java.util.ArrayList;

import com.huewu.alarme.model.AlarmInfo;

public class AlarmeProvider {
	
	private static ArrayList<AlarmInfo> mAlarmArray = new ArrayList<AlarmInfo>();

	//provide helper methods to get/set persistent data.
	public static AlarmInfo[] loadAlarms() {
		return mAlarmArray.toArray(new AlarmInfo[]{});
	}
	
	public static void addAlarm( AlarmInfo alarm ){
		mAlarmArray.add(alarm);
	}
	
	public static void backupAlarms( AlarmInfo[] alarms ){
	}

	public static AlarmInfo findAlarm(String aid) {
		for(AlarmInfo alarm : mAlarmArray){
			if(alarm.aid.equals(aid))
				return alarm;
		}
		
		return null;
	}

}//end of class
