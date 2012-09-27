package com.huewu.alarme;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.AlarmMember;
import com.huewu.alarme.model.GroupAlarmInfo;
import com.huewu.alarme.model.UserInfo;

public class DummyFactory {
	
	public static UserInfo createDummyUserInfo(){
		UserInfo user = new UserInfo();
		user.uid = "xxxx";
		user.uname = "huewu";
		user.rid = "1234";
		user.cid = "4321";
		return user;
	}
	
	public static AlarmInfo createDummyAlarmInfo(){
		AlarmInfo alarm = new AlarmInfo( createDummyUserInfo(), 5000);
		return alarm;
	}
	
	public static AlarmMember createDummyMemberInfo(){
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
