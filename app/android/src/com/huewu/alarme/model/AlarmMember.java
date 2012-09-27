package com.huewu.alarme.model;

public class AlarmMember extends JsonModel {
	
	public final static String STATUS_ON = "ON";
	public final static String STATUS_OFF = "OFF";
	
	public String uid;
	public String status;
	
	public AlarmMember(){
		
	}

	public AlarmMember(UserInfo owner, String status) {
		this.uid = owner.uid;
		this.status = status;
	}

	@Override
	public String toPostData() {
		return null;
	}

}//end of clss
