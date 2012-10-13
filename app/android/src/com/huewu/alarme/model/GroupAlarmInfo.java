package com.huewu.alarme.model;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Data class which represent one alarm setting.
 * @author huewu.yang
 *
 */
public class GroupAlarmInfo extends AlarmInfo {

	public GroupAlarmInfo(AlarmInfo alarm, String[] memberNames) {
		super(alarm.getOwner(), alarm.getTime());
		
		ArrayList<AlarmMember> members = new ArrayList<AlarmMember>();
		members.add(new AlarmMember(getOwner(), AlarmMember.STATUS_ON));
		
		for( String memberName : memberNames ){
			members.add(new AlarmMember(memberName, AlarmMember.STATUS_OFF));
		}

		this.type = AlarmInfo.GROUP_ALARM;
		this.members = members.toArray(new AlarmMember[]{});
	}
	
	@Override
	public String toPostData() {
		String postStr = "";
		try{
			postStr = String.format("type=%s&time=%s",
					URLEncoder.encode(type,"utf-8"),
					URLEncoder.encode(time,"utf-8"));
			
			int index = 0;
			for( AlarmMember member : members ){
				postStr += String.format("&members[%d][uid]=%s&members[%d][status]=%s", 
						index,
						URLEncoder.encode(members[index].uid,"utf-8"),
						index,
						URLEncoder.encode(members[index].status,"utf-8")
						);
				++index;
			}
		}catch(Exception e){
			return "";
		}
		
		return postStr;
	}
	
}//end of class
