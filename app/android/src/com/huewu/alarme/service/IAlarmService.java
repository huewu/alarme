package com.huewu.alarme.service;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.GroupAlarmInfo;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.service.network.ResponseCallback;

public interface IAlarmService {
	
	/**
	 * create a new user in clock server. (register this phone to server)
	 * @param userInfo.
	 */
	void createUser( UserInfo user, ResponseCallback<UserInfo> callback );

	/**
	 * delete a user in clock server. (unregister this phone from server)
	 * @param user
	 */
	void deleteUser( UserInfo user, ResponseCallback<UserInfo> callback );	

	/**
	 * create a new alarm in clock server
	 * @param alarm
	 */
	void setAlaram( AlarmInfo alarm, ResponseCallback<AlarmInfo> callback );

	/**
	 * delete an alarm from clock server.
	 * @param alarm
	 */
	void offAlaram( AlarmInfo alarm, ResponseCallback<AlarmInfo> callback );

	/**
	 * create a new group alarm in clock server (this phone is a master)
	 * @param alarm
	 */
	void setGroupAlarm( AlarmInfo alarm, ResponseCallback<AlarmInfo> callback );

	/**
	 * update a group alarm info in server (change alarm status of this phone - uid)
	 * @param alarm
	 */
	void updateGroupAlaram( AlarmInfo alarm, ResponseCallback<AlarmInfo> callback );

	/**
	 * for clock. get alarm information list.
	 * @param cid
	 * @return alarm list.
	 */
	AlarmInfo[] getAlarmInfoList( String cid );
	
}//end of interface.
