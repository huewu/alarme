package com.huewu.alarme.service;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.GroupAlarmInfo;
import com.huewu.alarme.model.UserInfo;

public interface IAlarmService {
	
	/**
	 * create a new user in clock server. (register this phone to server)
	 * @param userInfo.
	 */
	void createUser( UserInfo user );

	/**
	 * delete a user in clock server. (unregister this phone from server)
	 * @param user
	 */
	void deleteUser( UserInfo user );	

	/**
	 * create a new alarm in clock server
	 * @param alarm
	 */
	void setAlaram( AlarmInfo alarm );

	/**
	 * delete an alarm from clock server.
	 * @param alarm
	 */
	void offAlaram( AlarmInfo alarm );

	/**
	 * create a new group alarm in clock server (this phone is a master)
	 * @param alarm
	 */
	void setGroupAlarm( AlarmInfo alarm );

	/**
	 * update a group alarm info in server (change alarm status of this phone - uid)
	 * @param alarm
	 */
	void updateGroupAlaram( AlarmInfo alarm );

	/**
	 * for clock. get alarm information list.
	 * @param cid
	 * @return alarm list.
	 */
	AlarmInfo[] getAlarmInfoList( String cid );
	
}//end of interface.
