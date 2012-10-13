package com.huewu.alarme.view;

import java.util.ArrayList;

import com.huewu.alarme.model.AlarmInfo;

public interface IAlarmeUIEvent {
	
	void onSelectMember();
	
	void onClockRegistered();
	
	void onSetAlarm(AlarmInfo newAlarm);
	
	void onTurnOffAlarm();
	
	void onUpdateGroupAlarm();

	void onAddMembers(ArrayList<String> members);

}//end of interface
