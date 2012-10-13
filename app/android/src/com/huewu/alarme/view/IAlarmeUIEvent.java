package com.huewu.alarme.view;

import com.huewu.alarme.model.AlarmInfo;

public interface IAlarmeUIEvent {
	
	void onSelectMember();
	
	void onSyncToClock();
	
	void onSetAlarm(AlarmInfo newAlarm);
	
	void onSetGroupAlarm();
	
	void onTurnOffAlarm();
	
	void onUpdateGroupAlarm();

}//end of interface
