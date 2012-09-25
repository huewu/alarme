package com.huewu.alarme;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.AlarmMember;
import com.huewu.alarme.model.GroupAlarmInfo;
import com.huewu.alarme.model.UserInfo;

public class DummyFactory {
	
	public UserInfo createDummyUserInfo(){
		UserInfo user = new UserInfo();
		user.uid = "xxxx";
		user.uname = "huewu";
		user.rid = "1234";
		user.cid = "4321";
		return user;
	}
	
	public AlarmInfo createDummyAlarmInfo(){
		AlarmInfo alarm = new AlarmInfo();
		alarm.aid = "xxxx";
		alarm.time = "2012-09-19T02:18:26.421Z";
		alarm.type = AlarmInfo.PRIVATE_ALARM;
		alarm.member = new AlarmMember[]{ createDummyMemberInfo() };
		
		return alarm;
	}
	
	public AlarmMember createDummyMemberInfo(){
		AlarmMember member = new AlarmMember();
		member.uid = createDummyUserInfo().uid;
		member.status = AlarmMember.STATUS_OFF;
		return member;
	}
	
	public GroupAlarmInfo createDummyGroupAlarmInfo(){
		return new GroupAlarmInfo();
	}
	
	private DummyFactory(){	}

}//end of class
