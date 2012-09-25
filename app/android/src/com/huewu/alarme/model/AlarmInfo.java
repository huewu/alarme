package com.huewu.alarme.model;

import java.util.Date;

/**
 * Data class which represent one alarm setting.
 * @author huewu.yang
 *
 */
public class AlarmInfo extends JsonModel {
	
	public String aid;
	public String type;
	public String time;
	public AlarmMember[] member;
	
	@Override
	public String toPostData() {
		return "aid=" + aid;
	}

}//end of class
