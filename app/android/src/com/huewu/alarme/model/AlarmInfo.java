package com.huewu.alarme.model;

import java.net.URLEncoder;

import com.huewu.alarme.util.Util;

/**
 * Data class which represent one public alarm setting.
 * @author huewu.yang
 *
 */
public class AlarmInfo extends JsonModel {
	
	public static final String PRIVATE_ALARM = "PRIVATE";
	public static final String GROUP_ALARM = "GROUP";
	
	public String aid;
	public String type;
	public String time;
	public AlarmMember[] member;
	
	private long mTimeInMs = -1;
	private UserInfo mOwner = null;
	
	/**
	 * Provide for GSON Parser. 
	 * Don't recommend to use this constructor directly.
	 * use 'AlarmInfo( UserInfo owner, long alarmTime )
	 */
	public AlarmInfo(){
	}
	
	public AlarmInfo( UserInfo owner, long alarmTimeMs ){
		mTimeInMs = alarmTimeMs;
		time = Util.getTimeString(alarmTimeMs);
		type = PRIVATE_ALARM;
		member = new AlarmMember[]{ new AlarmMember(owner, AlarmMember.STATUS_ON ) };
		mOwner = owner;
	}
	
	public UserInfo getOwner(){
		return mOwner; 
	}
	
	public long getTime(){
		if(mTimeInMs == -1){
			mTimeInMs = Util.getTime(time);
		}
		return mTimeInMs;
	}
	
	@Override
	public String toPostData() {
		String postStr = "";
		try{
			postStr = String.format("type=%s&time=%s&members[0][uid]=%s&members[0][status]=%s", 
					URLEncoder.encode(type,"utf-8"),
					URLEncoder.encode(time,"utf-8"),
					URLEncoder.encode(member[0].uid,"utf-8"),
					URLEncoder.encode(member[0].status,"utf-8")
					);
		}catch(Exception e){
			return "";
		}
		
		return postStr;
	}

}//end of class
