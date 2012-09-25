package com.huewu.alarme.service.network.request;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.service.network.JsonRequest;

public class SetAlarmRequest extends JsonRequest<AlarmInfo>{

	public SetAlarmRequest(AlarmInfo info) {
		super(Method.POST, "");
	}

}//end of class
