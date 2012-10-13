package com.huewu.alarme.model;

import java.net.URLEncoder;

import com.huewu.alarme.util.Util;

/**
 * Data class which represent one public alarm setting.
 * @author huewu.yang
 *
 */
public class AlarmInfo extends JsonModel implements Comparable<AlarmInfo> {
	
	public static final String PRIVATE_ALARM = "PRIVATE";
	public static final String GROUP_ALARM = "GROUP";
	
	public static final String STATUS_OFF = "OFF";
	public static final String STATUS_ON = "ON";
	
	public String aid;
	public String type;
	public String time;
	public String offTime;
	public AlarmMember[] members;
	public String status = STATUS_ON;
	
	private UserInfo mOwner = null;
	
	/**
	 * Provide for GSON Parser. 
	 * Don't recommend to use this constructor directly.
	 * use 'AlarmInfo( UserInfo owner, long alarmTime )
	 */
	public AlarmInfo(){
	}
	
	public AlarmInfo( UserInfo owner, long alarmTimeMs ){
		time = String.valueOf(alarmTimeMs / 1000);
		type = PRIVATE_ALARM;
		members = new AlarmMember[]{ new AlarmMember(owner, AlarmMember.STATUS_ON ) };
		mOwner = owner;
	}
	
	public UserInfo getOwner(){
		return mOwner; 
	}
	
	public long getTime(){
		return Long.valueOf(time) * 1000;
	}
	
	@Override
	public String toPostData() {
		String postStr = "";
		try{
			postStr = String.format("type=%s&time=%s&members[0][uid]=%s&members[0][status]=%s", 
					URLEncoder.encode(type,"utf-8"),
					URLEncoder.encode(time,"utf-8"),
					URLEncoder.encode(members[0].uid,"utf-8"),
					URLEncoder.encode(members[0].status,"utf-8")
					);
		}catch(Exception e){
			return "";
		}
		
		return postStr;
	}

	@Override
	public int compareTo(AlarmInfo another) {
		return (int) (another.getTime() - this.getTime());
	}

}//end of class
