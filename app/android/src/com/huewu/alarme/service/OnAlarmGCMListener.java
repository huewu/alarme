package com.huewu.alarme.service;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.GroupAlarmInfo;

public interface OnAlarmGCMListener {

	/**
	 * this callback is invoked when GCM send off alarm message.
	 * @param alarm
	 */
	void onOffAlarm( AlarmInfo alarm );	//callback. 

	/**
	 * this callback is invoked when GCM send update alarm message.
	 * @param alarm
	 */
	void onUpdateAlarm( GroupAlarmInfo alarm );	//callback.

	/**
	 * this callback is invoked when GCM send accept alarm message.
	 * @param alarm
	 */
	void onRequestSetAlarm( GroupAlarmInfo alarm );	//callback.

}//end of interface
