package com.huewu.alarme.service;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.UserInfo;
import com.huewu.libs.network.ResponseListener;

public interface IAlarmService {
	
	/**
	 * create a new user in clock server. (register this phone to server)
	 * @param userInfo.
	 */
	void createUser( UserInfo user, ResponseListener callback );

	/**
	 * delete a user in clock server. (unregister this phone from server)
	 * @param user
	 */
	void deleteUser( UserInfo user, ResponseListener callback );	

	/**
	 * create a new alarm in clock server
	 * @param alarm
	 */
	void setAlarm( AlarmInfo alarm, ResponseListener callback );

	/**
	 * delete an alarm from clock server.
	 * @param alarm
	 */
	void offAlaram( AlarmInfo alarm, ResponseListener callback );

	/**
	 * update a group alarm info in server (change alarm status of this phone - uid)
	 * @param alarm
	 */
	void updateGroupAlaram( AlarmInfo alarm, ResponseListener callback );

	/**
	 * for clock. get alarm information list.
	 * @param cid
	 * @return alarm list.
	 */
	AlarmInfo[] getAlarmInfoList( String cid );
	
}//end of interface.
